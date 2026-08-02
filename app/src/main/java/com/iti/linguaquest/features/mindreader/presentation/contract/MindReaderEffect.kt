package com.iti.linguaquest.features.mindreader.presentation.contract

import com.iti.linguaquest.core.sharedComponents.text.UiText

sealed interface MindReaderEffect {
    data class ShowToast(val message: UiText) : MindReaderEffect
    data object NavigateBack : MindReaderEffect
    data class PlayAudio(val text: String, val languageCode: String) : MindReaderEffect
}
