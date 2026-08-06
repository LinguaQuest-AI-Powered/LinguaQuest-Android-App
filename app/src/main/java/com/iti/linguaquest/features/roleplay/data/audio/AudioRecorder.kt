package com.iti.linguaquest.features.roleplay.data.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRecorder @Inject constructor() {

    private var audioRecord: AudioRecord? = null

    @Volatile
    private var isActive = false

    @Volatile
    private var isSending = false

    private val preBuffer = ArrayDeque<ByteArray>()
    private val preBufferLock = Any()

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
            MediaRecorder.AudioSource.VOICE_RECOGNITION,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            minBufferSize * 2
        )

        if (record.state != AudioRecord.STATE_INITIALIZED) {
            Timber.e("[AudioRecorder] AudioRecord failed to initialize")
            record.release()
            return@flow
        }

        audioRecord = record
        val buffer = ByteArray(minBufferSize)

        record.startRecording()
        isActive = true
        var emitCount = 0


        try {
            while (isActive) {
                val read = record.read(buffer, 0, buffer.size)
                when {
                    read > 0 -> {
                        val chunk = buffer.copyOf(read)
                        if (isSending) {
                            val bufferedChunks = synchronized(preBufferLock) {
                                val list = preBuffer.toList()
                                preBuffer.clear()
                                list
                            }
                            for (preChunk in bufferedChunks) {
                                emit(preChunk)
                                emitCount++
                            }

                            emit(chunk)
                            emitCount++
                        } else {
                            synchronized(preBufferLock) {
                                if (preBuffer.size >= 5) {
                                    preBuffer.removeFirstOrNull()
                                }
                                preBuffer.addLast(chunk)
                            }
                        }
                    }

                    read == 0 -> if (!isActive) break
                    else -> break
                }
            }
        } finally {
            try {
                record.stop()
            } catch (e: IllegalStateException) {
                Timber.w(e, "Error stopping audio recorder")
            }
            record.release()
            audioRecord = null
            synchronized(preBufferLock) {
                preBuffer.clear()
            }
        }
    }.flowOn(Dispatchers.IO)

    fun resumeSending() {

        isSending = true
    }

    fun pauseSending() {

        isSending = false
        synchronized(preBufferLock) {
            preBuffer.clear()
        }
    }

    fun stopRecording() {

        isActive = false
        isSending = false
        synchronized(preBufferLock) {
            preBuffer.clear()
        }
        try {
            audioRecord?.stop()
        } catch (e: IllegalStateException) {
            Timber.w(e, "Error stopping audio recorder")
        }
    }
}

