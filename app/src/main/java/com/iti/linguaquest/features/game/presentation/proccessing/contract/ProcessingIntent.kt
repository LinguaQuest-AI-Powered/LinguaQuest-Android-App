package com.iti.linguaquest.features.game.presentation.proccessing.contract

sealed interface ProcessingIntent {
    object SimulateAiSuccess : ProcessingIntent
    object SimulateAiFailure : ProcessingIntent
}