package com.iti.linguaquest.core.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRecorderController @Inject constructor() {

    companion object {
        const val SAMPLE_RATE = 16000
        private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
        private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
    }

    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null
    private val recordingScope = CoroutineScope(Dispatchers.IO)

    private val isRecording = AtomicBoolean(false)
    private val isPaused = AtomicBoolean(false)
    private val outputStream = ByteArrayOutputStream()

    @SuppressLint("MissingPermission")
    fun start() {
        outputStream.reset()
        val minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT,
            minBufferSize * 2
        )
        audioRecord?.startRecording()
        isRecording.set(true)
        isPaused.set(false)

        recordingJob = recordingScope.launch {
            val buffer = ByteArray(minBufferSize)
            while (isActive && isRecording.get()) {
                val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                if (read > 0 && !isPaused.get()) {
                    synchronized(outputStream) {
                        outputStream.write(buffer, 0, read)
                    }
                }
            }
        }
    }

    fun pause() = isPaused.set(true)
    fun resume() = isPaused.set(false)

    fun stopAndGetPcmData(): ByteArray {
        isRecording.set(false)
        try {
            audioRecord?.stop()
        } catch (e: Exception) {
        }
        recordingJob?.cancel()
        recordingJob = null
        try {
            audioRecord?.release()
        } catch (e: Exception) {
        }
        audioRecord = null
        return synchronized(outputStream) { outputStream.toByteArray() }
    }

    fun discard() {
        isRecording.set(false)
        try {
            audioRecord?.stop()
        } catch (e: Exception) {
        }
        recordingJob?.cancel()
        recordingJob = null
        try {
            audioRecord?.release()
        } catch (e: Exception) {
        }
        audioRecord = null
        synchronized(outputStream) { outputStream.reset() }
    }
}