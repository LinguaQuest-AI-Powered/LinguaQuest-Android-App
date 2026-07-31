package com.iti.linguaquest.features.lockscreen.notification

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import com.iti.linguaquest.features.lockscreen.worker.VocabularyWorkScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VocabularyNotificationReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_VOCAB_REMINDER = "com.iti.linguaquest.ACTION_VOCAB_REMINDER"
        const val EXTRA_FORCE_SHOW = "extra_force_show"
        const val MIN_PENDING_WORDS = 5
    }

    @Inject
    lateinit var notificationManager: VocabularyNotificationManager

    @Inject
    lateinit var repository: LockScreenRepository

    @Inject
    lateinit var scheduler: VocabularyWorkScheduler

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_VOCAB_REMINDER) return

        val pendingResult = goAsync()
        scope.launch {
            try {
                val isFeatureEnabled = repository.featureEnabled.first()
                if (!isFeatureEnabled) return@launch
                val forceShow = intent.getBooleanExtra(EXTRA_FORCE_SHOW, false)
                val keyguardManager =
                    context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                if (!forceShow && !keyguardManager.isKeyguardLocked) {
                    scheduler.scheduleNotificationWork()
                    return@launch
                }

                val word = repository.observePendingOnce()
                    ?: repository.postedOrOpenedWords.firstOrNull()?.firstOrNull()

                if (word == null) {
                    scheduler.enqueueGenerationWork()
                } else {
                    val shown = notificationManager.show(word)

                    if (shown) {
                        repository.markPosted(word.id)
                    }
                    if (repository.pendingCountOnce() < MIN_PENDING_WORDS) {
                        scheduler.enqueueGenerationWork()
                    }
                }

                scheduler.scheduleNotificationWork()
            } catch (e: Exception) {
             } finally {
                pendingResult.finish()
            }
        }
    }
}
