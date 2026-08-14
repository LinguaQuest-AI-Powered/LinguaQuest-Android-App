package com.iti.linguaquest.features.mindreader.data.datasource.local

import com.iti.linguaquest.features.mindreader.data.dto.CategoryDto
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameConfig

interface MindReaderLocalDataSource {
    suspend fun getCategories(): List<CategoryDto>
    suspend fun getGameConfig(): MindReaderGameConfig
}
