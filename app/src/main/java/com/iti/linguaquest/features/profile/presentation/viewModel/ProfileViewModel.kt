package com.iti.linguaquest.features.profile.presentation.viewModel


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.features.profile.domain.usecase.GetCachedProfileUseCase
import com.iti.linguaquest.features.profile.domain.usecase.RefreshProfileSummaryUseCase
import com.iti.linguaquest.features.profile.domain.usecase.PreloadImageUseCase
import com.iti.linguaquest.features.profile.domain.usecase.UploadAvatarUseCase
import com.iti.linguaquest.features.profile.presentation.contract.ProfileEffect
import com.iti.linguaquest.features.profile.presentation.contract.ProfileIntent
import com.iti.linguaquest.features.profile.presentation.contract.ProfileUiState
import com.iti.linguaquest.features.profile.presentation.mapper.toProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect: SharedFlow<ProfileEffect> = _effect.asSharedFlow()

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
                            hasCachedData = true,
                            isLoading = false,
                            hasError = false
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.LoadProfile, ProfileIntent.Retry -> refreshProfile()
            ProfileIntent.SettingsClicked -> sendEffect(ProfileEffect.NavigateToSettings)
            ProfileIntent.ViewAllAchievementsClicked -> sendEffect(ProfileEffect.NavigateToAllAchievements)
            ProfileIntent.ViewAllLeaderboardClicked -> sendEffect(ProfileEffect.NavigateToAllLeaderboard)
            is ProfileIntent.AvatarPicked -> uploadAvatar(intent.uri)
        }
    }

    private fun refreshProfile() {
        viewModelScope.launch {
            val hasCache = getCachedProfileUseCase().firstOrNull() != null
            _state.update {
                it.copy(
                    isLoading = !hasCache,
                    hasError = false,
                    isOffline = false
                )
            }

            when (val result = refreshProfileSummaryUseCase()) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(isLoading = false, isOffline = false) }
                }

                is LinguaQuestResult.Failure -> {
                    val stillHasCache = getCachedProfileUseCase().firstOrNull() != null
                    _state.update {
                        it.copy(
                            isLoading = false,
                            hasError = !stillHasCache,
                            isOffline = stillHasCache && result.error.isNoInternet()
                        )
                    }

                    if (stillHasCache && result.error.isNoInternet()) {
                        snackbarController.sendEvent(
                            SnackbarEvent(
                                title = UiText.DynamicString("You're offline"),
                                message = UiText.DynamicString(
                                    "Showing your saved profile - it'll refresh automatically once you're back online."
                                ),
                                type = SnackbarType.INFO
                            )
                        )
                    } else {
                        snackbarController.sendEvent(
                            SnackbarEvent(
                                message = result.error.toUiText(),
                                type = SnackbarType.ERROR,
                                actionLabel = UiText.DynamicString("Retry"),
                                onAction = { refreshProfile() }
                            )
                        )
                    }
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
                    message = UiText.DynamicString("Uploading your photo, this may take a moment..."),
                    type = SnackbarType.INFO
                )
            )

            when (val result = uploadAvatarUseCase(uri)) {
                is LinguaQuestResult.Success -> {
                    preloadImageUseCase(result.data)
                    _state.update { it.copy(isAvatarUploading = false) }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            title = UiText.DynamicString("Congratulations"),
                            message = UiText.DynamicString("Profile photo updated"),
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
                            actionLabel = UiText.DynamicString("Retry"),
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