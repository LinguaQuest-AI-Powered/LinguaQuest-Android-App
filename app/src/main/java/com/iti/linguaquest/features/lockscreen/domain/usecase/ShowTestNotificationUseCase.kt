package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.core.database.lockscreen.LockScreenWordStatus
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import com.iti.linguaquest.features.lockscreen.notification.VocabularyNotificationManager
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class ShowTestNotificationUseCase @Inject constructor(
    private val observePendingOnceUseCase: ObserveLockScreenPendingOnceUseCase,
    private val notificationManager: VocabularyNotificationManager
) {
    suspend operator fun invoke() {
        delay(5000.milliseconds)

        val pendingWord = observePendingOnceUseCase()
        if (pendingWord != null) {
            notificationManager.show(pendingWord)
        } else {
            val testWord = LockScreenWord(
                id = 9999,
                word = "Test Word",
                translation = "كلمة اختبار",
                exampleSentence = "This is a test sentence.",
                status = LockScreenWordStatus.PENDING,
                createdAt = System.currentTimeMillis(),
                postedAt = null,
                openedAt = null,
                nativeLanguage = "Arabic",
                targetLanguage = "English",
                proficiencyLevel = "Beginner"
            )
            notificationManager.show(testWord)
        }
    }
}