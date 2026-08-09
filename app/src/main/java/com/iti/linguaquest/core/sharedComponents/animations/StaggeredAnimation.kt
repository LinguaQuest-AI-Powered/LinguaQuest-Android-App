package com.iti.linguaquest.core.sharedComponents.animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Stable
class StaggeredAnimationState(initialVisibleState: List<Boolean>) {
    private val visibilityStates = initialVisibleState.map { mutableStateOf(it) }
    
    fun isVisible(index: Int): Boolean {
        return visibilityStates.getOrNull(index)?.value ?: false
    }
    
    internal fun setVisible(index: Int, visible: Boolean) {
        visibilityStates.getOrNull(index)?.value = visible
    }

    internal fun getStates(): List<Boolean> = visibilityStates.map { it.value }

    companion object {
        fun Saver() = listSaver<StaggeredAnimationState, Boolean>(
            save = { state -> state.getStates() },
            restore = { restoredList -> StaggeredAnimationState(restoredList) }
        )
    }
}

@Composable
fun rememberStaggeredAnimationState(
    count: Int,
    initialDelayMs: Long = 80L,
    staggerDelayMs: Long = 120L
): StaggeredAnimationState {
    val state = rememberSaveable(saver = StaggeredAnimationState.Saver()) {
        StaggeredAnimationState(List(count) { false })
    }

    LaunchedEffect(count, initialDelayMs, staggerDelayMs) {
        if (state.getStates().all { it }) return@LaunchedEffect

        delay(initialDelayMs)
        for (i in 0 until count) {
            state.setVisible(i, true)
            if (i < count - 1) {
                delay(staggerDelayMs)
            }
        }
    }

    return state
}

@Composable
fun StaggeredAnimatedItem(
    index: Int,
    state: StaggeredAnimationState,
    enter: EnterTransition,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = state.isVisible(index),
        enter = enter,
        modifier = modifier
    ) {
        content()
    }
}
