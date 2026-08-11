package com.iti.linguaquest.features.lockscreen.domain.model

import com.iti.linguaquest.core.database.lockscreen.LockScreenWordStatus

data class LockScreenWord(
    val id: Int,
    val word: String,
    val translation: String,
    val exampleSentence: String,
    val difficulty: String,
    val meaning: String,
    val status: LockScreenWordStatus,
    val createdAt: Long,
    val postedAt: Long?,
    val openedAt: Long?,
    val nativeLanguage: String,
    val targetLanguage: String,
    val proficiencyLevel: String
)
