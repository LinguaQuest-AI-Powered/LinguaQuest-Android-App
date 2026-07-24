package com.iti.linguaquest.core.audio


import android.media.MediaPlayer
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioPlayerController @Inject constructor() {
    private var mediaPlayer: MediaPlayer? = null

    fun play(file: File, onCompletion: () -> Unit) {
        release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(file.absolutePath)
            setOnCompletionListener { onCompletion() }
            prepare()
            start()
        }
    }

    fun stop() {
        mediaPlayer?.let { if (it.isPlaying) it.stop() }
        release()
    }

    private fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}