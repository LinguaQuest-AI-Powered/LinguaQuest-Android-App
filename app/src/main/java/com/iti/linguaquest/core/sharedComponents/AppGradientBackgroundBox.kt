package com.iti.linguaquest.core.sharedComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.AppColors

@Composable
fun AppGradientBackgroundBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(32.dp),
    gradientColors: List<Color> = listOf(
        AppColors.DialogGradientTopRight,
        Color.White,
        AppColors.DialogGradientBottomLeft
    ),
    borderWidth: Dp = 2.dp,
    outlineColor: Color = AppColors.DialogOutline,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset(Float.POSITIVE_INFINITY, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                ),
                shape = shape
            )
            .border(borderWidth, outlineColor, shape),
        content = content
    )
}
