package com.iti.linguaquest.features.gallery.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.gallery.domain.repository.WordRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class RefreshGalleryUseCase @Inject constructor(
    private val wordRepository: WordRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(): LinguaQuestResult<Unit, LinguaQuestDataError> {
        val sourceLanguage = userPreferencesRepository.targetLanguageName
            .firstOrNull()
            .orEmpty()
            .ifBlank { "English" }

        val targetLanguage = userPreferencesRepository.nativeLanguageName
            .firstOrNull()
            .orEmpty()
            .ifBlank { "Arabic" }

        return wordRepository.refreshGalleryWords(
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage
        )
    }
}