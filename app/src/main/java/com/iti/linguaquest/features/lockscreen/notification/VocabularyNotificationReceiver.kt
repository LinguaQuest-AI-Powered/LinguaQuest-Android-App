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
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VocabularyNotificationReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_VOCAB_REMINDER = "com.iti.linguaquest.ACTION_VOCAB_REMINDER"
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
          val keyguardManager =
                    context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                if (!keyguardManager.isKeyguardLocked) {
                    android.util.Log.d(
                        "VocabReceiver",
                        "Device is unlocked — skipping this cycle, rescheduling."
                    )
                    scheduler.scheduleNotificationWork()
                    return@launch
                }

                val word = repository.observePendingOnce()
                
                if (word == null) {
                    Log.d("VocabReceiver", "No pending words found, enqueueing generation.")
                    scheduler.enqueueGenerationWork()
                } else {
                     Log.d("VocabReceiver", "Found word to show: ${word.word}")
                    val shown = notificationManager.show(word)
                    
                    if (shown) {
                        repository.markPosted(word.id)
                        Log.d("VocabReceiver", "Notification shown successfully for: ${word.word}")
                    } else {
                       Log.e("VocabReceiver", "Failed to show notification. Check permissions.")
                    }

                    if (repository.pendingCountOnce() < MIN_PENDING_WORDS) {
                         Log.d("VocabReceiver", "Pending count low, enqueueing generation.")
                        scheduler.enqueueGenerationWork()
                    }
                }

                 scheduler.scheduleNotificationWork()
                
            } catch (e: Exception) {
                android.util.Log.e("VocabReceiver", "Error in onReceive", e)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
