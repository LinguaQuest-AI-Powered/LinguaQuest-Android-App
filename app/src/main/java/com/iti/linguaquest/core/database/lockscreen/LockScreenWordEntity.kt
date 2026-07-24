package com.iti.linguaquest.core.database.lockscreen

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "lock_screen_words")
data class LockScreenWordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val word: String,
    val translation: String,
    val exampleSentence: String,
    val status: String = LockScreenWordStatus.PENDING.name,
    val createdAt: Long = System.currentTimeMillis(),
    val postedAt: Long? = null,
    val openedAt: Long? = null,
    val nativeLanguage: String,
    val targetLanguage: String,
    val proficiencyLevel: String
)
