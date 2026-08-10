package com.iti.linguaquest.features.profile.presentation.viewModel


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.core.wallet.domain.usecase.RefreshWalletUseCase
import com.iti.linguaquest.features.profile.domain.usecase.GetCachedProfileUseCase
import com.iti.linguaquest.features.profile.domain.usecase.RefreshProfileSummaryUseCase
import com.iti.linguaquest.features.profile.domain.usecase.PreloadImageUseCase
import com.iti.linguaquest.features.profile.domain.usecase.UploadAvatarUseCase
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.state.DataStatus
import com.iti.linguaquest.features.profile.presentation.contract.ProfileEffect
import com.iti.linguaquest.features.profile.presentation.contract.ProfileIntent
import com.iti.linguaquest.features.profile.presentation.contract.ProfileUiState
import com.iti.linguaquest.features.profile.presentation.mapper.toProfileState
import com.iti.linguaquest.features.profile.presentation.model.ProfileState
import timber.log.Timber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCachedProfileUseCase: GetCachedProfileUseCase,
    private val refreshProfileSummaryUseCase: RefreshProfileSummaryUseCase,
    private val uploadAvatarUseCase: UploadAvatarUseCase,
    private val snackbarController: SnackbarController,
    private val preloadImageUseCase: PreloadImageUseCase,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
    private val refreshWalletUseCase: RefreshWalletUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    private var lastRefreshTime = 0L
    private val REFRESH_COOLDOWN_MS = 5000L

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect: SharedFlow<ProfileEffect> = _effect.asSharedFlow()
    val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()

        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )
    init {
        observeCachedProfile()
        refreshProfile()
    }

    private fun observeCachedProfile() {
        getCachedProfileUseCase()
            .onEach { cached ->
                if (cached != null) {
                    _state.update {
                        it.copy(
                            profile = cached.toProfileState(),
                            dataStatus = if (it.dataStatus is DataStatus.Loading) DataStatus.Loaded else it.dataStatus
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.LoadProfile, ProfileIntent.Retry -> refreshProfile()
            ProfileIntent.Refresh -> {
                val now = System.currentTimeMillis()
                if (now - lastRefreshTime > REFRESH_COOLDOWN_MS && state.value.dataStatus !is DataStatus.Refreshing) {
                    lastRefreshTime = now
                    _state.update { it.copy(dataStatus = DataStatus.Refreshing) }
                    refreshProfile(isPullToRefresh = true)
                }
            }
            ProfileIntent.SettingsClicked -> sendEffect(ProfileEffect.NavigateToSettings)
            ProfileIntent.ViewAllAchievementsClicked -> sendEffect(ProfileEffect.NavigateToAllAchievements)
            ProfileIntent.ViewAllLeaderboardClicked -> sendEffect(ProfileEffect.NavigateToAllLeaderboard)
            is ProfileIntent.AvatarPicked -> uploadAvatar(intent.uri)
        }
    }

    private fun refreshProfile(isPullToRefresh: Boolean = false) {
        viewModelScope.launch {
            if (!isPullToRefresh) {
                val hasCachedData = _state.value.hasData || getCachedProfileUseCase().firstOrNull() != null
                _state.update {
                    it.copy(dataStatus = if (hasCachedData) DataStatus.Loaded else DataStatus.Loading)
                }
            }

            try {
                val profileDeferred = async { refreshProfileSummaryUseCase() }
                val walletDeferred = if (isPullToRefresh) async { refreshWalletUseCase() } else null

                val result = profileDeferred.await()
                walletDeferred?.await()

                when (result) {
                    is LinguaQuestResult.Success -> {
                        _state.update { it.copy(dataStatus = DataStatus.Loaded) }
                    }

                    is LinguaQuestResult.Failure -> {
                        val stillHasCache = _state.value.hasData || getCachedProfileUseCase().firstOrNull() != null
                        val dataError = result.error as? LinguaQuestDataError
                        val errorUiText = dataError?.toUiText() ?: UiText.StringResource(R.string.error_generic)
                        val isNoInternet = dataError == LinguaQuestDataError.Remote.NO_INTERNET

                        _state.update {
                            it.copy(
                                dataStatus = if (stillHasCache) DataStatus.Loaded else DataStatus.Error(errorUiText)
                            )
                        }

                        if (isPullToRefresh || stillHasCache) {
                            if (stillHasCache && isNoInternet) {
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
                                        onAction = { refreshProfile(isPullToRefresh = true) }
                                    )
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error refreshing profile data")
                val stillHasCache = _state.value.hasData
                val errorUiText = UiText.StringResource(R.string.error_generic)
                _state.update {
                    it.copy(
                        dataStatus = if (stillHasCache) DataStatus.Loaded else DataStatus.Error(errorUiText)
                    )
                }
            }
        }
    }

    private fun uploadAvatar(uri: Uri) {
        val previousAvatar = _state.value.profile.avatarUrl
        _state.update {
            it.copy(
                profile = it.profile.copy(avatarUrl = uri),
                isAvatarUploading = true
            )
        }

        viewModelScope.launch {
            snackbarController.sendEvent(
                SnackbarEvent(
                    message = UiText.StringResource(R.string.uploading_photo_msg),
                    type = SnackbarType.INFO
                )
            )

            when (val result = uploadAvatarUseCase(uri)) {
                is LinguaQuestResult.Success -> {
                    preloadImageUseCase(result.data)
                    _state.update { it.copy(isAvatarUploading = false) }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            title = UiText.StringResource(R.string.congrates),
                            message = UiText.StringResource(R.string.profile_photo_updated_successfully),
                            type = SnackbarType.SUCCESS
                        )
                    )
                }

                is LinguaQuestResult.Failure -> {
                    _state.update {
                        it.copy(
                            profile = it.profile.copy(avatarUrl = previousAvatar),
                            isAvatarUploading = false
                        )
                    }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = result.error.toUiText(),
                            type = SnackbarType.ERROR,
                            actionLabel = UiText.StringResource(R.string.retry),
                            onAction = { uploadAvatar(uri) }
                        )
                    )
                }
            }
        }
    }
    private fun LinguaQuestDataError.isNoInternet(): Boolean =
        this == LinguaQuestDataError.Remote.NO_INTERNET

    private fun sendEffect(effect: ProfileEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
