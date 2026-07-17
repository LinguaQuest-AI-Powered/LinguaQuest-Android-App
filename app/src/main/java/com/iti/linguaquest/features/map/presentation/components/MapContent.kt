package com.iti.linguaquest.features.map.presentation.components

import androidx.compose.foundation.Image
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
import com.iti.linguaquest.R
import com.iti.linguaquest.features.map.presentation.contract.MapState

// Layout constants
private val NODE_VERTICAL_SPACING = 200.dp
private val TOP_PADDING = 100.dp
private val BOTTOM_PADDING = 100.dp
private val LEFT_X = 60.dp
private val RIGHT_X = 200.dp

/**
 * Dynamically compute node positions based on the number of levels.
 * Nodes zigzag left ↔ right from bottom to top.
 */
private fun computeNodePositions(levelCount: Int): List<Pair<Dp, Dp>> {
    if (levelCount == 0) return emptyList()
    val totalHeight = TOP_PADDING + BOTTOM_PADDING + (NODE_VERTICAL_SPACING * (levelCount - 1))
    return List(levelCount) { index ->
        val x = if (index % 2 == 0) RIGHT_X else LEFT_X
        val y = totalHeight - BOTTOM_PADDING - (NODE_VERTICAL_SPACING * index)
        x to y
    }
}

/**
 * Compute the total map height based on the number of levels.
 */
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

    // Capture the viewport (visible window) height — NOT the scrollable content height
    var viewportHeightPx by remember { mutableIntStateOf(0) }

    val nodePositions = remember(state.levels.size) {
        computeNodePositions(state.levels.size)
    }
    val mapHeight = remember(state.levels.size) {
        computeMapHeight(state.levels.size)
    }

    // Scroll so the current level node is vertically centered in the viewport
    LaunchedEffect(state.currentLevelIndex, viewportHeightPx) {
        if (state.currentLevelIndex in nodePositions.indices && viewportHeightPx > 0) {
            val nodeTopYDp = nodePositions[state.currentLevelIndex].second
            val nodeTopYPx = with(density) { nodeTopYDp.toPx() }
            val nodeCenterYPx = nodeTopYPx + with(density) { 50.dp.toPx() } // center of 100dp node

            // Scroll so nodeCenterY lands exactly at the viewport center
            val scrollTarget = (nodeCenterYPx - viewportHeightPx / 2f)
                .coerceAtLeast(0f)
                .toInt()

            scrollState.animateScrollTo(scrollTarget)
        }
    }

    // Outer Box measures the VIEWPORT size (the visible screen area)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged { size ->
                viewportHeightPx = size.height
            }
    ) {
        // Inner scrollable Box contains the full map content
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
                // Background image
                Image(
                    painter = painterResource(id = R.drawable.map_bg),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Road
                if (nodePositions.size >= 2) {
                    MapPath(nodePositions = nodePositions)
                }

                // Level nodes
                state.levels.forEachIndexed { index, level ->
                    val (x, y) = nodePositions.getOrNull(index) ?: return@forEachIndexed
                    LevelNode(
                        levelNumber = level.levelNumber,
                        status = level.status,
                        stars = level.stars,
                        offsetX = x,
                        offsetY = y,
                        onClick = { onLevelClick(level.levelNumber) }
                    )
                }

                // Mascot next to the current level
                if (state.currentLevelIndex in nodePositions.indices) {
                    val (nodeX, nodeY) = nodePositions[state.currentLevelIndex]
                    Mascot(
                        offsetX = nodeX + 60.dp,
                        offsetY = nodeY - 60.dp
                    )
                }
            }
        }
    }
}
