package com.iti.linguaquest.features.roleplay.data.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.audiofx.AcousticEchoCanceler
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRecorder @Inject constructor() {

    private var audioRecord: AudioRecord? = null
    @Volatile
    private var isRecording = false

    @SuppressLint("MissingPermission")
    fun startRecording(): Flow<ByteArray> = flow {
        val sampleRate = 16000
        val minBufferSize = AudioRecord.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        if (minBufferSize <= 0) return@flow

        val record = AudioRecord(
            MediaRecorder.AudioSource.VOICE_COMMUNICATION,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            minBufferSize
        )

        if (record.state != AudioRecord.STATE_INITIALIZED) {
            record.release()
            return@flow
        }

        audioRecord = record
        val buffer = ByteArray(minBufferSize)

        if (AcousticEchoCanceler.isAvailable()) {
            val echoCanceler = AcousticEchoCanceler.create(record.audioSessionId)
            if (echoCanceler != null) {
                echoCanceler.enabled = true
                Log.d("AudioSetup", "Acoustic Echo Canceler enabled successfully.")
            } else {
                Log.w("AudioSetup", "Failed to create Acoustic Echo Canceler.")
            }
        } else {
            Log.w("AudioSetup", "Acoustic Echo Canceler is not available on this device.")
        }

        record.startRecording()
        isRecording = true

        try {
            while (isRecording) {
                val read = record.read(buffer, 0, buffer.size)
                when {
                    read > 0 -> emit(buffer.copyOf(read))
                    read == 0 -> if (!isRecording) break
                    else -> break
                }
            }
        } finally {
            try {
                record.stop()
            } catch (e: IllegalStateException) {
                // Ignore exception if already stopped or uninitialized
            }
            record.release()
            audioRecord = null
        }
    }.flowOn(Dispatchers.IO)

    fun stopRecording() {
        isRecording = false
        try {
            audioRecord?.stop()
        } catch (e: IllegalStateException) {
            // Ignore
        }
    }
}
