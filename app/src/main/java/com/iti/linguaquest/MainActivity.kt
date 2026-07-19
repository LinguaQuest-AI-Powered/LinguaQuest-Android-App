package com.iti.linguaquest

import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.iti.linguaquest.core.navigation.AppNavigation
import com.iti.linguaquest.core.sound.AppSoundPlayer
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var soundPlayer: AppSoundPlayer

    override fun onCreate(savedInstanceState: Bundle?) {

        volumeControlStream = AudioManager.STREAM_MUSIC

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalSoundPlayer provides soundPlayer) {
                LinguaQuestTheme {
                    AppNavigation()
                }
            }
        }
    }
}
