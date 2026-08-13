package com.iti.linguaquest.core.tutorial.model

data class TargetBounds(
    val left: Float,
    val top: Float,
    val width: Float,
    val height: Float
) {
    val bottom: Float get() = top + height
    val right: Float get() = left + width
}
