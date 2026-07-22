package com.iti.linguaquest.features.lockscreen.worker

interface VocabularyWorkScheduler {
    fun scheduleNotificationWork()
    fun enqueueGenerationWork()
    fun cancelAll()
}
