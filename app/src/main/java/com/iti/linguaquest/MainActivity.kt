package com.iti.linguaquest

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.AudioManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O_MR1
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
import android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.iti.linguaquest.core.navigation.AppNavigation
import com.iti.linguaquest.core.sound.AppSoundPlayer
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.LocaleUtils
import com.iti.linguaquest.features.lockscreen.notification.VocabularyNotificationManager
import com.iti.linguaquest.features.setting.system.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

     @Inject
    lateinit var soundPlayer: AppSoundPlayer

    private val viewModel: MainViewModel by viewModels()

    private var openHomeRequested by mutableStateOf(false)
    private var openLockScreenWordId by mutableStateOf<Int?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        volumeControlStream = AudioManager.STREAM_MUSIC
        super.onCreate(savedInstanceState)

         handleIntent(intent)

        enableEdgeToEdge()
        setContent {
            val appTheme by viewModel.appTheme.collectAsState()

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
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

     private fun handleIntent(intent: Intent?) {
        openHomeRequested = intent?.getBooleanExtra(NotificationHelper.EXTRA_OPEN_HOME, false) ?: false
        openLockScreenWordId = intent?.getIntExtra(VocabularyNotificationManager.EXTRA_LOCKSCREEN_WORD_ID, -1)
            ?.takeIf { it > 0 }

        if (openLockScreenWordId != null) {
            val notificationManager = getSystemService( NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(VocabularyNotificationManager.NOTIFICATION_ID_BASE)
            
            if ( SDK_INT >=  O_MR1) {
                setShowWhenLocked(true)
                setTurnScreenOn(true)
            } else {
                window.addFlags(
                     FLAG_SHOW_WHEN_LOCKED or
                     FLAG_TURN_SCREEN_ON
                )
            }
        } else {
            if ( SDK_INT >= O_MR1) {
                setShowWhenLocked(false)
            } else {
                window.clearFlags( FLAG_SHOW_WHEN_LOCKED)
            }
        }

    }

    override fun attachBaseContext(newBase: Context) {
        val context = if (SDK_INT < TIRAMISU) {
            LocaleUtils.wrapContext(newBase)
        } else {
            newBase
        }
        super.attachBaseContext(context)
    }
}