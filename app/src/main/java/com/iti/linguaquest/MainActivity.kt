package com.iti.linguaquest

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.iti.linguaquest.core.appicon.domain.AppIconService
import com.iti.linguaquest.core.navigation.AppNavigation
import com.iti.linguaquest.core.preferences.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.core.sound.AppSoundPlayer
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.lockscreen.notification.VocabularyNotificationManager
import com.iti.linguaquest.features.setting.system.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var soundPlayer: AppSoundPlayer

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    @Inject
    lateinit var appIconService: AppIconService

    private var openHomeRequested by mutableStateOf(false)
    private var openLockScreenWordId by mutableStateOf<Int?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {

        volumeControlStream = AudioManager.STREAM_MUSIC

        super.onCreate(savedInstanceState)
        openHomeRequested = intent?.getBooleanExtra(NotificationHelper.EXTRA_OPEN_HOME, false) == true
        openLockScreenWordId = intent?.getIntExtra(VocabularyNotificationManager.EXTRA_LOCKSCREEN_WORD_ID, -1)
            ?.takeIf { it > 0 }
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
                    AppNavigation(
                        openHomeRequested = openHomeRequested,
                        openLockScreenWordId = openLockScreenWordId,
                        onOpenHomeHandled = { openHomeRequested = false },
                        onOpenLockScreenWordHandled = { openLockScreenWordId = null }
                    )
                }
            }
        }

        lifecycleScope.launch {
            appIconService.refresh()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        openHomeRequested = intent.getBooleanExtra(NotificationHelper.EXTRA_OPEN_HOME, false)
        openLockScreenWordId = intent.getIntExtra(VocabularyNotificationManager.EXTRA_LOCKSCREEN_WORD_ID, -1)
            .takeIf { it > 0 }
        lifecycleScope.launch {
            appIconService.refresh()
        }
    }
}
