package com.iti.linguaquest.features.lockscreen.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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
class VocabularyScreenOffReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: LockScreenRepository

    @Inject
    lateinit var scheduler: VocabularyWorkScheduler

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_SCREEN_OFF) return

        val pendingResult = goAsync()
        scope.launch {
            try {
                if (repository.featureEnabled.first()) {
                    scheduler.scheduleImmediateNotification()
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
