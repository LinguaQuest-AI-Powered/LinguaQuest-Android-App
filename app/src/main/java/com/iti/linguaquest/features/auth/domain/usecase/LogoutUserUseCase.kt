package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.network.EmptyResult
import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.features.auth.domain.repo.AuthRepository
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): EmptyResult<LinguaQuestDataError> {
        return repository.logout()
    }
}