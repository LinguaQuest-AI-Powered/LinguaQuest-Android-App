package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderCategory
import com.iti.linguaquest.features.mindreader.domain.repository.MindReaderRepository
import javax.inject.Inject

class GetMindReaderCategoriesUseCase @Inject constructor(
    private val repository: MindReaderRepository
) {
    suspend operator fun invoke(): List<MindReaderCategory> {
        return repository.getCategories()
    }
}
