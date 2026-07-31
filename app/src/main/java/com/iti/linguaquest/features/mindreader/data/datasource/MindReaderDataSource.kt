package com.iti.linguaquest.features.mindreader.data.datasource

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderDataset

interface MindReaderDataSource {
    suspend fun loadDataset(): MindReaderDataset
}
