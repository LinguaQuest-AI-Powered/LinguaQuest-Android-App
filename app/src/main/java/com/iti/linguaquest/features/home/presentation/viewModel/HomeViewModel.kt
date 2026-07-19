package com.iti.linguaquest.features.home.presentation.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.features.home.domain.usecase.GetHomeSummaryUseCase
import com.iti.linguaquest.features.home.presentation.contract.HomeEffect
import com.iti.linguaquest.features.home.presentation.contract.HomeIntent
import com.iti.linguaquest.features.home.presentation.contract.HomeState
import com.iti.linguaquest.features.home.presentation.mapper.toLanguageProgressUi
import com.iti.linguaquest.features.home.presentation.mapper.toUiLessonPreview
import com.iti.linguaquest.features.home.presentation.mapper.toUiWorldItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeSummaryUseCase: GetHomeSummaryUseCase,
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
                    sendEffect(HomeEffect.NavigateToLessonDetails(lesson.lessonId))
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
        }
    }

    private fun loadHome() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, hasError = false) }

            when (val result = getHomeSummaryUseCase()) {
                is LinguaQuestResult.Success -> {
                    val summary = result.data
                    _state.update {
                        it.copy(
                            isLoading = false,
                            hasError = false,
                            languageProgress = summary.toLanguageProgressUi(),
                            worlds = summary.exploreWorlds.map { world -> world.toUiWorldItem() },
                            continueLesson = summary.continueLesson?.toUiLessonPreview()
                        )
                    }
                }
                is LinguaQuestResult.Failure -> {
                    _state.update { it.copy(isLoading = false, hasError = true) }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = result.error.toUiText(),
                            type = SnackbarType.ERROR,
                            actionLabel = UiText.DynamicString("Retry"),
                            onAction = { loadHome() }
                        )
                    )
                }
            }
        }
    }

    private fun sendEffect(effect: HomeEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
