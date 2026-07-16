package com.iti.linguaquest.features.auth.data.repository

import com.iti.linguaquest.core.cache.TokensLocalDataSource
import com.iti.linguaquest.core.network.LinguaQuestDataError
import com.iti.linguaquest.core.network.LinguaQuestResult
import com.iti.linguaquest.core.network.asEmptyDataResult
import com.iti.linguaquest.core.network.map
import com.iti.linguaquest.core.network.onSuccess
import com.iti.linguaquest.features.auth.data.datasource.AuthRemoteDataSource
import com.iti.linguaquest.features.auth.data.datasource.EmailRequestDto
import com.iti.linguaquest.features.auth.data.datasource.LoginRequestDto
import com.iti.linguaquest.features.auth.data.datasource.OAuthGoogleRequestDto
import com.iti.linguaquest.features.auth.data.datasource.RegisterRequestDto
import com.iti.linguaquest.features.auth.data.datasource.ResetPasswordRequestDto
import com.iti.linguaquest.features.auth.data.datasource.VerifyOtpRequestDto
import com.iti.linguaquest.features.auth.data.mapper.toAuthError
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val tokensLocalDataSource: TokensLocalDataSource
) : AuthRepository {

    override suspend fun register(
        email: String,
        username: String,
        password: String,
        nativeLanguage: String,
        targetLanguage: String
    ): LinguaQuestResult<Unit, AuthError> {

        val request = RegisterRequestDto(email, username, password, nativeLanguage, targetLanguage)

        return remoteDataSource.register(request)
            .map { Unit }
            .mapError()
    }

    override suspend fun login(
        email: String,
        password: String
    ): LinguaQuestResult<Unit, AuthError> {

        val request = LoginRequestDto(email, password)
        return remoteDataSource.login(request)
            .onSuccess { response ->
                tokensLocalDataSource.saveTokens(response.accessToken, response.refreshToken)
            }
            .asEmptyDataResult()
            .mapError()
    }

    override suspend fun signInWithGoogle(idToken: String): LinguaQuestResult<Unit, AuthError> {
        val request = OAuthGoogleRequestDto(idToken)
        return remoteDataSource.loginWithGoogle(request)
            .onSuccess { response ->
                tokensLocalDataSource.saveTokens(response.accessToken, response.refreshToken)
            }
            .asEmptyDataResult()
            .mapError()
    }

    override suspend fun sendRegistrationOtp(email: String): LinguaQuestResult<Unit, AuthError> {
        return remoteDataSource.sendRegistrationOtp(EmailRequestDto(email))
            .mapError()
    }

    override suspend fun sendPasswordResetOtp(email: String): LinguaQuestResult<Unit, AuthError> {
        TODO("Not yet implemented")

    }

    override suspend fun verifyEmailOtp(
        email: String,
        otpCode: String
    ): LinguaQuestResult<Unit, AuthError> {
        TODO("Not yet implemented")

    }

    override suspend fun verifyPasswordResetOtp(
        email: String,
        otpCode: String
    ): LinguaQuestResult<String, AuthError> {
        TODO("Not yet implemented")

    }

    override suspend fun setNewPassword(
        newPassword: String,
        resetToken: String
    ): LinguaQuestResult<Unit, AuthError> {
        TODO("Not yet implemented")

    }

    override fun isLoggedIn(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): LinguaQuestResult<Unit, AuthError> {
        TODO("Not yet implemented")
    }

    private fun <T> LinguaQuestResult<T, LinguaQuestDataError>.mapError(): LinguaQuestResult<T, AuthError> {
        return when (this) {
            is LinguaQuestResult.Success -> this
            is LinguaQuestResult.Failure -> {
                val authError =
                    if (this.error is LinguaQuestDataError.Auth) {
                        this.error.toAuthError()
                    } else {
                        AuthError.Unknown
                    }
                LinguaQuestResult.Failure(authError)
            }
        }
    }
}