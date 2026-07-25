package com.iti.linguaquest.features.profile.presentation.editprofile.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ShareTopBar
import com.iti.linguaquest.features.profile.presentation.editprofile.contract.EditProfileState
import com.iti.linguaquest.features.profile.presentation.editprofile.utils.FieldError
import com.iti.linguaquest.features.profile.presentation.editprofile.view.component.ChangePasswordCard
import com.iti.linguaquest.features.profile.presentation.editprofile.view.component.ChangePhotoButton
import com.iti.linguaquest.features.profile.presentation.editprofile.view.component.DisplayNameTrailingIcon
import com.iti.linguaquest.features.profile.presentation.editprofile.view.component.EditableAvatar
import com.iti.linguaquest.features.profile.presentation.editprofile.view.component.InfoNote
import com.iti.linguaquest.features.profile.presentation.editprofile.view.component.PrimaryActionButton
import com.iti.linguaquest.features.profile.presentation.editprofile.view.component.ProfileInputCard
import com.iti.linguaquest.features.profile.presentation.editprofile.view.component.SecondaryTextButton
import com.iti.linguaquest.features.profile.presentation.view.components.AvatarPickerBottomSheet


@Composable
fun EditProfileScreenContent(
    displayName: String,
    onDisplayNameChange: (String) -> Unit,
    oldPassword: String,
    onOldPasswordChange: (String) -> Unit,
    newPassword: String,
    onNewPasswordChange: (String) -> Unit,
    avatarModel: Any?,
    isLoading: Boolean,
    isSavingName: Boolean,
    isSavingPassword: Boolean,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onBackClick: () -> Unit,
    onSaveNameClick: () -> Unit,
    onSavePasswordClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
    displayNameError: FieldError = FieldError(),
    oldPasswordError: FieldError = FieldError(),
    newPasswordError: FieldError = FieldError()
) {
    var showAvatarSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        containerColor = LinguaQuestTheme.colors.textFieldFill
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            ShareTopBar(title = R.string.edit_profile_title, onBackClick = onBackClick)

            Spacer(modifier = Modifier.height(16.dp))

             Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.Center) {
                    EditableAvatar(
                       // state = state,
                        avatarModel = avatarModel,
                        onEditClick = { showAvatarSheet = true }
                    )

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(36.dp),
                            color = LinguaQuestTheme.colors.OrangeActive
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                ChangePhotoButton(
                    text = stringResource(R.string.change_photo),
                    onClick = { showAvatarSheet = true }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

             ProfileInputCard(
                label = stringResource(R.string.display_name_label),
                value = displayName,
                onValueChange = onDisplayNameChange,
                singleLine = true,
                trailingIcon = { DisplayNameTrailingIcon() },
                fieldError = displayNameError
            )

            Spacer(modifier = Modifier.height(12.dp))

            PrimaryActionButton(
                text = stringResource(R.string.save_changes),
                onClick = onSaveNameClick,
                isLoading = isSavingName
            )

            Spacer(modifier = Modifier.height(24.dp))

             ChangePasswordCard(
                label = stringResource(R.string.change_password_label),
                oldPassword = oldPassword,
                onOldPasswordChange = onOldPasswordChange,
                oldPasswordPlaceholder = stringResource(R.string.old_password_placeholder),
                newPassword = newPassword,
                onNewPasswordChange = onNewPasswordChange,
                newPasswordPlaceholder = stringResource(R.string.new_password_placeholder),
                oldPasswordError = oldPasswordError,
                newPasswordError = newPasswordError
            )

            Spacer(modifier = Modifier.height(12.dp))

            PrimaryActionButton(
                  text = stringResource(R.string.save_changes),
                onClick = onSavePasswordClick,
                isLoading = isSavingPassword
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoNote(text = stringResource(R.string.profile_visibility_note))

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                SecondaryTextButton(
                    text = stringResource(R.string.cancel),
                    onClick = onCancelClick
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        if (showAvatarSheet) {
            AvatarPickerBottomSheet(
                onDismiss = { showAvatarSheet = false },
                onChooseFromGalleryClick = {
                    showAvatarSheet = false
                    onGalleryClick()
                },
                onTakePhotoClick = {
                    showAvatarSheet = false
                    onCameraClick()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenContentErrorPreview() {
    LinguaQuestTheme {
        EditProfileScreenContent(
            displayName = "",
            onDisplayNameChange = {},
            oldPassword = "wrongpass",
            onOldPasswordChange = {},
            newPassword = "123",
            onNewPasswordChange = {},
            avatarModel = R.drawable.lingo_app_bar,
            isLoading = false,
            isSavingName = false,
            isSavingPassword = false,
            onGalleryClick = {},
            onCameraClick = {},
            onBackClick = {},
            onSaveNameClick = {},
            onSavePasswordClick = {},
            onCancelClick = {},
            displayNameError = FieldError(
                isError = true, message = UiText.StringResource(R.string.display_name_empty),
            ),
            oldPasswordError = FieldError(
                isError = true, message = UiText.StringResource(R.string.display_name_empty),
            ),
            newPasswordError = FieldError(
                isError = true, message = UiText.StringResource(R.string.display_name_empty),
            )
        )
    }
}