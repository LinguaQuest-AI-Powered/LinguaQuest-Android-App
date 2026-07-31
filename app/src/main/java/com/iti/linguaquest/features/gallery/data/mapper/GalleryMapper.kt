package com.iti.linguaquest.features.gallery.data.mapper

import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.features.gallery.data.datasource.remote.GalleryWordDto

fun List<GalleryWordDto>.toWordEntities(
    sourceLanguage: String,
    targetLanguage: String
): List<WordEntity> = map { dto ->
    WordEntity(
        sourceWord = dto.word.orEmpty(),
        translatedWord = dto.nativeWord.orEmpty(),
        sourceLanguage = sourceLanguage,
        targetLanguage = targetLanguage,
        category = dto.world?.name.orEmpty().ifBlank { "General" },
        imagePath = dto.world?.imageUrl.orEmpty(),
        isCorrect = false
    )
}
