package com.iti.linguaquest.core.appicon.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.iti.linguaquest.core.appicon.domain.AppIconService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppIconRefreshReceiver : BroadcastReceiver() {

    @Inject
    lateinit var appIconService: AppIconService

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        scope.launch {
            try {
                appIconService.refresh()
            } finally {
                pendingResult.finish()
            }
        }
    }
}
