package com.iti.linguaquest.features.home.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.appicon.domain.usecase.AppIconSyncUseCase
import com.iti.linguaquest.features.home.domain.model.HomeSummary
import com.iti.linguaquest.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeSummaryUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val appIconSyncUseCase: AppIconSyncUseCase
) {

    fun observe(): Flow<HomeSummary?> = homeRepository.observeHomeSummary()

    suspend fun refresh(): LinguaQuestResult<HomeSummary, LinguaQuestDataError> {
        val result = homeRepository.refreshHomeSummary()
        if (result is LinguaQuestResult.Success) {
            appIconSyncUseCase.onHomeSnapshotLoaded(result.data.streakDays)
        }
        return result
    }
}
