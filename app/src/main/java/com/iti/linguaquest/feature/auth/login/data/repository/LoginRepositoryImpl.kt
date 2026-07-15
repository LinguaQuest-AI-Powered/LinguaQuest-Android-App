package com.iti.linguaquest.features.auth.login.data.repository

import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.features.auth.login.data.mapper.toAuthError
import com.iti.linguaquest.features.auth.login.domain.model.AuthLoginResult
import com.iti.linguaquest.features.auth.login.domain.model.AuthUser
import com.iti.linguaquest.features.auth.login.domain.repository.LoginRepository
import javax.inject.Inject
import com.iti.linguaquest.features.auth.login.domain.model.AuthError



class LoginRepositoryImpl @Inject constructor() : LoginRepository {
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

    override suspend fun continueAsGuest(): LinguaQuestResult<AuthUser, AuthError> {
        return LinguaQuestResult.Failure(LinguaQuestDataError.Auth.UNKNOWN.toAuthError())
    }
}