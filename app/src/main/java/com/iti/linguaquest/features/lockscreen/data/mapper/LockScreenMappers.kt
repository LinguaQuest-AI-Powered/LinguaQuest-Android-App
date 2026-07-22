package com.iti.linguaquest.features.lockscreen.data.mapper

import com.iti.linguaquest.core.database.lockscreen.LockScreenWordEntity
import com.iti.linguaquest.features.lockscreen.domain.model.GeneratedVocabularyWord
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord

fun LockScreenWordEntity.toDomain(): LockScreenWord {
    return LockScreenWord(
        id = id,
        word = word,
        translation = translation,
        exampleSentence = exampleSentence,
        status = runCatching { com.iti.linguaquest.core.database.lockscreen.LockScreenWordStatus.valueOf(status) }
            .getOrDefault(com.iti.linguaquest.core.database.lockscreen.LockScreenWordStatus.PENDING),
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
    proficiencyLevel: String
): LockScreenWordEntity {
    return LockScreenWordEntity(
        word = word,
        translation = translation,
        exampleSentence = exampleSentence,
        nativeLanguage = nativeLanguage,
        targetLanguage = targetLanguage,
        proficiencyLevel = proficiencyLevel
    )
}
