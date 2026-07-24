package com.iti.linguaquest.core.audio.domain.usecase

import com.iti.linguaquest.core.audio.AudioPlayerController
import java.io.File
import javax.inject.Inject

class PlayAudioPreviewUseCase @Inject constructor(
    private val audioPlayerController: AudioPlayerController
) {
    fun play(file: File, onCompletion: () -> Unit = {}) {
        audioPlayerController.play(file, onCompletion)
    }

    fun stop() {
        audioPlayerController.stop()
    }
}
