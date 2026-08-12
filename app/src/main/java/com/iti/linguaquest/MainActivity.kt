package com.iti.linguaquest

import android.app.NotificationManager
import android.content.Context
import android.content.Intent

import android.content.res.Configuration
import android.media.AudioManager
import android.os.Build
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
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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

import com.iti.linguaquest.features.lockscreen.worker.VocabularyWorkScheduler
import com.iti.linguaquest.features.setting.system.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var soundPlayer: AppSoundPlayer

    @Inject
    lateinit var vocabularyWorkScheduler: VocabularyWorkScheduler


    private val viewModel: AppViewModel by viewModels()

    private var openHomeRequested by mutableStateOf(false)
    private var openDailyMissionRequested by mutableStateOf(false)
    private var openLockScreenWordId by mutableStateOf<Int?>(null)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        volumeControlStream = AudioManager.STREAM_MUSIC
        installSplashScreen()
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
                        openDailyMissionRequested = openDailyMissionRequested,
                        openLockScreenWordId = openLockScreenWordId,
                        onOpenHomeHandled = { openHomeRequested = false },
                        onOpenDailyMissionHandled = { openDailyMissionRequested = false },
                        onOpenLockScreenWordHandled = { openLockScreenWordId = null }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

     private fun handleIntent(intent: Intent?) {
        openHomeRequested = intent?.getBooleanExtra(NotificationHelper.EXTRA_OPEN_HOME, false) ?: false
        openDailyMissionRequested = intent?.getStringExtra("type") == "DAILY_MISSION_AVAILABLE"
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

         viewModel.refreshAppIcon()
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