package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import com.iti.linguaquest.features.lockscreen.notification.VocabularyNotificationManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.seconds
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class ShowTestNotificationUseCase @Inject constructor(
    private val repository: LockScreenRepository,
    private val notificationManager: VocabularyNotificationManager
) {
    suspend operator fun invoke(): Boolean {
        val storedWord = repository.observePendingOnce()
            ?: repository.postedOrOpenedWordsReplaySafe()
            ?: return false

        delay(5.seconds)

        val shown = notificationManager.show(storedWord)
        if (shown) {
            repository.markPosted(storedWord.id)
        }
        return shown
    }

    private suspend fun LockScreenRepository.postedOrOpenedWordsReplaySafe(): LockScreenWord? {
        return postedOrOpenedWords.firstOrNullSafe()
    }

    private suspend fun kotlinx.coroutines.flow.Flow<List<LockScreenWord>>.firstOrNullSafe(): LockScreenWord? {
        return withTimeoutOrNull(500.milliseconds) {
            firstOrNull()?.firstOrNull()
        }
    }
}
