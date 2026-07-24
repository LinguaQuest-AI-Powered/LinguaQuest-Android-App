package com.iti.linguaquest.features.gallery.domain.usecase


import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.result.EmptyResult
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.features.gallery.domain.repository.WordRepository
import javax.inject.Inject


class DeleteWordUseCase @Inject constructor(
    private val repository: WordRepository
) {
    suspend operator fun invoke(
        word: WordEntity
    ): EmptyResult<LinguaQuestDataError.Local> =
        repository.deleteWord(word)
}