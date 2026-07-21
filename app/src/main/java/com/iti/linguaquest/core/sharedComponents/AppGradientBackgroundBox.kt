package com.iti.linguaquest.core.sharedComponents

import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppGradientBackgroundBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(32.dp),
    gradientColors: List<Color> = listOf(
        LinguaQuestTheme.colors.DialogGradientTopRight,
        LinguaQuestTheme.colors.whiteColor,
        LinguaQuestTheme.colors.DialogGradientBottomLeft
    ),
    borderWidth: Dp = 2.dp,
    outlineColor: Color = LinguaQuestTheme.colors.textFieldBorder,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = LinguaQuestTheme.colors.isDark
    val solidWhite = LinguaQuestTheme.colors.whiteColor
    Box(
        modifier = modifier
            .drawBehind {
                val brush = if (isDark) {
                    SolidColor(solidWhite)
                } else {
                    Brush.linearGradient(
                        colors = gradientColors,
                        start = Offset(size.width, 0f),
                        end = Offset(0f, size.height)
                    )
                }
                drawRoundRect(
                    brush = brush,
                    cornerRadius = CornerRadius(32.dp.toPx(), 32.dp.toPx())
                )
            }
            .border(borderWidth, outlineColor, shape),
        content = content
    )
}
