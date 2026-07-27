package com.iti.linguaquest.features.home.utils
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

fun calculatePopupOffset(
    anchor: Rect?,
    popupSize: IntSize,
    screenWidthDp: Int,
    screenHeightDp: Int,
    density: Density,
    defaultPopupWidthDp: Int = 280,
    defaultPopupHeightDp: Int = 120,
    marginDp: Int = 8,
    fallbackTopDp: Int = 16
): IntOffset {
    val popupWidthPx = if (popupSize.width > 0) {
        popupSize.width.toFloat()
    } else {
        with(density) { defaultPopupWidthDp.dp.toPx() }
    }
    val popupHeightPx = if (popupSize.height > 0) {
        popupSize.height.toFloat()
    } else {
        with(density) { defaultPopupHeightDp.dp.toPx() }
    }

    val marginPx = with(density) { marginDp.dp.toPx() }
    val fallbackTopPx = with(density) { fallbackTopDp.dp.toPx() }
    val screenWidthPx = with(density) { screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { screenHeightDp.dp.toPx() }

    val x = if (anchor != null) {
        (anchor.left + anchor.width / 2f - popupWidthPx / 2f)
            .coerceIn(
                marginPx,
                (screenWidthPx - popupWidthPx - marginPx).coerceAtLeast(marginPx)
            )
    } else {
        ((screenWidthPx - popupWidthPx) / 2f).coerceAtLeast(marginPx)
    }

    val y = if (anchor != null) {
        val aboveY = anchor.top - popupHeightPx - marginPx
        if (aboveY >= marginPx) {
            aboveY
        } else {
            (anchor.bottom + marginPx)
                .coerceAtMost(screenHeightPx - popupHeightPx - marginPx)
        }
    } else {
        fallbackTopPx
    }

    return IntOffset(x.roundToInt(), y.roundToInt())
}