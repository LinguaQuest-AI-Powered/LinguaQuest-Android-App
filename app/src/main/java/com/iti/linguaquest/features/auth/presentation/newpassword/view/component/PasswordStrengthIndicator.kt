package com.iti.linguaquest.features.auth.presentation.newpassword.view.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.features.auth.presentation.newpassword.view.NewPasswordDimens

private const val STRONG_PASSWORD_LENGTH = 12
private const val MEDIUM_PASSWORD_LENGTH = 8

@Composable
fun PasswordStrengthIndicator(
    password: String,
    modifier: Modifier = Modifier,
) {
    val strength = computePasswordStrength(password)
    val strengthColor by animateColorAsState(
        targetValue = when (strength) {
            PasswordStrength.STRONG -> MaterialTheme.colorScheme.tertiary
            PasswordStrength.MEDIUM -> MaterialTheme.colorScheme.tertiary
            PasswordStrength.WEAK -> MaterialTheme.colorScheme.error
            PasswordStrength.EMPTY -> MaterialTheme.colorScheme.outline
        },
        label = "StrengthColor",
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = stringResource(id = R.string.new_password_strength_label),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(NewPasswordDimens.StrengthBarHeight)
                .clip(RoundedCornerShape(NewPasswordDimens.StrengthBarCornerRadius))
                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = strength.fraction)
                    .height(NewPasswordDimens.StrengthBarHeight)
                    .clip(RoundedCornerShape(NewPasswordDimens.StrengthBarCornerRadius))
                    .background(strengthColor)
            )
        }
    }
}

private enum class PasswordStrength(val fraction: Float) {
    EMPTY(0f),
    WEAK(0.33f),
    MEDIUM(0.66f),
    STRONG(1f),
}

private fun computePasswordStrength(password: String): PasswordStrength {
    if (password.isBlank()) return PasswordStrength.EMPTY
    val hasUpper = password.any { it.isUpperCase() }
    val hasDigit = password.any { it.isDigit() }
    val isLongEnough = password.length >= MEDIUM_PASSWORD_LENGTH
    val isStrong = password.length >= STRONG_PASSWORD_LENGTH && hasUpper && hasDigit
    val isMedium = isLongEnough && (hasUpper || hasDigit)
    return when {
        isStrong -> PasswordStrength.STRONG
        isMedium -> PasswordStrength.MEDIUM
        else -> PasswordStrength.WEAK
    }
}
