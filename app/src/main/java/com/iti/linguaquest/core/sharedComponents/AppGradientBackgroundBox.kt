package com.iti.linguaquest.core.sharedComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    Box(
        modifier = modifier
            .background(
                brush = if (isSystemInDarkTheme()) {
                    SolidColor(LinguaQuestTheme.colors.whiteColor)
                } else {
                    Brush.linearGradient(
                        colors = gradientColors,
                        start = Offset(Float.POSITIVE_INFINITY, 0f),
                        end = Offset(0f, Float.POSITIVE_INFINITY)
                    )
                },
                shape = shape
            )
            .border(borderWidth, outlineColor, shape),
        content = content
    )
}
