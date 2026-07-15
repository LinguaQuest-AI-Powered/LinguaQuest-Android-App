package com.iti.linguaquest.features.auth.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.AppTextStyles

@Composable
fun OtpInputField(
    otpCode: String,
    onOtpChanged: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = otpCode,
        onValueChange = { newText ->
            if (newText.length <= 4) {
                onOtpChanged(newText)

                if (newText.length == 4) {
                    focusManager.clearFocus()
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onKeyboardDone()
            }
        ),
        modifier = modifier.fillMaxWidth(),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(4) { index ->
                    val char = when {
                        index >= otpCode.length -> ""
                        else -> otpCode[index].toString()
                    }
                    val isFocused = index == otpCode.length

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .border(
                                width = if (isFocused) 2.dp else 1.dp,
                                color = if (isFocused) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline,
                                shape = CircleShape
                            )
                            .background(MaterialTheme.colorScheme.surface, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            style = AppTextStyles.Word,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}