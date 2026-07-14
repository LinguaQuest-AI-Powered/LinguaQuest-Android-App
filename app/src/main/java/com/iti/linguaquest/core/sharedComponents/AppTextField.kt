package com.iti.linguaquest.core.sharedComponents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.ui.theme.AppColors


@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: Painter? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: Painter? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    borderWidth: Dp = 2.dp
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth()
            .border(
                width = borderWidth,
                color = if (isError) Color.Red else AppColors.TextFieldBorderColor,
                shape = RoundedCornerShape(50)
            ),
        enabled = enabled,
        isError = isError,
        placeholder = { Text(placeholder, color = AppColors.TextFieldPlaceholderColor) },
        singleLine = true,
        shape = RoundedCornerShape(50),
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    painter = it,
                    contentDescription = null,
                    tint = AppColors.TextFieldPlaceholderColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        trailingIcon = {
            when {
                isPassword -> {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            painter = painterResource(
                                if (isPasswordVisible) R.drawable.eye else R.drawable.eyelock
                            ),
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                            tint = AppColors.TextFieldPlaceholderColor
                        )
                    }
                }
                trailingIcon != null -> {
                    IconButton(
                        onClick = { onTrailingIconClick?.invoke() },
                        enabled = onTrailingIconClick != null
                    ) {
                        Icon(
                            painter = trailingIcon,
                            contentDescription = null,
                            tint = AppColors.TextFieldPlaceholderColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        },
        visualTransformation = if (isPassword && !isPasswordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else keyboardType
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = AppColors.SocialButtonFillColor,
            unfocusedContainerColor = AppColors.SocialButtonFillColor,
            disabledContainerColor = AppColors.SocialButtonFillColor,
            focusedBorderColor = AppColors.TextFieldBorderColor,
            unfocusedBorderColor = AppColors.TextFieldBorderColor,
            focusedTextColor = AppColors.TextFieldPlaceholderColor,
            unfocusedTextColor = AppColors.TextFieldPlaceholderColor,
            cursorColor = AppColors.TextOnSocialButton
        )
    )
}