package com.iti.linguaquest

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.iti.linguaquest.core.appicon.worker.AppIconWorkScheduler
import com.iti.linguaquest.features.lockscreen.notification.VocabularyScreenOffReceiver
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class LinguaQuestApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var appIconWorkScheduler: AppIconWorkScheduler

    private val screenOffReceiver = VocabularyScreenOffReceiver()

    override fun onCreate() {
        super.onCreate()
        instance = this
        Firebase.initialize(context = this)
        appIconWorkScheduler.scheduleDailyRefresh()

        ContextCompat.registerReceiver(
            this,
            screenOffReceiver,
            IntentFilter(Intent.ACTION_SCREEN_OFF),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    companion object {
        lateinit var instance: LinguaQuestApplication
            private set
    }
}