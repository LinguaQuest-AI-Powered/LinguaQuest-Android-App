package com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

private data class FallingCoin(
    val xFraction: Float,
    val delayMillis: Int,
    val durationMillis: Int,
    val sizeDp: Int,
    val startRotation: Float
)

@Composable
fun CoinRainOverlay(modifier: Modifier = Modifier, coinCount: Int = 14, startYPx: Float = 0f) {
    var widthPx by remember { mutableFloatStateOf(0f) }
    var heightPx by remember { mutableFloatStateOf(0f) }
    val coins = remember {
        List(coinCount) {
            FallingCoin(
                xFraction = Random.nextFloat(),
                delayMillis = Random.nextInt(0, 1800),
                durationMillis = Random.nextInt(2600, 3800),
                sizeDp = Random.nextInt(14, 26),
                startRotation = Random.nextFloat() * 360f
            )
        }
    }

    Box(
        modifier = modifier.onGloballyPositioned {
            widthPx = it.size.width.toFloat()
            heightPx = it.size.height.toFloat()
        }
    ) {
        if (heightPx > 0f && widthPx > 0f) {
            coins.forEach { coin -> FallingCoinItem(coin, widthPx, heightPx, startYPx) }
        }
    }
}

@Composable
private fun FallingCoinItem(
    coin: FallingCoin,
    containerWidthPx: Float,
    containerHeightPx: Float,
    startYPx: Float
) {

    var started by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(coin.delayMillis.toLong().milliseconds)
        started = true
    }

    val progress by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(durationMillis = coin.durationMillis, easing = LinearEasing),
        label = "coinFall"
    )

    val yOffsetPx = startYPx + progress * (containerHeightPx - startYPx)
    val xOffsetPx = coin.xFraction * containerWidthPx

    val alpha = when {
        progress < 0.03f -> progress / 0.03f
        progress > 0.8f -> (1f - progress) / 0.2f
        else -> 1f
    }

    val rotation = coin.startRotation + progress * 300f

    Box(
        modifier = Modifier
            .offset { IntOffset(x = xOffsetPx.toInt(), y = yOffsetPx.toInt()) }
            .alpha(alpha.coerceIn(0f, 1f))
            .rotate(rotation)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_coin),
            contentDescription = null,
            modifier = Modifier.size(coin.sizeDp.dp)
        )
    }
}