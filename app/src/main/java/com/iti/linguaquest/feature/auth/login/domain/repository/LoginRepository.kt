package com.iti.linguaquest.feature.auth.login.domain.repository

import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.feature.auth.login.domain.model.AuthLoginResult
import com.iti.linguaquest.feature.auth.login.domain.model.AuthUser

interface LoginRepository {
    suspend fun loginWithEmail(
        email: String,
        password: String
    ): LinguaQuestResult<AuthLoginResult, LinguaQuestDataError.Auth>

    suspend fun loginWithGoogle(
        idToken: String,
    ): LinguaQuestResult<AuthLoginResult, LinguaQuestDataError.Auth>

    suspend fun continueAsGuest(): LinguaQuestResult<AuthUser, LinguaQuestDataError.Auth>
}
