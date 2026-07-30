package com.iti.linguaquest.features.leaderboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.connectivity.NetworkMonitor
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.onFailure
import com.iti.linguaquest.core.result.onSuccess
import com.iti.linguaquest.features.leaderboard.domain.model.LeaderboardScope
import com.iti.linguaquest.features.leaderboard.domain.usecase.GetLeaderboardUseCase
import com.iti.linguaquest.features.leaderboard.presentation.contract.LeaderboardIntent
import com.iti.linguaquest.features.leaderboard.presentation.contract.LeaderboardState
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
class LeaderboardViewModel @Inject constructor(
    private val getLeaderboardUseCase: GetLeaderboardUseCase,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 10
        private const val FIRST_PAGE = 0
    }

    private val _state = MutableStateFlow(LeaderboardState())
    val state: StateFlow<LeaderboardState> = _state.asStateFlow()
    val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()

        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )


    init {
        onIntent(LeaderboardIntent.LoadLeaderboard)
    }

    fun onIntent(intent: LeaderboardIntent) {
        when (intent) {

            LeaderboardIntent.LoadLeaderboard -> loadLeaderboard()

            LeaderboardIntent.LoadMore -> loadMore()

            is LeaderboardIntent.ChangeScope -> onChangeScope(intent)

        }
    }

    private fun onChangeScope(intent: LeaderboardIntent.ChangeScope) {
        _state.update {
            it.copy(scope = intent.scope, languageId = intent.languageId)
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
                    errorMessage = null,
                    currentPage = FIRST_PAGE,
                    endReached = false
                )
            }

            getLeaderboardUseCase(
                scope = scope,
                languageId = languageId,
                page = FIRST_PAGE,
                limit = PAGE_SIZE
            )
                .onSuccess { leaderboard ->

                    _state.update {
                        it.copy(
                            isLoading = false,
                            leaderboard = leaderboard,
                            currentPage = FIRST_PAGE,
                            endReached = leaderboard.entries.size < PAGE_SIZE
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

    private fun loadMore() {
        val current = _state.value

         if (current.isLoading || current.isLoadingMore || current.endReached || current.leaderboard == null) {
            return
        }

        val nextPage = current.currentPage + 1

        viewModelScope.launch {
            _state.update { it.copy(isLoadingMore = true) }

            getLeaderboardUseCase(
                scope = current.scope,
                languageId = current.languageId,
                page = nextPage,
                limit = PAGE_SIZE
            )
                .onSuccess { newPage ->
                    _state.update {
                        val mergedEntries = (it.leaderboard?.entries.orEmpty()) + newPage.entries
                        it.copy(
                            isLoadingMore = false,
                            currentPage = nextPage,
                            endReached = newPage.entries.size < PAGE_SIZE,
                            leaderboard = it.leaderboard?.copy(entries = mergedEntries)
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoadingMore = false,
                            errorMessage = error.toString()
                        )
                    }
                }
        }
    }
}