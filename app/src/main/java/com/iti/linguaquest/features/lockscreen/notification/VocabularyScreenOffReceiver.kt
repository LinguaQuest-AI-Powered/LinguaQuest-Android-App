package com.iti.linguaquest.features.lockscreen.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import com.iti.linguaquest.features.lockscreen.worker.VocabularyWorkScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class VocabularyScreenOffReceiver : BroadcastReceiver() {

    @Inject
    lateinit var scheduler: VocabularyWorkScheduler

    @Inject
    lateinit var repository: LockScreenRepository

    private val scope = CoroutineScope(Dispatchers.Default)

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Intent.ACTION_SCREEN_OFF) return
        
        val pendingResult = goAsync()
        scope.launch {
            try {
                val isEnabled = repository.featureEnabled.first()
                if (!isEnabled) {
                    Timber.d("VocabularyScreenOffReceiver: Feature disabled, ignoring.")
                    return@launch
                }

                Timber.d("VocabularyScreenOffReceiver: Screen turned off. Starting 15-minute cycle.")
                scheduler.scheduleScreenOffNotification()
            } catch (e: Exception) {
                Timber.e(e, "Error processing screen off event")
            } finally {
                pendingResult.finish()
            }
        }
    }
}
