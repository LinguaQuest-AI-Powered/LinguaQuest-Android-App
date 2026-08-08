package com.iti.linguaquest.features.map.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

data class Snowflake(
    var x: Float,
    var y: Float,
    val radius: Float,
    val speed: Float,
    val drift: Float
)

@Composable
fun SnowEffect(modifier: Modifier = Modifier) {
    val snowflakes = remember {
        List(15) {
            Snowflake(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 1.5f + 0.5f,
                speed = Random.nextFloat() * 0.3f + 0.15f,
                drift = (Random.nextFloat() - 0.5f) * 0.2f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "snow")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(100000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        snowflakes.forEach { flake ->
            val currentY = (flake.y * height + time * flake.speed * 50f) % (height + 50f) - 25f
            val currentX = (flake.x * width + time * flake.drift * 20f) % (width + 50f) - 25f
            val finalX = if (currentX < -25f) width + 25f + currentX else currentX

            // Outer glow
            drawCircle(
                color = Color.White.copy(alpha = 0.2f),
                radius = flake.radius.dp.toPx() * 3f,
                center = Offset(finalX, currentY)
            )
            
            // Inner bright core
            drawCircle(
                color = Color.White.copy(alpha = 0.8f),
                radius = flake.radius.dp.toPx(),
                center = Offset(finalX, currentY)
            )
        }
    }
}
