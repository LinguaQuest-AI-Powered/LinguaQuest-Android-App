package com.iti.linguaquest.features.gallery.domain.usecase

import com.iti.linguaquest.core.result.EmptyResult
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.features.gallery.domain.repository.WordRepository
import jakarta.inject.Inject


class DeleteWordByIdUseCase @Inject constructor(
    private val repository: WordRepository
) {
    suspend operator fun invoke(
        wordId: Int
    ): EmptyResult<LinguaQuestDataError.Local> =
        repository.deleteWordById(wordId)

}