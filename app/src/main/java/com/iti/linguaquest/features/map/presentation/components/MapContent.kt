package com.iti.linguaquest.features.map.presentation.components
import com.iti.linguaquest.core.theme.LinguaQuestTheme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.animation.core.*
import com.iti.linguaquest.R
import com.iti.linguaquest.features.map.presentation.contract.MapState

private val NODE_VERTICAL_SPACING = 200.dp
private val TOP_PADDING = 100.dp
private val BOTTOM_PADDING = 100.dp
private val LEFT_X = 60.dp
private val RIGHT_X = 200.dp


private fun computeNodePositions(levelCount: Int): List<Pair<Dp, Dp>> {
    if (levelCount == 0) return emptyList()
    val totalHeight = TOP_PADDING + BOTTOM_PADDING + (NODE_VERTICAL_SPACING * (levelCount - 1))
    return List(levelCount) { index ->
        val x = if (index % 2 == 0) RIGHT_X else LEFT_X
        val y = totalHeight - BOTTOM_PADDING - (NODE_VERTICAL_SPACING * index)
        x to y
    }
}


private fun computeMapHeight(levelCount: Int): Dp {
    if (levelCount == 0) return 600.dp
    return TOP_PADDING + BOTTOM_PADDING + (NODE_VERTICAL_SPACING * (levelCount - 1)) + 100.dp
}

@Composable
fun MapContent(
    state: MapState,
    onLevelClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val density = LocalDensity.current

    var viewportHeightPx by remember { mutableIntStateOf(0) }

    val nodePositions = remember(state.levels.size) {
        computeNodePositions(state.levels.size)
    }
    val mapHeight = remember(state.levels.size) {
        computeMapHeight(state.levels.size)
    }

    LaunchedEffect(state.currentLevelIndex, viewportHeightPx) {
        if (state.currentLevelIndex in nodePositions.indices && viewportHeightPx > 0) {
            val nodeTopYDp = nodePositions[state.currentLevelIndex].second
            val nodeTopYPx = with(density) { nodeTopYDp.toPx() }
            val nodeCenterYPx = nodeTopYPx + with(density) { 50.dp.toPx() }

            val scrollTarget = (nodeCenterYPx - viewportHeightPx / 2f)
                .coerceAtLeast(0f)
                .toInt()

            scrollState.animateScrollTo(scrollTarget)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged { size ->
                viewportHeightPx = size.height
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(mapHeight)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.map_bg),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                if (nodePositions.size >= 2) {
                    MapPath(nodePositions = nodePositions)
                }

                state.levels.forEachIndexed { index, level ->
                    val (x, y) = nodePositions.getOrNull(index) ?: return@forEachIndexed
                    LevelNode(
                        levelNumber = level.levelNumber,
                        status = level.status,
                        stars = level.stars,
                        offsetX = x,
                        offsetY = y,
                        isLastLevel = index == state.levels.lastIndex,
                        onClick = { onLevelClick(level.levelNumber) }
                    )
                }

                if (state.currentLevelIndex in nodePositions.indices) {
                    val (nodeX, nodeY) = nodePositions[state.currentLevelIndex]
                    val infiniteTransition = rememberInfiniteTransition(label = "mascot_halo")
                    val floatOffset by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = -12f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "float"
                    )

                    Mascot(
                        offsetX = nodeX + 60.dp,
                        offsetY = nodeY - 60.dp + floatOffset.dp
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(LinguaQuestTheme.colors.whiteColor, Color.Transparent)
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, LinguaQuestTheme.colors.whiteColor)
                    )
                )
        )
    }
}
