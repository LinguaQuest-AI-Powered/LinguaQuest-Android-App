package com.iti.linguaquest.features.home.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.connectivity.NetworkMonitor
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.core.utils.DailyRewardSessionState
import com.iti.linguaquest.core.wallet.domain.usecase.RefreshWalletUseCase
import com.iti.linguaquest.features.all_worlds.domain.usecase.GetWorldsUseCase
import com.iti.linguaquest.features.home.domain.usecase.ClaimDailyRewardUseCase
import com.iti.linguaquest.features.home.domain.usecase.GetDailyRewardStatusUseCase
import com.iti.linguaquest.features.home.domain.usecase.GetHomeSummaryUseCase
import com.iti.linguaquest.features.home.presentation.contract.HomeEffect
import com.iti.linguaquest.features.home.presentation.contract.HomeIntent
import com.iti.linguaquest.features.home.presentation.contract.HomeState
import com.iti.linguaquest.features.home.presentation.mapper.toLanguageProgressUi
import com.iti.linguaquest.features.home.presentation.mapper.toUi
import com.iti.linguaquest.features.home.presentation.mapper.toUiWorldItem
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
    private val getWorldsUseCase: GetWorldsUseCase,
    private val getDailyRewardStatusUseCase: GetDailyRewardStatusUseCase,
    private val claimDailyRewardUseCase: ClaimDailyRewardUseCase,
    private val snackbarController: SnackbarController,
    private val refreshWalletUseCase: RefreshWalletUseCase,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

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
            is HomeIntent.WorldClicked -> sendEffect(HomeEffect.NavigateToWorld(intent.world.id))
            HomeIntent.StartVoicePractiseClicked -> sendEffect(HomeEffect.NavigateToVoiceGame)
            HomeIntent.RoleplayCardClicked -> sendEffect(HomeEffect.NavigateToRoleplayList)
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
                        startVoicePractise = true
                    )
                }
            }
        }
    }

    private fun refreshFromRemote() {
        viewModelScope.launch {
            val hasCache = state.value.xp > 0 || state.value.worlds.isNotEmpty()
            if (!hasCache) {
                _state.update { it.copy(isLoading = true, hasError = false) }
            }

            val homeSummaryDeferred = async { getHomeSummaryUseCase.refresh() }
            val dailyRewardDeferred = async { getDailyRewardStatusUseCase() }
            val walletDeferred = async { refreshWalletUseCase() }

            val homeSummaryResult = homeSummaryDeferred.await()
            val dailyRewardResult = dailyRewardDeferred.await()
            walletDeferred.await()

            _state.update { it.copy(isLoading = false) }

            if (homeSummaryResult is LinguaQuestResult.Success) {
                val dailyRewardUi = (dailyRewardResult as? LinguaQuestResult.Success)?.data?.toUi()
                val shouldShowBanner = dailyRewardUi != null &&
                        !dailyRewardUi.claimedToday &&
                        !DailyRewardSessionState.hasAutoShownThisSession

                if (shouldShowBanner) DailyRewardSessionState.hasAutoShownThisSession = true

                _state.update {
                    it.copy(
                        hasError = false,
                        dailyReward = dailyRewardUi,
                        isDailyRewardBannerVisible = shouldShowBanner
                    )
                }
            } else {
                val errorResult = homeSummaryResult as LinguaQuestResult.Failure
                _state.update { it.copy(hasError = true) }
                snackbarController.sendEvent(
                    SnackbarEvent(
                        message = errorResult.error.toUiText(),
                        type = SnackbarType.ERROR,
                        actionLabel = UiText.DynamicString("Retry"),
                        onAction = { refreshFromRemote() }
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
                            dailyReward = it.dailyReward?.copy(claimedToday = true),
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
