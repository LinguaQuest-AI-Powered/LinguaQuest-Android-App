package com.iti.linguaquest

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.initialize
import com.iti.linguaquest.core.appicon.domain.AppIconService
import com.iti.linguaquest.core.appicon.worker.AppIconWorkScheduler
import com.iti.linguaquest.core.appicon.util.AppForegroundTracker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class LinguaQuestApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var appIconWorkScheduler: AppIconWorkScheduler

    @Inject
    lateinit var appForegroundTracker: AppForegroundTracker

    @Inject
    lateinit var appIconService: AppIconService

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(context = this)

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityStarted(activity: android.app.Activity) {
                appForegroundTracker.onActivityStarted()
                appIconWorkScheduler.cancelBackgroundExitCheck()
            }

            override fun onActivityStopped(activity: android.app.Activity) {
                appForegroundTracker.onActivityStopped()
                if (!appForegroundTracker.isInForeground) {
                    appIconWorkScheduler.scheduleBackgroundExitCheck()
                    applicationScope.launch {
                        appIconService.onAppBackgrounded()
                    }
                }
            }

            override fun onActivityCreated(activity: android.app.Activity, savedInstanceState: android.os.Bundle?) {}
            override fun onActivityResumed(activity: android.app.Activity) {}
            override fun onActivityPaused(activity: android.app.Activity) {}
            override fun onActivitySaveInstanceState(activity: android.app.Activity, outState: android.os.Bundle) {}
            override fun onActivityDestroyed(activity: android.app.Activity) {}
        })
        Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )
        appIconWorkScheduler.scheduleDailyRefresh()

        if (BuildConfig.DEBUG) {
            timber.log.Timber.plant(timber.log.Timber.DebugTree())
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
