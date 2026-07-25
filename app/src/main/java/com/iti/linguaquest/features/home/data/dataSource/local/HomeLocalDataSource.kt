package com.iti.linguaquest.features.home.data.dataSource.local

import com.iti.linguaquest.features.home.data.dataSource.remote.dto.HomeSummaryDto
import kotlinx.coroutines.flow.Flow

interface HomeLocalDataSource {
    fun observeHomeSummary(): Flow<HomeSummaryDto?>
    suspend fun upsertHomeSummary(dto: HomeSummaryDto)
    suspend fun clearHomeSummary()
}
