package com.iti.linguaquest.features.profile.presentation.editprofile.view.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.sharedComponents.AppTextField
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.profile.presentation.editprofile.utils.FieldError
import com.iti.linguaquest.features.profile.presentation.editprofile.utils.shakeOnError

@Composable
fun ProfileInputCard(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = "new name",
    singleLine: Boolean = true,
    minLines: Int = 1,
    trailingIcon: (@Composable () -> Unit)? = null,
    fieldError: FieldError = FieldError(),
    helperText: String? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = LinguaQuestTheme.colors.fieldCardBackground,
        tonalElevation = 1.dp,
        shadowElevation = 3.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = LinguaQuestTheme.colors.iconsColor
            )

            Spacer(modifier = Modifier.height(10.dp))

             AppTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = placeholder ?: "",
                singleLine = singleLine,
                minLines = minLines,
                trailingIcon = trailingIcon,
                isError = fieldError.isError,
                modifier = Modifier
                    .fillMaxWidth()
                    .shakeOnError(isError = fieldError.isError, shakeTrigger = fieldError.shakeTrigger)
            )

            HelperOrErrorText(fieldError = fieldError, helperText = helperText)
        }
    }
}


@Composable
fun ChangePasswordCard(
    label: String,
    oldPassword: String,
    onOldPasswordChange: (String) -> Unit,
    newPassword: String,
    onNewPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    oldPasswordPlaceholder: String? = null,
    newPasswordPlaceholder: String? = null,
    oldPasswordError: FieldError = FieldError(),
    newPasswordError: FieldError = FieldError(),
    newPasswordHelperText: String? = null
) {
    var isOldPasswordVisible by remember { mutableStateOf(false) }
    var isNewPasswordVisible by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = LinguaQuestTheme.colors.fieldCardBackground,
        tonalElevation = 1.dp,
        shadowElevation = 3.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = LinguaQuestTheme.colors.iconsColor
            )

            Spacer(modifier = Modifier.height(10.dp))

            AppTextField(
                value = oldPassword,
                onValueChange = onOldPasswordChange,
                placeholder = oldPasswordPlaceholder ?: "",
                visualTransformation = if (isOldPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = { PasswordFieldLockIcon() },
                trailingIcon = {
                    PasswordVisibilityToggleIcon(
                        isVisible = isOldPasswordVisible,
                        onToggle = { isOldPasswordVisible = !isOldPasswordVisible }
                    )
                },
                isError = oldPasswordError.isError,
                modifier = Modifier
                    .fillMaxWidth()
                    .shakeOnError(isError = oldPasswordError.isError, shakeTrigger = oldPasswordError.shakeTrigger)
            )
            HelperOrErrorText(fieldError = oldPasswordError)

            Spacer(modifier = Modifier.height(14.dp))

            AppTextField(
                value = newPassword,
                onValueChange = onNewPasswordChange,
                placeholder = newPasswordPlaceholder ?: "",
                visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = { PasswordFieldLockIcon() },
                trailingIcon = {
                    PasswordVisibilityToggleIcon(
                        isVisible = isNewPasswordVisible,
                        onToggle = { isNewPasswordVisible = !isNewPasswordVisible }
                    )
                },
                isError = newPasswordError.isError,
                modifier = Modifier
                    .fillMaxWidth()
                    .shakeOnError(isError = newPasswordError.isError, shakeTrigger = newPasswordError.shakeTrigger)
            )
            HelperOrErrorText(fieldError = newPasswordError, helperText = newPasswordHelperText)
        }
    }
}


@Composable
private fun HelperOrErrorText(
    fieldError: FieldError,
    helperText: String? = null
) {
    when {
        fieldError.isError && fieldError.message != null -> {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = fieldError.message.asString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        helperText != null -> {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = helperText,
                style = MaterialTheme.typography.labelSmall,
                color = LinguaQuestTheme.colors.iconsColor
            )
        }
    }
}