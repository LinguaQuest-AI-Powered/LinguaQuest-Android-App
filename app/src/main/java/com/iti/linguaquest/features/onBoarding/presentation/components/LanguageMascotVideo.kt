package com.iti.linguaquest.features.onBoarding.presentation.components

import android.widget.VideoView
import androidx.annotation.RawRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.core.net.toUri

@Composable
fun LanguageMascotVideo(modifier: Modifier = Modifier) {
    val isDark = LinguaQuestTheme.colors.isDark

    @RawRes val videoRes = if (isDark) R.raw.lingo_intro_dark else R.raw.lingo_intro_light
    key(videoRes) {
        var videoView: VideoView? = null

        DisposableEffect(Unit) {
            onDispose { videoView?.stopPlayback() }
        }

        AndroidView(
            modifier =  modifier
                .clip(RoundedCornerShape(20.dp)),
            factory = { ctx ->
                VideoView(ctx).apply {
                    videoView = this
                    setVideoURI("android.resource://${ctx.packageName}/$videoRes".toUri())
                    setOnPreparedListener { mediaPlayer ->
                        mediaPlayer.isLooping = true
                        mediaPlayer.setVolume(0f, 0f)
                        start()
                    }
                }
            }
        )
    }
}