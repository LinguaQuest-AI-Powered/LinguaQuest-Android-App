package com.iti.linguaquest.features.lockscreen.data.remote

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.lockscreen.domain.model.GeneratedVocabularyWord

import com.iti.linguaquest.features.lockscreen.domain.model.VocabularyBatchParams

interface LockScreenRemoteDataSource {
    suspend fun deductCoins(
        operationId: String,
        amount: Int,
        reason: String = "lock_screen_vocabulary"
    ): LinguaQuestResult<Unit, LinguaQuestDataError>

    suspend fun generateVocabulary(
        params: VocabularyBatchParams
    ): LinguaQuestResult<List<GeneratedVocabularyWord>, LinguaQuestDataError>
}
