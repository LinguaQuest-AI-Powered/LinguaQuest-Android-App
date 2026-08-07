package com.iti.linguaquest.core.sharedComponents


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
@Composable
fun Card3DWrapper(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    borderColor: Color,
    onClick: (() -> Unit)? = null,
    ledgeHeight: Dp = 4.dp,
    cornerRadius: Dp = 16.dp,
    borderWidth: Dp = 1.dp,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val pressOffset by animateDpAsState(
        targetValue = if (isPressed && onClick != null) ledgeHeight else 0.dp,
        animationSpec = tween(durationMillis = 80),
        label = "card3DPressOffset"
    )

    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier.padding(bottom = ledgeHeight)
    ) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = ledgeHeight)
                .clip(shape)
                .background(borderColor)
        )

        Surface(
            shape = shape,
            color = backgroundColor,
            border = BorderStroke(borderWidth, borderColor),
            modifier = Modifier
                .offset(y = pressOffset)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onClick
                        )
                    } else Modifier
                )
        ) {
            content()
        }
    }
}