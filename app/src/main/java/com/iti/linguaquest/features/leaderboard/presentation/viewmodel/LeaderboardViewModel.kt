package com.iti.linguaquest.features.leaderboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.result.onFailure
import com.iti.linguaquest.core.result.onSuccess
import com.iti.linguaquest.features.leaderboard.domain.model.LeaderboardScope
import com.iti.linguaquest.features.leaderboard.domain.usecase.GetLeaderboardUseCase
import com.iti.linguaquest.features.leaderboard.presentation.contract.LeaderboardIntent
import com.iti.linguaquest.features.leaderboard.presentation.contract.LeaderboardState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val getLeaderboardUseCase: GetLeaderboardUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LeaderboardState())
    val state: StateFlow<LeaderboardState> = _state.asStateFlow()

    init {
        onIntent(LeaderboardIntent.LoadLeaderboard)
    }

    fun onIntent(intent: LeaderboardIntent) {
        when (intent) {

            LeaderboardIntent.LoadLeaderboard -> loadLeaderboard()

            is LeaderboardIntent.ChangeScope -> onChangeScope(intent)

        }
    }

    private fun onChangeScope(intent: LeaderboardIntent.ChangeScope) {
        _state.update {
            it.copy(scope = intent.scope)
        }

        loadLeaderboard(
            scope = intent.scope,
            languageId = intent.languageId
        )
    }

    private fun loadLeaderboard(
        scope: LeaderboardScope = LeaderboardScope.GLOBAL,
        languageId: Int? = null
    ) {

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            getLeaderboardUseCase(
                scope = scope,
                languageId = languageId
            )
                .onSuccess { leaderboard ->

                    _state.update {
                        it.copy(
                            isLoading = false,
                            leaderboard = leaderboard
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.toString()
                        )
                    }
                }
        }
    }
}