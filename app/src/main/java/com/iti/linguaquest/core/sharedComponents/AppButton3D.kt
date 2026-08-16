package com.iti.linguaquest.core.sharedComponents

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.sharedComponents.LingoSpinningIcon
import com.iti.linguaquest.core.sound.LocalSoundPlayer
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
            background = MaterialTheme.colorScheme.surface,
            content = MaterialTheme.colorScheme.tertiary,
            borderColor = MaterialTheme.colorScheme.tertiary
        )

        ButtonVariant.SOCIAL -> ButtonStyle(
            background = LinguaQuestTheme.colors.textFieldFill,
            content = MaterialTheme.colorScheme.onBackground,
            borderColor = LinguaQuestTheme.colors.socialButtonBorder
        )
    }

enum class ButtonVisualState {
    DEFAULT,
    LOADING,
    SUCCESS
}

@Composable
fun AppButton3D(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.PRIMARY,
    icon: Painter? = null,
    iconPosition: IconPosition = IconPosition.NONE,
    tintIcon: Boolean = true,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    isError: Boolean = false,
    isSuccess: Boolean = false,
    successText: String = text,
    backgroundColor: Color? = null,
    ledgeColor: Color? = null,
    textColor: Color? = null,
    contentColorOverride: Color? = null,
    borderColorOverride: Color? = null,
    backgroundColorOverride: Color? = null,
    ledgeColorOverride: Color? = null,
    ledgeHeight: Dp = 6.dp,
    cornerRadius: Dp = 16.dp,
    buttonHeight: Dp = 52.dp
) {
    val style = variant.toStyle()

    val resolvedBackgroundColor = backgroundColorOverride ?: backgroundColor ?: style.background
    val resolvedContentColor = if (isError) {
        MaterialTheme.colorScheme.error
    } else if (!enabled) {
        (contentColorOverride ?: textColor ?: style.content).copy(alpha = 0.5f)
    } else {
        (contentColorOverride ?: textColor ?: style.content)
    }
    val resolvedBorderColor = if (isError) {
        MaterialTheme.colorScheme.error
    } else if (!enabled) {
        (borderColorOverride ?: style.borderColor)?.copy(alpha = 0.5f)
    } else {
        (borderColorOverride ?: style.borderColor)
    }

    val resolvedLedgeColor = ledgeColorOverride ?: ledgeColor ?: when (variant) {
        ButtonVariant.PRIMARY -> if (enabled) LinguaQuestTheme.colors.ShadowOrange else LinguaQuestTheme.colors.ShadowOrange.copy(alpha = 0.5f)
        ButtonVariant.SECONDARY -> (resolvedBorderColor ?: MaterialTheme.colorScheme.tertiary).copy(alpha = if (enabled) 0.35f else 0.18f)
        ButtonVariant.SOCIAL -> (resolvedBorderColor ?: LinguaQuestTheme.colors.socialButtonBorder).copy(alpha = 0.4f)
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val pressOffset by animateDpAsState(
        targetValue = if (isPressed && enabled && !isLoading) ledgeHeight else 0.dp,
        animationSpec = tween(durationMillis = 80),
        label = "buttonPressOffset"
    )
    

    val visualState = when {
        isLoading -> ButtonVisualState.LOADING
        isSuccess -> ButtonVisualState.SUCCESS
        else -> ButtonVisualState.DEFAULT
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(buttonHeight + ledgeHeight)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(buttonHeight)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(cornerRadius))
                .background(resolvedLedgeColor)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(buttonHeight)
                .graphicsLayer { translationY = pressOffset.toPx() }
                .clip(RoundedCornerShape(cornerRadius))
                .background(if (enabled) resolvedBackgroundColor else resolvedBackgroundColor.copy(alpha = 0.6f))
                .then(
                    if (resolvedBorderColor != null) {
                        Modifier.border(
                            width = if (variant == ButtonVariant.SOCIAL) 1.5.dp else 2.dp,
                            color = resolvedBorderColor,
                            shape = RoundedCornerShape(cornerRadius)
                        )
                    } else Modifier
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled && !isLoading,
                    onClick = {
                        onClick()
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = visualState,
                label = "buttonContentAnimation",
                modifier = Modifier.fillMaxWidth()
            ) { state ->
                when (state) {
                    ButtonVisualState.LOADING -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            LingoSpinningIcon(
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    ButtonVisualState.SUCCESS -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = resolvedContentColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = successText,
                                style = MaterialTheme.typography.labelLarge,
                                color = resolvedContentColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    ButtonVisualState.DEFAULT -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (icon != null && iconPosition == IconPosition.START) {
                                Icon(
                                    painter = icon,
                                    contentDescription = null,
                                    tint = if (tintIcon) resolvedContentColor else Color.Unspecified,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                text = text,
                                color = resolvedContentColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                            if (icon != null && iconPosition == IconPosition.END) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    painter = icon,
                                    contentDescription = null,
                                    tint = if (tintIcon) resolvedContentColor else Color.Unspecified,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}