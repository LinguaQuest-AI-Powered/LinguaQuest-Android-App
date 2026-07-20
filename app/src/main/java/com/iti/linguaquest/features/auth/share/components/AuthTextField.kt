package com.iti.linguaquest.features.auth.share.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.tooling.preview.Preview
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: Painter? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: androidx.compose.ui.text.input.ImeAction = androidx.compose.ui.text.input.ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: Painter? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    borderWidth: Dp = 2.dp,
    errorMessage: String? = null
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            keyboardActions = keyboardActions,
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth(),
            enabled = enabled,
            isError = isError,
            placeholder = { Text(placeholder, color = LinguaQuestTheme.colors.textFieldPlaceholder) },
            singleLine = true,
            shape = RoundedCornerShape(50),
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        painter = it,
                        contentDescription = null,
                        tint = LinguaQuestTheme.colors.iconsColor,
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
                                contentDescription = stringResource(id = if (isPasswordVisible) R.string.hide_password else R.string.show_password),
                                tint = LinguaQuestTheme.colors.iconsColor
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
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            },
            visualTransformation = if (isPassword && !isPasswordVisible)
                PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType,
                imeAction = imeAction
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                disabledContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = LinguaQuestTheme.colors.socialButtonBorder,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AuthTextFieldPreview() {
    LinguaQuestTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .padding(16.dp)
                    .height(10.dp)
            )
            AuthTextField(
                value = "",
                onValueChange = {},
                placeholder = "Email address",
                leadingIcon = painterResource(R.drawable.email)
            )


            AuthTextField(
                value = "",
                onValueChange = {},
                placeholder = "Password",
                isPassword = true,
                leadingIcon = painterResource(R.drawable.lock),
            )

            AuthTextField(
                value = "",
                onValueChange = {},
                placeholder = "Password",
                isPassword = true,
                leadingIcon = painterResource(R.drawable.lock),
            )

        }
    }
}