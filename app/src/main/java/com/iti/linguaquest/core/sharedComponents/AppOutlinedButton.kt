package com.iti.linguaquest.core.sharedComponents


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.AppColors
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AppOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = AppColors.DialogSecondaryButtonOutline,
    enabled: Boolean = true,
    ledgeHeight: Dp = 6.dp,
    cornerRadius: Dp = 16.dp,
    buttonHeight: Dp = 52.dp
) {
    val actualColor = if (enabled) color else Color.Gray.copy(alpha = 0.5f)

    AppButton3D(
        text = text,
        onClick = onClick,
        modifier = modifier,
        variant = ButtonVariant.SOCIAL,
        enabled = enabled,
        backgroundColorOverride = MaterialTheme.colorScheme.surface,
        contentColorOverride = actualColor,
        borderColorOverride = actualColor,
        ledgeColorOverride = actualColor.copy(alpha = 0.35f),
        ledgeHeight = ledgeHeight,
        cornerRadius = cornerRadius,
        buttonHeight = buttonHeight
    )
}


@Preview(name = "AppOutlinedButton 3D - Light", showBackground = true)
@Preview(name = "AppOutlinedButton 3D - Dark", showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun AppOutlinedButtonPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            AppOutlinedButton(
                text = "Outlined 3D Button",
                onClick = {}
            )

            AppOutlinedButton(
                text = "Disabled State",
                enabled = false,
                onClick = {}
            )
        }
    }
}