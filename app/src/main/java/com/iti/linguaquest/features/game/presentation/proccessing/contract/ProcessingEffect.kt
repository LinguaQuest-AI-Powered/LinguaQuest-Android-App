package com.iti.linguaquest.features.game.presentation.proccessing.contract

sealed interface ProcessingEffect {
    object NavigateToSuccess : ProcessingEffect
    object NavigateToFailure : ProcessingEffect
}