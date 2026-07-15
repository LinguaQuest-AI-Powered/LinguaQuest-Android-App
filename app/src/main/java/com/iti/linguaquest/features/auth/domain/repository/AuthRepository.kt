package com.iti.linguaquest.features.auth.domain.repository

import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.model.AuthLoginResult


interface AuthRepository {
    suspend fun loginWithEmail(
        email: String,
        password: String
    ): LinguaQuestResult<AuthLoginResult, AuthError>

    suspend fun loginWithGoogle(
        idToken: String,
    ): LinguaQuestResult<AuthLoginResult, AuthError>

}