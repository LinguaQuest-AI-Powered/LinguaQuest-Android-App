package com.iti.linguaquest

import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.CompositionLocalProvider
import com.iti.linguaquest.core.navigation.AppNavigation
import com.iti.linguaquest.core.sound.AppSoundPlayer
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var soundPlayer: AppSoundPlayer

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {

        volumeControlStream = AudioManager.STREAM_MUSIC

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appTheme by userPreferencesRepository.appTheme.collectAsState(initial = null)
            
            if (appTheme == null) return@setContent
            
            val isDarkTheme = when (appTheme) {
                "dark" -> true
                "light" -> false
                else -> isSystemInDarkTheme()
            }

            CompositionLocalProvider(LocalSoundPlayer provides soundPlayer) {
                LinguaQuestTheme(darkTheme = isDarkTheme) {
                    AppNavigation()
                }
            }
        }
    }
}
