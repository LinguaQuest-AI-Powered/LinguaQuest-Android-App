package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.core.database.lockscreen.LockScreenWordStatus
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import com.iti.linguaquest.features.lockscreen.notification.VocabularyNotificationManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class ShowTestNotificationUseCase @Inject constructor(
    private val repository: LockScreenRepository,
    private val notificationManager: VocabularyNotificationManager
) {
    suspend operator fun invoke(): Boolean {
        val wordToShow = repository.observePendingOnce()
            ?: repository.postedOrOpenedWordsReplaySafe()
            ?: buildFallbackWord()

        val shown = notificationManager.show(wordToShow)
        if (shown && wordToShow.id != FALLBACK_WORD_ID) {
            repository.markPosted(wordToShow.id)
        }
        return shown
    }

    private fun buildFallbackWord() = LockScreenWord(
        id = FALLBACK_WORD_ID,
        word = "Serendipity",
        translation = "صدفة سعيدة",
        exampleSentence = "She found the cafe by serendipity.",
        difficulty = "Easy",
        meaning = "A happy discovery made by chance.",
        status = LockScreenWordStatus.PENDING,
        createdAt = System.currentTimeMillis(),
        postedAt = null,
        openedAt = null,
        nativeLanguage = "Arabic",
        targetLanguage = "English",
        proficiencyLevel = "Beginner"
    )

    private suspend fun LockScreenRepository.postedOrOpenedWordsReplaySafe(): LockScreenWord? {
        return withTimeoutOrNull(500.milliseconds) {
            postedOrOpenedWords.firstOrNull()?.firstOrNull()
        }
    }

    companion object {
        private const val FALLBACK_WORD_ID = 9999
    }
}
