package com.iti.linguaquest.features.home.data.dataSource.local

import com.google.gson.Gson
import com.iti.linguaquest.core.database.home.HomeDao
import com.iti.linguaquest.core.database.home.HomeEntity
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.ActiveLanguageDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.ExploreWorldsContainerDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.HomeSummaryDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeLocalDataSourceImpl @Inject constructor(
    private val homeDao: HomeDao,
    private val gson: Gson
) : HomeLocalDataSource {

    override fun observeHomeSummary(): Flow<HomeSummaryDto?> {
        return homeDao.getHomeSummary().map { entity ->
            entity?.toDto(gson)
        }
    }

    override suspend fun upsertHomeSummary(dto: HomeSummaryDto) {
        homeDao.upsertHomeSummary(dto.toEntity(gson))
    }

    override suspend fun clearHomeSummary() {
        homeDao.clearHomeSummary()
    }
}

private fun HomeSummaryDto.toEntity(gson: Gson): HomeEntity {
    val lang = activeLanguage
    val worldsJson = exploreWorlds?.let { gson.toJson(it) } ?: "null"
    return HomeEntity(
        id = 1,
        xp = xp ?: 0,
        coins = coins ?: 0,
        streakDays = streakDays ?: 0,
        activeLanguageId = lang?.id,
        activeLanguageName = lang?.name,
        activeLanguageCode = lang?.code,
        activeLanguageImageUrl = lang?.imageUrl,
        activeLanguageLevel = lang?.level,
        activeLanguageProgressPercent = lang?.progressPercent ?: lang?.levelProgressPercent,
        activeLanguageIsActive = lang?.isActive,
        exploreWorldsJson = worldsJson
    )
}

private fun HomeEntity.toDto(gson: Gson): HomeSummaryDto {
    val activeLanguageDto = if (activeLanguageId != null) {
        ActiveLanguageDto(
            id = activeLanguageId,
            name = activeLanguageName,
            code = activeLanguageCode,
            imageUrl = activeLanguageImageUrl,
            level = activeLanguageLevel,
            isActive = activeLanguageIsActive,
            progressPercent = activeLanguageProgressPercent,
            levelProgressPercent = activeLanguageProgressPercent
        )
    } else null

    val exploreWorlds = try {
        gson.fromJson(exploreWorldsJson, ExploreWorldsContainerDto::class.java)
    } catch (e: Exception) {
        null
    }

    return HomeSummaryDto(
        xp = xp,
        coins = coins,
        streakDays = streakDays,
        activeLanguage = activeLanguageDto,
        exploreWorlds = exploreWorlds
    )
}
