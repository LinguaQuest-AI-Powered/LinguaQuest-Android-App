package com.iti.linguaquest.core.sharedComponents.animations

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.ui.unit.IntOffset

object LingoEntranceAnimations {


    fun popUpVertically(offset: Int = 60, duration: Int = 800): EnterTransition {
        val slideSpec = tween<IntOffset>(durationMillis = duration, easing = FastOutSlowInEasing)
        val floatSpec = tween<Float>(durationMillis = duration, easing = FastOutSlowInEasing)

        return slideInVertically(
            initialOffsetY = { offset },
            animationSpec = slideSpec
        ) + fadeIn(floatSpec) + scaleIn(initialScale = 0.92f, animationSpec = floatSpec)
    }

    fun popUpHorizontally(offset: Int = 200, duration: Int = 800): EnterTransition {
        val slideSpec = tween<IntOffset>(durationMillis = duration, easing = FastOutSlowInEasing)
        val floatSpec = tween<Float>(durationMillis = duration, easing = FastOutSlowInEasing)

        return slideInHorizontally(
            initialOffsetX = { offset },
            animationSpec = slideSpec
        ) + fadeIn(floatSpec) + scaleIn(initialScale = 0.92f, animationSpec = floatSpec)
    }
}
