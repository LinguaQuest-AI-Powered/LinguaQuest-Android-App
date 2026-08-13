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
import com.iti.linguaquest.features.home.domain.usecase.RefreshMyLanguagesUseCase
import com.iti.linguaquest.features.home.presentation.contract.HomeEffect
import com.iti.linguaquest.features.home.presentation.contract.HomeIntent
import com.iti.linguaquest.features.home.presentation.contract.HomeState
import com.iti.linguaquest.core.sharedComponents.state.DataStatus
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
import kotlinx.coroutines.flow.firstOrNull
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
    private val refreshWalletUseCase: RefreshWalletUseCase,
    private val refreshMyLanguagesUseCase: RefreshMyLanguagesUseCase
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
            HomeIntent.LoadHome -> refreshFromRemote()
            HomeIntent.Retry -> {
                _state.update { it.copy(dataStatus = DataStatus.Loading) }
                refreshFromRemote()
            }
            HomeIntent.Refresh -> {
                val now = System.currentTimeMillis()
                if (now - lastRefreshTime > REFRESH_COOLDOWN_MS && state.value.dataStatus != DataStatus.Refreshing) {
                    lastRefreshTime = now
                    _state.update { it.copy(dataStatus = DataStatus.Refreshing) }
                    refreshFromRemote(isPullToRefresh = true)
                }
            }
            is HomeIntent.WorldClicked -> sendEffect(HomeEffect.NavigateToWorld(intent.world.id, intent.world.totalLevels))
            HomeIntent.SeeMoreWorldsClicked -> sendEffect(HomeEffect.NavigateToAllWorlds)
            HomeIntent.FabClicked -> _state.update { it.copy(isLanguageBottomSheetVisible = true) }
            HomeIntent.DismissLanguageBottomSheet -> _state.update { it.copy(isLanguageBottomSheetVisible = false) }
            HomeIntent.AddNewLanguageClicked -> {
                _state.update { it.copy(isLanguageBottomSheetVisible = false, restoreLanguageBottomSheet = true) }
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
            is HomeIntent.ContinueLevelClicked -> sendEffect(HomeEffect.NavigateToContinueLevel(intent.continueLevel.worldId, intent.continueLevel.levelId, intent.continueLevel.levelOrder, intent.continueLevel.totalLevels, intent.continueLevel.targetWord))
            HomeIntent.TriggerDailyMission -> triggerDailyMission()
            HomeIntent.DismissDailyMissionDialog -> _state.update { it.copy(dailyMissionState = DailyMissionDialogState.Hidden) }
            is HomeIntent.StartDailyMissionCamera -> {
                _state.update { it.copy(dailyMissionState = DailyMissionDialogState.Hidden) }
                sendEffect(HomeEffect.NavigateToDailyMissionCamera(intent.word))
            }
            HomeIntent.PrepareLanguageSwitch -> {
                _state.update { it.copy(dataStatus = DataStatus.Loading) }
            }
            HomeIntent.CancelLanguageSwitch -> {
                val hasCache = _state.value.hasData
                _state.update { it.copy(dataStatus = if (hasCache) DataStatus.Loaded else DataStatus.Error(UiText.StringResource(R.string.error_generic))) }
            }
            HomeIntent.ScreenResumed -> {
                if (_state.value.restoreLanguageBottomSheet) {
                    _state.update {
                        it.copy(
                            isLanguageBottomSheetVisible = true,
                            restoreLanguageBottomSheet = false
                        )
                    }
                }
            }
        }
    }

    private fun triggerDailyMission() {
        viewModelScope.launch {
            _state.update { it.copy(dailyMissionState = DailyMissionDialogState.Loading) }
            
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
                if (summary == null) {
                    return@collect
                }
                _state.update { current ->
                    current.copy(
                        dataStatus = DataStatus.Loaded,
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
                val dailyMissionDeferred = async { getDailyMissionWordUseCase(forceRefresh = true) }
                val walletDeferred = if (isPullToRefresh) async { refreshWalletUseCase() } else null
                val languagesDeferred = if (isPullToRefresh) async { refreshMyLanguagesUseCase() } else null

                val homeSummaryResult = homeSummaryDeferred.await()
                val dailyRewardResult = dailyRewardDeferred.await()
                dailyMissionDeferred.await()
                walletDeferred?.await()
                languagesDeferred?.await()

                if (homeSummaryResult is LinguaQuestResult.Success) {
                    val dailyRewardUi = (dailyRewardResult as? LinguaQuestResult.Success)?.data?.toUi()
                    val shouldShowBanner = dailyRewardUi != null &&
                            !dailyRewardUi.claimedToday &&
                            (!DailyRewardSessionState.hasAutoShownThisSession || isPullToRefresh)

                    if (shouldShowBanner) DailyRewardSessionState.hasAutoShownThisSession = true

                    _state.update {
                        it.copy(
                            dataStatus = DataStatus.Loaded,
                            dailyReward = dailyRewardUi,
                            isDailyRewardBannerVisible = shouldShowBanner
                        )
                    }
                } else {
                    val dataError = (homeSummaryResult as? LinguaQuestResult.Failure)?.error as? LinguaQuestDataError
                    val errorUiText = dataError?.toUiText() ?: UiText.StringResource(R.string.error_generic)
                    val isOffline = dataError == LinguaQuestDataError.Remote.NO_INTERNET
                    val hasCache = _state.value.hasData || getHomeSummaryUseCase.observe().firstOrNull() != null

                    _state.update {
                        it.copy(
                            dataStatus = if (hasCache) DataStatus.Loaded else DataStatus.Error(errorUiText)
                        )
                    }

                    if (hasCache) {
                        if (isOffline) {
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
                val hasCache = _state.value.hasData || getHomeSummaryUseCase.observe().firstOrNull() != null
                val errorUiText = UiText.StringResource(R.string.error_generic)
                _state.update {
                    it.copy(
                        dataStatus = if (hasCache) DataStatus.Loaded else DataStatus.Error(errorUiText)
                    )
                }
                if (hasCache) {
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
    }

    private fun claimDailyReward() {
        viewModelScope.launch {
            _state.update { it.copy(isClaimingReward = true) }
            when (val result = claimDailyRewardUseCase()) {
                is LinguaQuestResult.Success -> {
                    _state.update {
                        it.copy(
                            isClaimingReward = false,
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
                    _state.update { it.copy(isDailyRewardDialogVisible = false, isClaimingReward = false) }
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
