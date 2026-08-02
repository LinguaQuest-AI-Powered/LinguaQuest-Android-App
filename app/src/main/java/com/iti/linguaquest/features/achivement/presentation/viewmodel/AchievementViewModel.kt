package com.iti.linguaquest.features.achivement.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.onFailure
import com.iti.linguaquest.core.result.onSuccess
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.features.achivement.domain.model.AchievementFilter
import com.iti.linguaquest.features.achivement.domain.usecase.GetAchievementsUseCase
import com.iti.linguaquest.features.achivement.presentation.contract.AchievementIntent
import com.iti.linguaquest.features.achivement.presentation.contract.AchievementState
import com.iti.linguaquest.features.achivement.presentation.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AchievementViewModel @Inject constructor(
    private val getAchievementsUseCase: GetAchievementsUseCase,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
    private val snackbarController: SnackbarController
) : ViewModel() {

    private val _state = MutableStateFlow(AchievementState())
    val state: StateFlow<AchievementState> = _state.asStateFlow()

    val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )

    init {
        onIntent(AchievementIntent.LoadAchievements)
    }

    fun onIntent(intent: AchievementIntent) {
        when (intent) {
            AchievementIntent.LoadAchievements -> loadAchievements()
            is AchievementIntent.ChangeFilter -> changeFilter(intent.filter)
        }
    }

    private fun changeFilter(filter: AchievementFilter) {
        _state.update { it.copy(filter = filter) }
        loadAchievements(filter)
    }

    private fun loadAchievements(filter: AchievementFilter = _state.value.filter) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            getAchievementsUseCase(filter)
                .onSuccess { data ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            earnedCount = data.earnedCount,
                            inProgressCount = data.inProgressCount,
                            xpEarned = data.xpEarned,
                            achievements = data.achievements.map { achievement -> achievement.toUiModel() }
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.toUiText()
                        )
                    }
                }
        }
    }
}