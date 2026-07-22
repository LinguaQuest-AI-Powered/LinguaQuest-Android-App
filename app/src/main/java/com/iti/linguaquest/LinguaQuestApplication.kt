package com.iti.linguaquest

import android.app.Application
import androidx.work.Configuration
import androidx.hilt.work.HiltWorkerFactory
import com.iti.linguaquest.core.appicon.worker.AppIconWorkScheduler
import dagger.hilt.android.HiltAndroidApp
import com.google.firebase.Firebase
import com.google.firebase.initialize
import javax.inject.Inject

@HiltAndroidApp
class LinguaQuestApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var appIconWorkScheduler: AppIconWorkScheduler

    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(context = this)
        appIconWorkScheduler.scheduleDailyRefresh()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
