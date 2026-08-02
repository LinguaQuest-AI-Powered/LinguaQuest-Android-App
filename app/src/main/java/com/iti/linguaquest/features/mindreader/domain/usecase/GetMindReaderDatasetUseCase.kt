package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderDataset
import com.iti.linguaquest.features.mindreader.domain.repository.MindReaderRepository
import javax.inject.Inject

class GetMindReaderDatasetUseCase @Inject constructor(
    private val repository: MindReaderRepository
) {
    suspend operator fun invoke(): MindReaderDataset {
        return repository.loadDataset()
    }
}
