package com.iti.linguaquest.core.audio.domain.usecase

import com.iti.linguaquest.core.audio.AudioRecorderController
import javax.inject.Inject

class RecordAudioUseCase @Inject constructor(
    private val audioRecorderController: AudioRecorderController
) {
    fun start() = audioRecorderController.start()
    fun pause() = audioRecorderController.pause()
    fun resume() = audioRecorderController.resume()
    fun stopAndGetPcmData(): ByteArray = audioRecorderController.stopAndGetPcmData()
    fun discard() = audioRecorderController.discard()
}
