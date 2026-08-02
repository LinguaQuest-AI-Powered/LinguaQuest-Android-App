package com.iti.linguaquest.core.appicon.worker

interface AppIconWorkScheduler {

     fun scheduleDailyRefresh()

     fun scheduleAngryWindowCheck()

      fun scheduleFollowUpCheck(delayMillis: Long)

     fun scheduleBackgroundExitCheck()

     fun cancelBackgroundExitCheck()
}
