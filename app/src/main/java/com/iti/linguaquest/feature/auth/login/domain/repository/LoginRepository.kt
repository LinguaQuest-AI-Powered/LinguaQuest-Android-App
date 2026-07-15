package com.iti.linguaquest.features.auth.login.domain.repository

import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.features.auth.login.domain.model.AuthError
import com.iti.linguaquest.features.auth.login.domain.model.AuthLoginResult
import com.iti.linguaquest.features.auth.login.domain.model.AuthUser


interface LoginRepository {
    suspend fun loginWithEmail(
        email: String,
        password: String
    ): LinguaQuestResult<AuthLoginResult, AuthError>

    suspend fun loginWithGoogle(
        idToken: String,
    ): LinguaQuestResult<AuthLoginResult, AuthError>

    suspend fun continueAsGuest(): LinguaQuestResult<AuthUser, AuthError>
}