package com.iti.linguaquest.features.lockscreen.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class VocabularyGotItReceiver : BroadcastReceiver() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface VocabularyReceiverEntryPoint {
        fun lockScreenRepository(): LockScreenRepository
    }

    companion object {
        const val ACTION_GOT_IT = "com.iti.linguaquest.ACTION_GOT_IT"
        const val EXTRA_WORD_ID = "extra_word_id"
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_GOT_IT) return

        val wordId = intent.getIntExtra(EXTRA_WORD_ID, -1).takeIf { it > 0 } ?: return
        val notificationId = intent.getIntExtra(
            EXTRA_NOTIFICATION_ID,
            VocabularyNotificationManager.NOTIFICATION_ID_BASE
        )

        val pendingResult = goAsync()
        scope.launch {
            try {
                val appContext = context.applicationContext
                val entryPoint = EntryPointAccessors.fromApplication(
                    appContext,
                    VocabularyReceiverEntryPoint::class.java
                )
                val repository = entryPoint.lockScreenRepository()
                repository.markOpened(wordId)
             } catch (e: Exception) {
                Timber.e(e, "Failed to mark word as opened")
             } finally {
                val nm =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.cancel(notificationId)
                 pendingResult.finish()
            }
        }
    }
}
