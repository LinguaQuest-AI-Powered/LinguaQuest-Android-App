package com.iti.linguaquest.core.audio.domain.usecase

import android.content.Context
import com.iti.linguaquest.core.audio.AudioRecorderController
import com.iti.linguaquest.core.audio.writePcmAsWavFile
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class RecordAudioUseCase @Inject constructor(
    private val audioRecorderController: AudioRecorderController,
    @ApplicationContext private val context: Context
) {
    fun start() = audioRecorderController.start()
    fun pause() = audioRecorderController.pause()
    fun resume() = audioRecorderController.resume()
    fun stopAndGetPcmData(): ByteArray = audioRecorderController.stopAndGetPcmData()
    fun savePcmAsWav(pcmData: ByteArray): File {
        return writePcmAsWavFile(context, pcmData, AudioRecorderController.SAMPLE_RATE)
    }
    fun discard() = audioRecorderController.discard()
}
