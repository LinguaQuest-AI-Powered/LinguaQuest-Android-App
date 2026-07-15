package com.iti.linguaquest.feature.auth.login.data.repository

import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.feature.auth.login.domain.model.AuthLoginResult
import com.iti.linguaquest.feature.auth.login.domain.model.AuthUser
import com.iti.linguaquest.feature.auth.login.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor() : LoginRepository {
    override suspend fun loginWithEmail(
        email: String,
        password: String,
    ): LinguaQuestResult<AuthLoginResult, LinguaQuestDataError.Auth> {
        return LinguaQuestResult.Failure(LinguaQuestDataError.Auth.UNKNOWN)
    }

    override suspend fun loginWithGoogle(
        idToken: String,
    ): LinguaQuestResult<AuthLoginResult, LinguaQuestDataError.Auth> {
        return LinguaQuestResult.Failure(LinguaQuestDataError.Auth.UNKNOWN)
    }

    override suspend fun continueAsGuest(): LinguaQuestResult<AuthUser, LinguaQuestDataError.Auth> {
        return LinguaQuestResult.Failure(LinguaQuestDataError.Auth.UNKNOWN)
    }
}
