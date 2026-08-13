package com.iti.linguaquest.features.onBoarding.presentation.view.components

import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.net.Uri
import android.view.Surface
import android.view.TextureView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.viewinterop.AndroidView
import com.iti.linguaquest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SplashVideoView(
    modifier: Modifier = Modifier,
    shouldStop: Boolean = false
) {
    var isVideoRendering by remember { mutableStateOf(false) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    val videoAlpha by animateFloatAsState(
        targetValue = if (isVideoRendering && !shouldStop) 1f else 0f,
        animationSpec = tween(500, easing = LinearEasing),
        label = "video_alpha"
    )


    Box(modifier = modifier) {
        DisposableEffect(Unit) {
            onDispose {
                val playerToRelease = mediaPlayer
                mediaPlayer = null
                CoroutineScope(Dispatchers.IO).launch {
                    playerToRelease?.release()
                }
            }
        }

        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .graphicsLayer { alpha = videoAlpha },
            factory = { ctx ->
                TextureView(ctx).apply {
                    isOpaque = false
                    
                    surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                        override fun onSurfaceTextureAvailable(st: SurfaceTexture, width: Int, height: Int) {
                            val surface = Surface(st)
                            val player = MediaPlayer()
                            mediaPlayer = player
                            
                            player.setDataSource(ctx, Uri.parse("android.resource://${ctx.packageName}/${R.raw.splash}"))
                            player.setSurface(surface)
                            player.isLooping = true
                            player.setVolume(0f, 0f)
                            player.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
                            
                            player.setOnInfoListener { _, what, _ ->
                                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                                    isVideoRendering = true
                                    true
                                } else {
                                    false
                                }
                            }
                            
                            player.setOnPreparedListener { mp ->
                                val params = PlaybackParams()
                                params.speed = 1.5f
                                mp.playbackParams = params
                                mp.start()
                            }
                            
                            player.prepareAsync()
                        }

                        override fun onSurfaceTextureSizeChanged(st: SurfaceTexture, width: Int, height: Int) {}
                        
                        override fun onSurfaceTextureDestroyed(st: SurfaceTexture): Boolean {
                            val playerToRelease = mediaPlayer
                            mediaPlayer?.setSurface(null)
                            mediaPlayer = null
                            CoroutineScope(Dispatchers.IO).launch {
                                playerToRelease?.release()
                            }
                            return true
                        }
                        
                        override fun onSurfaceTextureUpdated(st: SurfaceTexture) {}
                    }
                }
            }
        )
    }
}
