package com.iti.linguaquest.core.audio

import android.content.Context
import java.io.File
import java.io.FileOutputStream

fun writePcmAsWavFile(context: Context, pcmData: ByteArray, sampleRate: Int): File {
    val file = File(context.cacheDir, "voice_preview_${System.currentTimeMillis()}.wav")
    FileOutputStream(file).use { out ->
        out.write(buildWavHeader(pcmData.size, sampleRate))
        out.write(pcmData)
    }
    return file
}

private fun buildWavHeader(pcmDataSize: Int, sampleRate: Int): ByteArray {
    val byteRate = sampleRate * 2
    val header = ByteArray(44)
    "RIFF".toByteArray().copyInto(header, 0)
    writeInt(header, 4, pcmDataSize + 36)
    "WAVE".toByteArray().copyInto(header, 8)
    "fmt ".toByteArray().copyInto(header, 12)
    writeInt(header, 16, 16)
    header[20] = 1; header[22] = 1
    writeInt(header, 24, sampleRate)
    writeInt(header, 28, byteRate)
    header[32] = 2; header[34] = 16
    "data".toByteArray().copyInto(header, 36)
    writeInt(header, 40, pcmDataSize)
    return header
}

private fun writeInt(header: ByteArray, offset: Int, value: Int) {
    header[offset] = (value and 0xff).toByte()
    header[offset + 1] = ((value shr 8) and 0xff).toByte()
    header[offset + 2] = ((value shr 16) and 0xff).toByte()
    header[offset + 3] = ((value shr 24) and 0xff).toByte()
}