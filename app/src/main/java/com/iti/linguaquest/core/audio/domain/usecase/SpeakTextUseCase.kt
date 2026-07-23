package com.iti.linguaquest.core.audio.domain.usecase

import com.iti.linguaquest.core.audio.TextToSpeechController
import javax.inject.Inject

class SpeakTextUseCase @Inject constructor(
    private val textToSpeechController: TextToSpeechController
) {
    operator fun invoke(text: String) {
        textToSpeechController.speak(text)
    }
}
