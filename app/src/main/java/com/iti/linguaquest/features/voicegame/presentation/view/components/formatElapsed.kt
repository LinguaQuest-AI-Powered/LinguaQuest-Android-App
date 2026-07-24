package com.iti.linguaquest.features.voicegame.presentation.view.components

fun formatElapsed(totalSeconds: Int): String {
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    return "%d:%02d".format(m, s)
}