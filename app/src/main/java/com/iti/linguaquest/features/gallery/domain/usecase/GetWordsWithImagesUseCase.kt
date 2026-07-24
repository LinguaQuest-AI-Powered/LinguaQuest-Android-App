package com.iti.linguaquest.features.gallery.domain.usecase

import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.features.gallery.domain.repository.WordRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow


class GetWordsWithImagesUseCase @Inject constructor(
    private val repository: WordRepository
) {
    operator fun invoke(): Flow<List<WordEntity>> =
        repository.getWordsWithImages()

}