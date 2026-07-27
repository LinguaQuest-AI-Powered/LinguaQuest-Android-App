package com.iti.linguaquest.features.gallery.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.gallery.domain.repository.WordRepository
import javax.inject.Inject

class RefreshGalleryUseCase @Inject constructor(
    private val repository: WordRepository
) {
    suspend operator fun invoke(): LinguaQuestResult<Unit, LinguaQuestDataError> =
        repository.refreshGalleryWords()
}
