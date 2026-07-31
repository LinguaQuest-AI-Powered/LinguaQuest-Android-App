package com.iti.linguaquest.features.mindreader.data.repository

import com.iti.linguaquest.features.mindreader.data.datasource.MindReaderDataSource
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderDataset
import com.iti.linguaquest.features.mindreader.domain.repository.MindReaderRepository
import javax.inject.Inject

class MindReaderRepositoryImpl @Inject constructor(
    private val dataSource: MindReaderDataSource
) : MindReaderRepository {

    override suspend fun loadDataset(): MindReaderDataset {
        return dataSource.loadDataset()
    }
}
