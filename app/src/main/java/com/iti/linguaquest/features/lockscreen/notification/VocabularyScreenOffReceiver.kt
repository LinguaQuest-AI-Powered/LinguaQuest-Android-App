package com.iti.linguaquest.features.lockscreen.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import com.iti.linguaquest.features.lockscreen.worker.VocabularyWorkScheduler
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

class VocabularyScreenOffReceiver : BroadcastReceiver() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ScreenOffEntryPoint {
        fun lockScreenRepository(): LockScreenRepository
        fun vocabularyWorkScheduler(): VocabularyWorkScheduler
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_SCREEN_OFF) return

        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            ScreenOffEntryPoint::class.java
        )
        val repository = entryPoint.lockScreenRepository()
        val scheduler = entryPoint.vocabularyWorkScheduler()

        val pendingResult = goAsync()
        scope.launch {
            try {
                val isEnabled = repository.featureEnabled.first()
                Timber.d("VocabularyScreenOffReceiver: onReceive SCREEN_OFF. featureEnabled=$isEnabled")
                if (isEnabled) {
                    Timber.d("VocabularyScreenOffReceiver: Scheduling immediate notification")
                    scheduler.scheduleImmediateNotification()
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
