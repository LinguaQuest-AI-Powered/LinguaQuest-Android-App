package com.iti.linguaquest.features.home.presentation.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.animations.LingoEntranceAnimations
import com.iti.linguaquest.core.sharedComponents.animations.StaggeredAnimatedItem
import com.iti.linguaquest.core.sharedComponents.animations.rememberStaggeredAnimationState
import com.iti.linguaquest.core.tutorial.presentation.tutorialTarget

@Composable
fun HomeFabs(
    onDailyMissionClick: (Rect?) -> Unit,
    onWorldMapClick: (Rect?) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState? = null
) {
    val fabBounds = remember { arrayOf<Rect?>(null) }
    val animationState = rememberStaggeredAnimationState(count = 2)
    var isVisible by remember { mutableStateOf(true) }

    if (scrollState != null) {
        var previousScrollOffset by remember { mutableIntStateOf(0) }
        LaunchedEffect(scrollState) {
            snapshotFlow { scrollState.value }
                .collect { currentScrollOffset ->
                    val delta = currentScrollOffset - previousScrollOffset
                    if (delta > 0 && currentScrollOffset > 50) {
                        isVisible = false
                    } else if (delta < 0) {
                        isVisible = true
                    }
                    previousScrollOffset = currentScrollOffset
                }
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(initialScale = 0.8f) + fadeIn(),
        exit = scaleOut(targetScale = 0.8f, animationSpec = tween(durationMillis = 150)) + fadeOut(animationSpec = tween(durationMillis = 150)),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        StaggeredAnimatedItem(
            index = 0,
            state = animationState,
            enter = LingoEntranceAnimations.popUpVertically(offset = 60)
        ) {
            FloatingActionButton(
                onClick = { onDailyMissionClick(fabBounds[0]) },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.tutorialTarget("tutorial_daily_mission")
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_daily_mission),
                    contentDescription = "daily_mission_content_description",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        StaggeredAnimatedItem(
            index = 1,
            state = animationState,
            enter = LingoEntranceAnimations.popUpVertically(offset = 60)
        ) {
            FloatingActionButton(
                onClick = { onWorldMapClick(fabBounds[0]) },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        fabBounds[0] = coordinates.boundsInRoot()
                    }
                    .tutorialTarget("tutorial_language_button")
            ) {
                Image(
                    painter = painterResource(R.drawable.world_home_icon),
                    contentDescription = "world_map_content_description",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        }
    }
}
