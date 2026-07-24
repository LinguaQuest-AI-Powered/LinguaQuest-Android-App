package com.iti.linguaquest.features.home.presentation.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.core.utils.DailyRewardSessionState
import com.iti.linguaquest.features.all_worlds.domain.usecase.GetWorldsUseCase
import com.iti.linguaquest.features.home.domain.usecase.ClaimDailyRewardUseCase
import com.iti.linguaquest.features.home.domain.usecase.GetDailyRewardStatusUseCase
import com.iti.linguaquest.features.home.domain.usecase.GetHomeSummaryUseCase
import com.iti.linguaquest.features.home.presentation.contract.HomeEffect
import com.iti.linguaquest.features.home.presentation.contract.HomeIntent
import com.iti.linguaquest.features.home.presentation.contract.HomeState
import com.iti.linguaquest.features.home.presentation.mapper.toLanguageProgressUi
import com.iti.linguaquest.features.home.presentation.mapper.toUi
import com.iti.linguaquest.features.home.presentation.mapper.toUiLessonPreview
import com.iti.linguaquest.features.home.presentation.mapper.toUiWorldItem
import com.iti.linguaquest.features.all_worlds.domain.model.World
import com.iti.linguaquest.features.all_worlds.domain.model.WorldStatus
import com.iti.linguaquest.features.all_worlds.domain.model.WorldDifficulty as DomainWorldDifficulty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

private val mockExploreWorlds = listOf(
    World(10, "Kitchen World", "/media/worlds/kitchen.jpg", DomainWorldDifficulty.EASY, WorldStatus.IN_PROGRESS, 40, 20, 8),
    World(11, "City World", "/media/worlds/city.jpg", DomainWorldDifficulty.MEDIUM, WorldStatus.IN_PROGRESS, 10, 20, 2),
    World(12, "Park World", "/media/worlds/park.jpg", DomainWorldDifficulty.EASY, WorldStatus.LOCKED, 0, 20, 0),
    World(13, "School World", "/media/worlds/school.jpg", DomainWorldDifficulty.HARD, WorldStatus.LOCKED, 0, 20, 0)
).map { it.toUiWorldItem() }

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeSummaryUseCase: GetHomeSummaryUseCase,
    private val getWorldsUseCase: GetWorldsUseCase,
    private val getDailyRewardStatusUseCase: GetDailyRewardStatusUseCase,
    private val claimDailyRewardUseCase: ClaimDailyRewardUseCase,
    private val snackbarController: SnackbarController
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect: SharedFlow<HomeEffect> = _effect.asSharedFlow()

    init {
        loadHome()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadHome, HomeIntent.Retry -> loadHome()
            is HomeIntent.WorldClicked -> sendEffect(HomeEffect.NavigateToWorld(intent.world.id))
            HomeIntent.ContinueLessonClicked -> {
                _state.value.continueLesson?.let { lesson ->
                    sendEffect(HomeEffect.NavigateToVoiceGame(lesson.lessonId, lesson.sentence))
                }
            }

            HomeIntent.SeeMoreWorldsClicked -> sendEffect(HomeEffect.NavigateToAllWorlds)

            HomeIntent.FabClicked -> {
                _state.update { it.copy(isLanguageBottomSheetVisible = true) }
            }

            HomeIntent.DismissLanguageBottomSheet -> {
                _state.update { it.copy(isLanguageBottomSheetVisible = false) }
            }

            HomeIntent.AddNewLanguageClicked -> {
                _state.update { it.copy(isLanguageBottomSheetVisible = false) }
                sendEffect(HomeEffect.NavigateToAddLanguages)
            }

            HomeIntent.DailyRewardBannerClicked -> {
                _state.update {
                    it.copy(
                        isDailyRewardDialogVisible = true,
                        isDailyRewardBannerVisible = false
                    )
                }
            }

            HomeIntent.DismissDailyRewardBanner -> _state.update {
                it.copy(
                    isDailyRewardBannerVisible = false
                )
            }

            HomeIntent.DismissDailyRewardDialog -> _state.update {
                it.copy(
                    isDailyRewardDialogVisible = false
                )
            }

            HomeIntent.ClaimDailyRewardClicked -> claimDailyReward()
        }
    }

    private fun loadHome() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, hasError = false) }

            val homeSummaryDeferred = async { getHomeSummaryUseCase() }
            val dailyRewardDeferred = async { getDailyRewardStatusUseCase() }

            val homeSummaryResult = homeSummaryDeferred.await()
            val dailyRewardResult = dailyRewardDeferred.await()

            if (homeSummaryResult is LinguaQuestResult.Success) {
                val summary = homeSummaryResult.data
                val dailyRewardUi = (dailyRewardResult as? LinguaQuestResult.Success)?.data?.toUi()
                val shouldShowBanner = dailyRewardUi != null &&
                        !dailyRewardUi.claimedToday &&
                        !DailyRewardSessionState.hasAutoShownThisSession

                if (shouldShowBanner) DailyRewardSessionState.hasAutoShownThisSession = true

                _state.update {
                    it.copy(
                        isLoading = false,
                        hasError = false,
                        languageProgress = summary.toLanguageProgressUi(),
                        worlds = mockExploreWorlds,
                        continueLesson = summary.continueLesson?.toUiLessonPreview(),
                        dailyReward = dailyRewardUi,
                        isDailyRewardBannerVisible = shouldShowBanner
                    )
                }
            } else {
                val errorResult = homeSummaryResult as LinguaQuestResult.Failure
                _state.update { it.copy(isLoading = false, hasError = true) }
                snackbarController.sendEvent(
                    SnackbarEvent(
                        message = errorResult.error.toUiText(),
                        type = SnackbarType.ERROR,
                        actionLabel = UiText.DynamicString("Retry"),
                        onAction = { loadHome() }
                    )
                )
            }
        }
    }

    private fun claimDailyReward() {
        viewModelScope.launch {
            when (val result = claimDailyRewardUseCase()) {
                is LinguaQuestResult.Success -> {
                    _state.update {
                        it.copy(
                            isDailyRewardDialogVisible = false,
                            dailyReward = it.dailyReward?.copy(claimedToday = true)
                        )
                    }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = UiText.DynamicString("+${result.data.coinsAwarded} coins claimed!"),
                            type = SnackbarType.SUCCESS
                        )
                    )
                }

                is LinguaQuestResult.Failure -> {
                    _state.update { it.copy(isDailyRewardDialogVisible = false) }
                    snackbarController.sendEvent(
                        SnackbarEvent(message = result.error.toUiText(), type = SnackbarType.ERROR)
                    )
                }
            }
        }
    }


    private fun sendEffect(effect: HomeEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
