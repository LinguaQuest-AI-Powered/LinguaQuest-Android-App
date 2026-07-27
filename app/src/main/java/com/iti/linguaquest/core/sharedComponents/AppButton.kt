package com.iti.linguaquest.core.sharedComponents


import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppTextStyles

import com.iti.linguaquest.core.theme.LinguaQuestTheme

enum class IconPosition { NONE, START, END }
enum class ButtonVariant { PRIMARY, SECONDARY, SOCIAL }

data class ButtonStyle(
    val background: Color,
    val content: Color,
    val borderColor: Color?
)

@Composable
fun ButtonVariant.toStyle(): ButtonStyle =
    when (this) {

        ButtonVariant.PRIMARY -> ButtonStyle(
            background = MaterialTheme.colorScheme.primary,
            content = MaterialTheme.colorScheme.onPrimary,
            borderColor = null
        )

        ButtonVariant.SECONDARY -> ButtonStyle(
            background = MaterialTheme.colorScheme.secondary,
            content = MaterialTheme.colorScheme.onSecondary,
            borderColor = null
        )

        ButtonVariant.SOCIAL -> ButtonStyle(
            background = Color.Transparent,
            content = MaterialTheme.colorScheme.onBackground,
            borderColor = LinguaQuestTheme.colors.socialButtonBorder
        )
    }

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.PRIMARY,
    icon: Painter? = null,
    iconPosition: IconPosition = IconPosition.NONE,
    tintIcon: Boolean = true,
    shape: Shape = RoundedCornerShape(50),
    enabled: Boolean = true,
    isLoading: Boolean = false,
    isError: Boolean = false,
    contentColorOverride: Color? = null,
    borderColorOverride: Color? = null,
    backgroundColorOverride: Color? = null
) {
    val style = variant.toStyle()

    val alpha by animateFloatAsState(if (enabled && !isLoading) 1f else 0.5f, label = "buttonAlpha")

    val actualBorderColor = if (isError) MaterialTheme.colorScheme.error else (borderColorOverride ?: style.borderColor)
    val actualContentColor = if (isError) MaterialTheme.colorScheme.error else (contentColorOverride ?: style.content)

    Surface(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = shape,
        color = backgroundColorOverride ?: style.background,
        border = actualBorderColor?.let { BorderStroke(if (variant == ButtonVariant.SOCIAL) 1.dp else 2.dp, it) },
        modifier = modifier
            .fillMaxWidth()
            .alpha(alpha)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                LingoSpinningIcon(
                    size = 24.dp
                )
            } else {
                if (icon != null && iconPosition == IconPosition.START) {
                    Icon(
                        icon,
                        null,
                        tint = if (tintIcon) actualContentColor else Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text, color = actualContentColor,
                    style = AppTextStyles.Button,
                    overflow =  TextOverflow.Ellipsis,
                    maxLines = 1
                )
                if (icon != null && iconPosition == IconPosition.END) {
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        icon,
                        null,
                        tint = if (tintIcon) actualContentColor else Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppButtonVariantsPreview() {
    LinguaQuestTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppButton(
                text = "Primary Button",
                onClick = {},
                variant = ButtonVariant.PRIMARY
            )

            AppButton(
                text = "Secondary Button",
                onClick = {},
                variant = ButtonVariant.SECONDARY
            )

            AppButton(
                text = "Social Button",
                onClick = {},
                variant = ButtonVariant.SOCIAL
            )

            AppButton(
                text = "Log In",
                onClick = {},
                variant = ButtonVariant.PRIMARY,
                iconPosition = IconPosition.END,
                icon = painterResource(R.drawable.arrow_right),
            )
            AppButton(
                text = "Sign Up",
                onClick = {},
                variant = ButtonVariant.SECONDARY,
                iconPosition = IconPosition.START,
                icon = painterResource(R.drawable.skip),
            )
        }
    }
}
