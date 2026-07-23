package com.iti.linguaquest.features.roleplay.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayResult
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import javax.inject.Inject

class EvaluateRoleplayUseCase @Inject constructor(
    private val repository: RoleplayRepository
) {
    suspend operator fun invoke(): LinguaQuestResult<RoleplayResult, LinguaQuestDataError> {
        return repository.evaluateRoleplay()
    }
}
