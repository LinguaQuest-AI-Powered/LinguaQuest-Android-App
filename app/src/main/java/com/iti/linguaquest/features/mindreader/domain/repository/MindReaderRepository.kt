package com.iti.linguaquest.features.mindreader.domain.repository

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderDataset

interface MindReaderRepository {
    suspend fun loadDataset(): MindReaderDataset
}
