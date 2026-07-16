package com.iti.linguaquest.features.game.presentation.success.contract

sealed interface GameSuccessEffect {
    object NavigateToNextLevel : GameSuccessEffect
    object NavigateToExit : GameSuccessEffect
}