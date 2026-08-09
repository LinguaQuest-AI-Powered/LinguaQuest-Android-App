package com.iti.linguaquest.features.home.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.core.utils.DailyRewardSessionState
import com.iti.linguaquest.core.wallet.domain.usecase.RefreshWalletUseCase
import com.iti.linguaquest.features.dailymission.domain.usecase.GetDailyMissionWordUseCase
import com.iti.linguaquest.features.home.domain.usecase.ClaimDailyRewardUseCase
import com.iti.linguaquest.features.home.domain.usecase.GetDailyRewardStatusUseCase
import com.iti.linguaquest.features.home.domain.usecase.GetHomeSummaryUseCase
import com.iti.linguaquest.features.home.presentation.contract.HomeEffect
import com.iti.linguaquest.features.home.presentation.contract.HomeIntent
import com.iti.linguaquest.features.home.presentation.contract.HomeState
import com.iti.linguaquest.features.home.presentation.mapper.toLanguageProgressUi
import com.iti.linguaquest.features.home.presentation.mapper.toUi
import com.iti.linguaquest.features.home.presentation.mapper.toContinueLevelUi
import com.iti.linguaquest.features.home.presentation.mapper.toUiWorldItem
import com.iti.linguaquest.features.home.presentation.contract.DailyMissionDialogState
import com.iti.linguaquest.R
import com.iti.linguaquest.core.result.LinguaQuestDataError
import timber.log.Timber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeSummaryUseCase: GetHomeSummaryUseCase,
    private val getDailyMissionWordUseCase: GetDailyMissionWordUseCase,
    private val getDailyRewardStatusUseCase: GetDailyRewardStatusUseCase,
    private val claimDailyRewardUseCase: ClaimDailyRewardUseCase,
    private val snackbarController: SnackbarController,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
    private val refreshWalletUseCase: RefreshWalletUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private var lastRefreshTime = 0L
    private val REFRESH_COOLDOWN_MS = 5000L

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect: SharedFlow<HomeEffect> = _effect.asSharedFlow()
    val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()

        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )
    init {
        observeLocalCache()
        refreshFromRemote()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadHome, HomeIntent.Retry -> refreshFromRemote()
            HomeIntent.Refresh -> {
                val now = System.currentTimeMillis()
                if (now - lastRefreshTime > REFRESH_COOLDOWN_MS && !state.value.isRefreshing) {
                    lastRefreshTime = now
                    _state.update { it.copy(isRefreshing = true) }
                    refreshFromRemote(isPullToRefresh = true)
                } else {
                    _state.update { it.copy(isRefreshing = false) }
                }
            }
            is HomeIntent.WorldClicked -> sendEffect(HomeEffect.NavigateToWorld(intent.world.id))
            HomeIntent.SeeMoreWorldsClicked -> sendEffect(HomeEffect.NavigateToAllWorlds)
            HomeIntent.FabClicked -> _state.update { it.copy(isLanguageBottomSheetVisible = true) }
            HomeIntent.DismissLanguageBottomSheet -> _state.update { it.copy(isLanguageBottomSheetVisible = false) }
            HomeIntent.AddNewLanguageClicked -> {
                _state.update { it.copy(isLanguageBottomSheetVisible = false) }
                sendEffect(HomeEffect.NavigateToAddLanguages)
            }
            HomeIntent.DailyRewardBannerClicked -> _state.update {
                it.copy(isDailyRewardDialogVisible = true, isDailyRewardBannerVisible = false)
            }
            HomeIntent.DismissDailyRewardBanner -> _state.update {
                it.copy(isDailyRewardBannerVisible = false)
            }
            HomeIntent.DismissDailyRewardDialog -> _state.update {
                it.copy(isDailyRewardDialogVisible = false)
            }
            HomeIntent.ClaimDailyRewardClicked -> claimDailyReward()
            is HomeIntent.ContinueLevelClicked -> sendEffect(HomeEffect.NavigateToContinueLevel(intent.continueLevel.worldId, intent.continueLevel.levelId, intent.continueLevel.levelOrder, intent.continueLevel.targetWord))
            HomeIntent.TriggerDailyMission -> triggerDailyMission()
            HomeIntent.DismissDailyMissionDialog -> _state.update { it.copy(dailyMissionState = DailyMissionDialogState.Hidden) }
            is HomeIntent.StartDailyMissionCamera -> {
                _state.update { it.copy(dailyMissionState = DailyMissionDialogState.Hidden) }
                sendEffect(HomeEffect.NavigateToDailyMissionCamera(intent.word))
            }
        }
    }

    private fun triggerDailyMission() {
        viewModelScope.launch {
            _state.update { it.copy(dailyMissionState = com.iti.linguaquest.features.home.presentation.contract.DailyMissionDialogState.Loading) }
            
            when (val result = getDailyMissionWordUseCase()) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(dailyMissionState = DailyMissionDialogState.Success(result.data.word)) }
                }
                is LinguaQuestResult.Failure -> {
                    _state.update { it.copy(dailyMissionState = DailyMissionDialogState.Hidden) }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = result.error.toUiText(),
                            type = SnackbarType.ERROR
                        )
                    )
                }
            }
        }
    }

    private fun observeLocalCache() {
        viewModelScope.launch {
            getHomeSummaryUseCase.observe().collect { summary ->
                summary ?: return@collect
                _state.update { current ->
                    current.copy(
                        xp = summary.xp,
                        coins = summary.coins,
                        languageProgress = summary.toLanguageProgressUi(),
                        worlds = summary.exploreWorlds.map { it.toUiWorldItem() },
                        continueLevel = summary.toContinueLevelUi(),
                        startVoicePractise = true
                    )
                }
            }
        }
    }

    private fun refreshFromRemote(isPullToRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                val homeSummaryDeferred = async { getHomeSummaryUseCase.refresh() }
                val dailyRewardDeferred = async { getDailyRewardStatusUseCase() }
                val walletDeferred = if (isPullToRefresh) async { refreshWalletUseCase() } else null

                val homeSummaryResult = homeSummaryDeferred.await()
                val dailyRewardResult = dailyRewardDeferred.await()
                walletDeferred?.await()

                _state.update { it.copy(isRefreshing = false) }

                if (homeSummaryResult is LinguaQuestResult.Success) {
                    val dailyRewardUi = (dailyRewardResult as? LinguaQuestResult.Success)?.data?.toUi()
                    val shouldShowBanner = dailyRewardUi != null &&
                            !dailyRewardUi.claimedToday &&
                            !DailyRewardSessionState.hasAutoShownThisSession

                    if (shouldShowBanner) DailyRewardSessionState.hasAutoShownThisSession = true

                    _state.update {
                        it.copy(
                            hasError = false,
                            errorMessage = null,
                            dailyReward = dailyRewardUi,
                            isDailyRewardBannerVisible = shouldShowBanner
                        )
                    }
                } else {
                    val dataError = (homeSummaryResult as? LinguaQuestResult.Failure)?.error as? LinguaQuestDataError
                    val errorUiText = dataError?.toUiText() ?: UiText.StringResource(R.string.error_generic)
                    val hasCache = _state.value.worlds.isNotEmpty() || _state.value.languageProgress != null
                    val isNoInternet = dataError == LinguaQuestDataError.Remote.NO_INTERNET

                    _state.update {
                        it.copy(
                            hasError = !hasCache && !isNoInternet,
                            errorMessage = if (!hasCache && !isNoInternet) errorUiText else null
                        )
                    }

                    if (isPullToRefresh || hasCache) {
                        if (hasCache && dataError == LinguaQuestDataError.Remote.NO_INTERNET) {
                            snackbarController.sendEvent(
                                SnackbarEvent(
                                    title = UiText.StringResource(R.string.offline_title),
                                    message = UiText.StringResource(R.string.offline_msg),
                                    type = SnackbarType.INFO
                                )
                            )
                        } else {
                            snackbarController.sendEvent(
                                SnackbarEvent(
                                    message = errorUiText,
                                    type = SnackbarType.ERROR,
                                    actionLabel = UiText.StringResource(R.string.retry),
                                    onAction = { refreshFromRemote(isPullToRefresh = true) }
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error refreshing home data from remote")
                val emptyWorlds = _state.value.worlds.isEmpty()
                val errorUiText = UiText.StringResource(R.string.error_generic)
                _state.update {
                    it.copy(
                        isRefreshing = false,
                        hasError = emptyWorlds,
                        errorMessage = if (emptyWorlds) errorUiText else null
                    )
                }
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
                            dailyReward = it.dailyReward?.copy(
                                claimedToday = true,
                                currentDay = result.data.nextDay
                            ),
                            coins = result.data.newCoinsBalance,
                            xp = result.data.newXpBalance
                        )
                    }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = UiText.DynamicString("+${result.data.coinsAwarded} coins claimed!"),
                            type = SnackbarType.SUCCESS
                        )
                    )
                    refreshWalletUseCase()
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
