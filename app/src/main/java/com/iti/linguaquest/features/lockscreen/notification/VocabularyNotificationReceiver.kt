package com.iti.linguaquest.features.lockscreen.notification

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.iti.linguaquest.core.database.lockscreen.LockScreenWordStatus
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import com.iti.linguaquest.features.lockscreen.worker.VocabularyWorkScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class VocabularyNotificationReceiver : BroadcastReceiver() {

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
                Timber.d("VocabularyNotificationReceiver: onReceive ACTION_VOCAB_REMINDER")
                val isFeatureEnabled = repository.featureEnabled.first()
                if (!isFeatureEnabled) {
                    Timber.d("VocabularyNotificationReceiver: Feature is disabled, ignoring.")
                    return@launch
                }
                val forceShow = intent.getBooleanExtra(EXTRA_FORCE_SHOW, false)

                val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                val isLocked = keyguardManager.isKeyguardLocked

                if (!forceShow && !isLocked) {
                    scheduler.scheduleScreenOffNotification()
                    return@launch
                }

                val word = repository.observePendingOnce()
                    ?: repository.postedOrOpenedWords.firstOrNull()?.firstOrNull()

                val wordToShow = word ?: if (forceShow) buildFallbackTestWord() else null

                if (wordToShow == null) {
                    Timber.d("VocabularyNotificationReceiver: No words available to show, enqueuing generation...")
                    scheduler.enqueueGenerationWork()
                } else {
                    Timber.d("VocabularyNotificationReceiver: Attempting to show notification for word ID: ${wordToShow.id}")
                    val shown = notificationManager.show(wordToShow)

                    if (shown && word != null) {
                        repository.markPosted(word.id)
                    }
                    val pendingCount = repository.pendingCountOnce()
                    
                    if (!forceShow) {
                        Timber.d("VocabularyNotificationReceiver: Scheduling next cycle in 15 mins.")
                        scheduler.scheduleScreenOffNotification()
                    }

                    if (pendingCount < MIN_PENDING_WORDS) {
                        Timber.d("VocabularyNotificationReceiver: Pending words ($pendingCount) < $MIN_PENDING_WORDS, enqueuing generation...")
                        scheduler.enqueueGenerationWork()
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "LockScreen notification failed")
            } finally {
                pendingResult.finish()
            }
        }
    }

    private fun buildFallbackTestWord(): LockScreenWord {
        return LockScreenWord(
            id = 9999,
            word = "Serendipity",
            translation = "happy chance",
            exampleSentence = "She found the cafe by serendipity.",
            difficulty = "Easy",
            meaning = "A happy discovery made by chance.",
            status = LockScreenWordStatus.PENDING,
            createdAt = System.currentTimeMillis(),
            postedAt = null,
            openedAt = null,
            nativeLanguage = "Arabic",
            targetLanguage = "English",
            proficiencyLevel = "Beginner"
        )
    }

    companion object {
        const val ACTION_VOCAB_REMINDER = "com.iti.linguaquest.ACTION_VOCAB_REMINDER"
        const val EXTRA_FORCE_SHOW = "extra_force_show"
        const val MIN_PENDING_WORDS = 3
    }
}
