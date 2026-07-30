package com.iti.linguaquest.features.profile.presentation.editprofile.view.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.AppColors
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

            ProfileTextFieldRow(
                value = value,
                onValueChange = onValueChange,
                placeholder = placeholder,
                singleLine = singleLine,
                minLines = minLines,
                trailingIcon = trailingIcon,
                fieldError = fieldError
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

            ProfileTextFieldRow(
                value = oldPassword,
                onValueChange = onOldPasswordChange,
                placeholder = oldPasswordPlaceholder,
                visualTransformation = if (isOldPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = { PasswordFieldLockIcon() },
                trailingIcon = {
                    PasswordVisibilityToggleIcon(
                        isVisible = isOldPasswordVisible,
                        onToggle = { isOldPasswordVisible = !isOldPasswordVisible }
                    )
                },
                fieldError = oldPasswordError
            )
            HelperOrErrorText(fieldError = oldPasswordError)

            Spacer(modifier = Modifier.height(14.dp))

            ProfileTextFieldRow(
                value = newPassword,
                onValueChange = onNewPasswordChange,
                placeholder = newPasswordPlaceholder,
                visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = { PasswordFieldLockIcon() },
                trailingIcon = {
                    PasswordVisibilityToggleIcon(
                        isVisible = isNewPasswordVisible,
                        onToggle = { isNewPasswordVisible = !isNewPasswordVisible }
                    )
                },
                fieldError = newPasswordError
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

@Composable
private fun ProfileTextFieldRow(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    fieldError: FieldError = FieldError()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = when {
        fieldError.isError -> MaterialTheme.colorScheme.error
        isFocused -> AppColors.OrangeActive
        else -> LinguaQuestTheme.colors.textFieldBorder
    }
    val borderWidth = if (isFocused && !fieldError.isError) 1.5.dp else 1.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shakeOnError(isError = fieldError.isError, shakeTrigger = fieldError.shakeTrigger)
            .clip(RoundedCornerShape(14.dp))
            .background(LinguaQuestTheme.colors.textFieldFill)
            .border(
                BorderStroke(borderWidth, borderColor),
                RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Bottom
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(10.dp))
        }

        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty() && placeholder != null) {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.titleMedium,
                    color = LinguaQuestTheme.colors.textFieldPlaceholder
                )
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = singleLine,
                minLines = minLines,
                visualTransformation = visualTransformation,
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    color = if (fieldError.isError) MaterialTheme.colorScheme.error else LinguaQuestTheme.colors.BrownText
                ),
                interactionSource = interactionSource,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (trailingIcon != null) {
            Spacer(modifier = Modifier.width(8.dp))
            trailingIcon()
        }
    }
}