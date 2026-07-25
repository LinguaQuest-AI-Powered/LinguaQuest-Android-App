package com.iti.linguaquest.features.profile.presentation.editprofile.view.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
    fieldError: FieldError = FieldError()
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(LinguaQuestTheme.colors.fieldCardBackground)
            .padding(16.dp)
    ) {
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

        if (fieldError.isError && fieldError.message != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = fieldError.message.asString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
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
    newPasswordError: FieldError = FieldError()
) {
    var isOldPasswordVisible by remember { mutableStateOf(false) }
    var isNewPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(LinguaQuestTheme.colors.fieldCardBackground)
            .padding(16.dp)
    ) {
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
            trailingIcon = {
                PasswordVisibilityToggleIcon(
                    isVisible = isOldPasswordVisible,
                    onToggle = { isOldPasswordVisible = !isOldPasswordVisible }
                )
            },
            fieldError = oldPasswordError
        )
        if (oldPasswordError.isError && oldPasswordError.message != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = oldPasswordError.message.asString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        ProfileTextFieldRow(
            value = newPassword,
            onValueChange = onNewPasswordChange,
            placeholder = newPasswordPlaceholder,
            visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                PasswordVisibilityToggleIcon(
                    isVisible = isNewPasswordVisible,
                    onToggle = { isNewPasswordVisible = !isNewPasswordVisible }
                )
            },
            fieldError = newPasswordError
        )
        if (newPasswordError.isError && newPasswordError.message != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = newPasswordError.message.asString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
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
    trailingIcon: (@Composable () -> Unit)? = null,
    fieldError: FieldError = FieldError()
) {
    val borderColor = if (fieldError.isError) {
        MaterialTheme.colorScheme.error
    } else {
        LinguaQuestTheme.colors.textFieldBorder
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shakeOnError(isError = fieldError.isError, shakeTrigger = fieldError.shakeTrigger)
            .clip(RoundedCornerShape(14.dp))
            .background(LinguaQuestTheme.colors.textFieldFill)
            .border(
                BorderStroke(1.dp, borderColor),
                RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Bottom
    ) {
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
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (trailingIcon != null) {
            Spacer(modifier = Modifier.width(8.dp))
            trailingIcon()
        }
    }
}