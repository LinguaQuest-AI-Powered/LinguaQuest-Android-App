package com.iti.linguaquest.features.voicegame.presentation.contract

import com.iti.linguaquest.features.voicegame.presentation.model.VoiceResultUi

sealed interface VoiceGameEffect {
    data object RequestMicPermission : VoiceGameEffect
    data class NavigateToResult(val result: VoiceResultUi) : VoiceGameEffect
}