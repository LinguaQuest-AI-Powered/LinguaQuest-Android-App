package com.iti.linguaquest.features.map.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun MapPath(nodePositions: List<Pair<Dp, Dp>>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (nodePositions.size < 2) return@Canvas

        val extensionPx = 200.dp.toPx()
        val controlOffsetPx = 100.dp.toPx()

        val points = nodePositions.map { (x, y) ->
            Offset(
                x = (x + 50.dp).toPx(),
                y = (y + 50.dp).toPx()
            )
        }

        val path = Path().apply {
            moveTo(points.first().x, points.first().y + extensionPx)
            lineTo(points.first().x, points.first().y)

            for (i in 0 until points.size - 1) {
                val p1 = points[i]
                val p2 = points[i + 1]
                val cp1 = Offset(p1.x, p1.y - controlOffsetPx)
                val cp2 = Offset(p2.x, p2.y + controlOffsetPx)
                cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, p2.x, p2.y)
            }

            lineTo(points.last().x, points.last().y - extensionPx)
        }

        drawPath(
            path = path,
            color = Color(0xFF6E4322),
            style = Stroke(width = 36.dp.toPx(), cap = StrokeCap.Round)
        )
        drawPath(
            path = path,
            color = Color(0xFFA06F43),
            style = Stroke(width = 28.dp.toPx(), cap = StrokeCap.Round)
        )
        drawPath(
            path = path,
            color = Color(0xFFD6AB80).copy(alpha = 0.6f),
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 30f), 0f)
            )
        )
    }
}