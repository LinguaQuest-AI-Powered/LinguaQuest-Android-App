package com.iti.linguaquest.features.profile.presentation.editprofile.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.connectivity.NetworkMonitor
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.profile.domain.usecase.ChangePasswordUseCase
import com.iti.linguaquest.features.profile.domain.usecase.GetCachedProfileUseCase
import com.iti.linguaquest.features.profile.domain.usecase.UploadAvatarUseCase
import com.iti.linguaquest.features.profile.domain.usecase.UpdateProfileUseCase
import com.iti.linguaquest.features.profile.presentation.editprofile.contract.EditProfileIntent
import com.iti.linguaquest.features.profile.presentation.editprofile.contract.EditProfileState
import com.iti.linguaquest.features.profile.presentation.editprofile.utils.FieldError
import kotlinx.coroutines.flow.firstOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.features.profile.domain.usecase.PreloadImageUseCase

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val uploadAvatarUseCase: UploadAvatarUseCase,
    private val snackbarController: SnackbarController,
    private val getCachedProfileUseCase: GetCachedProfileUseCase,
    private val networkMonitor: NetworkMonitor

    private val getCachedProfileUseCase: GetCachedProfileUseCase,
    private val preloadImageUseCase: PreloadImageUseCase
) : ViewModel() {
    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )
    private val _state = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = _state.asStateFlow()

    init {
        loadCachedProfile()
    }

    private fun loadCachedProfile() {
        viewModelScope.launch {
            val cached = getCachedProfileUseCase().firstOrNull()
            if (cached != null) {
                _state.update {
                    it.copy(
                        displayName = cached.username,
                        avatarModel = cached.photoUrl
                    )
                }
            }
        }
    }

    fun onIntent(intent: EditProfileIntent) {
        when (intent) {
            is EditProfileIntent.OnDisplayNameChanged ->
                _state.update { it.copy(displayName = intent.name, displayNameError = FieldError()) }

            is EditProfileIntent.OnOldPasswordChanged ->
                _state.update { it.copy(oldPassword = intent.password, oldPasswordError = FieldError()) }

            is EditProfileIntent.OnNewPasswordChanged ->
                _state.update { it.copy(newPassword = intent.password, newPasswordError = FieldError()) }

            is EditProfileIntent.UploadPhoto ->
                uploadPhoto(intent.uri)

            is EditProfileIntent.SaveNameChanges ->
                validateAndSaveName()

            is EditProfileIntent.SavePasswordChanges ->
                validateAndSavePassword()
        }
    }


    private fun validateAndSaveName() {
        val currentName = _state.value.displayName

        val displayNameError = if (currentName.isBlank()) {
            FieldError(
                isError = true,
                message = UiText.StringResource(R.string.display_name_empty),
                shakeTrigger = _state.value.displayNameError.shakeTrigger + 1
            )
        } else FieldError()

        _state.update { it.copy(displayNameError = displayNameError) }

        if (!displayNameError.isError) {
            executeNameChange(currentName)
        }
    }

    private fun executeNameChange(displayName: String) {
        viewModelScope.launch {
            _state.update { it.copy(isSavingName = true) }

            val result = updateProfileUseCase(displayName)

            _state.update { it.copy(isSavingName = false) }

            when (result) {
                is LinguaQuestResult.Success -> {
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = UiText.StringResource(R.string.profile_updated_successfully),
                            type = SnackbarType.SUCCESS
                        )
                    )
                    _state.update { it.copy(isNameUpdateSuccess = true) }
                }
                is LinguaQuestResult.Failure -> {
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = UiText.StringResource(R.string.failed_update_profile),
                            type = SnackbarType.ERROR
                        )
                    )
                }
            }
        }
    }


    private fun validateAndSavePassword() {
        val currentState = _state.value

        val oldPasswordError = if (currentState.oldPassword.isBlank()) {
            FieldError(
                isError = true,
                message = UiText.StringResource(R.string.enter_current_password),
                shakeTrigger = currentState.oldPasswordError.shakeTrigger + 1
            )
        } else FieldError()

        val newPasswordError = if (currentState.newPassword.length < 6) {
            FieldError(
                isError = true,
                message = UiText.StringResource(R.string.new_password_min_length),
                shakeTrigger = currentState.newPasswordError.shakeTrigger + 1
            )
        } else FieldError()

        val hasError = oldPasswordError.isError || newPasswordError.isError

        _state.update {
            it.copy(
                oldPasswordError = oldPasswordError,
                newPasswordError = newPasswordError
            )
        }

        if (!hasError) {
            executePasswordChange(currentState.oldPassword, currentState.newPassword)
        }
    }

    private fun executePasswordChange(oldPass: String, newPass: String) {
        viewModelScope.launch {
            _state.update { it.copy(isSavingPassword = true) }

            val result = changePasswordUseCase(oldPass, newPass)

            _state.update { it.copy(isSavingPassword = false) }

            when (result) {
                is LinguaQuestResult.Success -> {
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = UiText.StringResource(R.string.password_updated_successfully),
                            type = SnackbarType.SUCCESS
                        )
                    )
                    _state.update {
                        it.copy(
                            isPasswordUpdateSuccess = true,
                            oldPassword = "",
                            newPassword = ""
                        )
                    }
                }
                is LinguaQuestResult.Failure -> {
                    _state.update {
                        it.copy(
                            oldPasswordError = FieldError(
                                isError = true,
                                message = UiText.StringResource(R.string.incorrect_old_password)
                            )
                        )
                    }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = UiText.StringResource(R.string.failed_update_password),
                            type = SnackbarType.ERROR
                        )
                    )
                }
            }
        }
    }


    private fun uploadPhoto(uri: Uri) {
        val previousAvatar = _state.value.avatarModel
        _state.update {
            it.copy(isLoading = true, avatarModel = uri)
        }

        viewModelScope.launch {
            snackbarController.sendEvent(
                SnackbarEvent(
                    message = UiText.StringResource(com.iti.linguaquest.R.string.uploading_photo_msg),
                    type = SnackbarType.INFO
                )
            )

            when (val result = uploadAvatarUseCase(uri)) {
                is LinguaQuestResult.Success -> {
                    preloadImageUseCase(result.data)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            avatarModel = result.data
                        )
                    }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            title = UiText.StringResource(com.iti.linguaquest.R.string.congrates),
                            message = UiText.StringResource(com.iti.linguaquest.R.string.profile_photo_updated_successfully),
                            type = SnackbarType.SUCCESS
                        )
                    )
                }
                is LinguaQuestResult.Failure -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            avatarModel = previousAvatar
                        )
                    }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = result.error.toUiText(),
                            type = SnackbarType.ERROR,
                            actionLabel = UiText.StringResource(com.iti.linguaquest.R.string.retry),
                            onAction = { uploadPhoto(uri) }
                        )
                    )
                }
            }
        }
    }
}