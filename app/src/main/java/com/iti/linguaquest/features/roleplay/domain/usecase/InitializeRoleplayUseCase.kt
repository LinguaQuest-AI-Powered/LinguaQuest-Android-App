package com.iti.linguaquest.features.roleplay.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayTurnResponse
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import javax.inject.Inject

class InitializeRoleplayUseCase @Inject constructor(
    private val repository: RoleplayRepository
) {
    suspend operator fun invoke(
        setting: String,
        taskDescription: String
    ): LinguaQuestResult<RoleplayTurnResponse, LinguaQuestDataError> {
        return repository.initializeRoleplay(setting, taskDescription)
    }
}
