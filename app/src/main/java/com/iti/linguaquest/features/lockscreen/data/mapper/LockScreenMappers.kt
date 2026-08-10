package com.iti.linguaquest.features.lockscreen.data.mapper

import com.iti.linguaquest.core.database.lockscreen.LockScreenWordEntity
import com.iti.linguaquest.features.lockscreen.domain.model.GeneratedVocabularyWord
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import com.iti.linguaquest.core.database.lockscreen.LockScreenWordStatus

fun LockScreenWordEntity.toDomain(): LockScreenWord {
    return LockScreenWord(
        id = id,
        word = word,
        translation = translation,
        exampleSentence = exampleSentence,
        difficulty = difficulty,
        meaning = meaning.ifBlank { translation },
        status = runCatching { LockScreenWordStatus.valueOf(status) }
            .getOrDefault(LockScreenWordStatus.PENDING),
        createdAt = createdAt,
        postedAt = postedAt,
        openedAt = openedAt,
        nativeLanguage = nativeLanguage,
        targetLanguage = targetLanguage,
        proficiencyLevel = proficiencyLevel
    )
}

fun GeneratedVocabularyWord.toEntity(
    nativeLanguage: String,
    targetLanguage: String,
    proficiencyLevel: String,
    userId: Int
): LockScreenWordEntity {
    return LockScreenWordEntity(
        word = word,
        translation = translation,
        exampleSentence = exampleSentence,
        difficulty = difficulty,
        meaning = meaning.ifBlank { translation },
        nativeLanguage = nativeLanguage,
        targetLanguage = targetLanguage,
        proficiencyLevel = proficiencyLevel,
        userId = userId
    )
}
