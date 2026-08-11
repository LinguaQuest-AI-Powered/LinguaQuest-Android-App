package com.iti.linguaquest.features.lockscreen.worker

interface VocabularyWorkScheduler {
    fun scheduleNotificationWork()
    fun scheduleImmediateNotification()
    fun scheduleScreenOffNotification()
    fun enqueueGenerationWork()
    fun cancelAll()
    fun testNotification(delaySeconds: Long = 5)
}
