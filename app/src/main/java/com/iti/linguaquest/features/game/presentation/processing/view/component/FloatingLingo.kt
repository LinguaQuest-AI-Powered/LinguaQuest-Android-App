package com.iti.linguaquest.features.game.presentation.processing.view.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun FloatingLingo(
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val lingoSizeDp = 150.dp
    val lingoSizePx = with(density) { lingoSizeDp.toPx() }

    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    val maxX = (screenWidthPx / 2f) - (lingoSizePx / 2f) - 40f
    val minX = -maxX
    val maxY = (screenHeightPx / 3f)
    val minY = -maxY + 100f

    val posX = remember { Animatable(0f) }
    val posY = remember { Animatable(0f) }
    val scaleX = remember { Animatable(1f) }

    val images = listOf(
        R.drawable.lingo_checking_pronounciation,
        R.drawable.lingo_camera,
        R.drawable.lingo_searching
    )
    var currentImageIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1500)
            currentImageIndex = (currentImageIndex + 1) % images.size
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "Popping")
    val popY by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(250, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PopY"
    )

    LaunchedEffect(Unit) {
        while (true) {
            val nextX = Random.nextFloat() * (maxX - minX) + minX
            val nextY = Random.nextFloat() * (maxY - minY) + minY

            val targetScaleX = if (nextX > posX.value) 1f else -1f

            val jobX = launch { posX.animateTo(nextX, tween(500, easing = FastOutSlowInEasing)) }
            val jobY = launch { posY.animateTo(nextY, tween(500, easing = FastOutSlowInEasing)) }
            launch { scaleX.animateTo(targetScaleX, tween(150)) }

            joinAll(jobX, jobY)
            delay(1500)
        }
    }

    Image(
        painter = painterResource(id = images[currentImageIndex]),
        contentDescription = stringResource(id = R.string.game_processing_catchable_lingo_desc),
        modifier = modifier
            .offset {
                IntOffset(
                    x = posX.value.roundToInt(),
                    y = (posY.value + popY).roundToInt()
                )
            }
            .size(lingoSizeDp)
            .graphicsLayer {
                this.scaleX = scaleX.value
            }
    )
}