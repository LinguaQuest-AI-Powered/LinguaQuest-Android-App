package com.iti.linguaquest.features.profile.presentation.viewModel


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.features.profile.domain.usecase.GetCachedAvatarUrlUseCase
import com.iti.linguaquest.features.profile.domain.usecase.GetProfileSummaryUseCase
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileSummaryUseCase: GetProfileSummaryUseCase,
    private val uploadAvatarUseCase: UploadAvatarUseCase,
    private val getCachedAvatarUrlUseCase: GetCachedAvatarUrlUseCase,
    private val snackbarController: SnackbarController,
    private val preloadImageUseCase: PreloadImageUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect: SharedFlow<ProfileEffect> = _effect.asSharedFlow()

    init {
        seedCachedAvatar()
        loadProfile()
    }

    private fun seedCachedAvatar() {
        viewModelScope.launch {
            getCachedAvatarUrlUseCase().firstOrNull()?.let { cachedUrl ->
                _state.update { it.copy(profile = it.profile.copy(avatarUrl = cachedUrl)) }
            }
        }
    }

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.LoadProfile, ProfileIntent.Retry -> loadProfile()
            ProfileIntent.SettingsClicked -> sendEffect(ProfileEffect.NavigateToSettings)
            ProfileIntent.ChangeLanguageClicked -> sendEffect(ProfileEffect.NavigateToChangeLanguage)
            ProfileIntent.ViewAllAchievementsClicked -> sendEffect(ProfileEffect.NavigateToAllAchievements)
            ProfileIntent.ViewAllLeaderboardClicked -> sendEffect(ProfileEffect.NavigateToAllLeaderboard)
            is ProfileIntent.AvatarPicked -> uploadAvatar(intent.uri)
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, hasError = false) }

            when (val result = getProfileSummaryUseCase()) {
                is LinguaQuestResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            hasError = false,
                            profile = result.data.toProfileState()
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
                            onAction = { loadProfile() }
                        )
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
                    message = UiText.DynamicString("Uploading your photo, this may take a moment..."),
                    type = SnackbarType.INFO
                )
            )

            when (val result = uploadAvatarUseCase(uri)) {
                is LinguaQuestResult.Success -> {
                    preloadImageUseCase(result.data)

                    _state.update {
                        it.copy(
                            profile = it.profile.copy(avatarUrl = result.data),
                            isAvatarUploading = false
                        )
                    }
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

    private fun sendEffect(effect: ProfileEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}