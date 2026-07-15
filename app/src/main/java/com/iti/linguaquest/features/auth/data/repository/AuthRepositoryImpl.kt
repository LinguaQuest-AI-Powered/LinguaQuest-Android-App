package com.iti.linguaquest.features.auth.data.repository

import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.features.auth.data.mapper.toAuthError
import com.iti.linguaquest.features.auth.domain.model.AuthLoginResult
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import javax.inject.Inject
import com.iti.linguaquest.features.auth.domain.model.AuthError



class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    override suspend fun loginWithEmail(
        email: String,
        password: String,
    ): LinguaQuestResult<AuthLoginResult, AuthError> {
          return LinguaQuestResult.Failure(LinguaQuestDataError.Auth.UNKNOWN.toAuthError())
    }

    override suspend fun loginWithGoogle(
        idToken: String,
    ): LinguaQuestResult<AuthLoginResult, AuthError> {
        return LinguaQuestResult.Failure(LinguaQuestDataError.Auth.UNKNOWN.toAuthError())
    }
}