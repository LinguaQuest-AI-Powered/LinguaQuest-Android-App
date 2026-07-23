package com.iti.linguaquest.features.roleplay.domain.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayResult
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayTurnResponse

interface RoleplayRepository {

    suspend fun initializeRoleplay(
        setting: String,
        taskDescription: String
    ): LinguaQuestResult<RoleplayTurnResponse, LinguaQuestDataError>

    suspend fun submitUserAudio(
        audioBytes: ByteArray
    ): LinguaQuestResult<RoleplayTurnResponse, LinguaQuestDataError>

    suspend fun evaluateRoleplay(): LinguaQuestResult<RoleplayResult, LinguaQuestDataError>
}
