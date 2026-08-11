package com.iti.linguaquest.features.lockscreen.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VocabularyGotItReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_GOT_IT = "com.iti.linguaquest.ACTION_GOT_IT"
        const val EXTRA_WORD_ID = "extra_word_id"
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
    }

    @Inject
    lateinit var repository: LockScreenRepository

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
                repository.markOpened(wordId)
             } catch (e: Exception) {
             } finally {
                val nm =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.cancel(notificationId)
                 pendingResult.finish()
            }
        }
    }
}
