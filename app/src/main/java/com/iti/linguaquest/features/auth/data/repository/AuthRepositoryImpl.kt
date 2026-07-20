package com.iti.linguaquest.features.auth.data.repository


import com.iti.linguaquest.core.preferences.UserPreferencesLocalDataSource
import com.iti.linguaquest.core.preferences.cache.TokensLocalDataSource
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.asEmptyDataResult
import com.iti.linguaquest.core.result.map
import com.iti.linguaquest.core.result.onSuccess
import com.iti.linguaquest.features.auth.data.datasource.remote.AuthRemoteDataSource
import com.iti.linguaquest.features.auth.data.datasource.remote.OtpSendRequestDto
import com.iti.linguaquest.features.auth.data.datasource.remote.OtpVerifyRequestDto
import com.iti.linguaquest.features.auth.data.datasource.remote.ResetPasswordRequestDto
import com.iti.linguaquest.features.auth.data.datasource.remote.LoginRequestDto
import com.iti.linguaquest.features.auth.data.datasource.remote.OAuthGoogleRequestDto
import com.iti.linguaquest.features.auth.data.datasource.remote.RegisterRequestDto
import com.iti.linguaquest.features.auth.data.mapper.toAuthError
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val tokensLocalDataSource: TokensLocalDataSource,
    private val userPreferencesLocalDataSource: UserPreferencesLocalDataSource
) : AuthRepository {

    override suspend fun register(
        email: String,
        username: String,
        password: String,
    ): LinguaQuestResult<Unit, AuthError> {

        val appLanguage = userPreferencesLocalDataSource.appLanguage.first() ?: ""
        val targetLanguage = userPreferencesLocalDataSource.targetLanguage.first() ?: ""

        val request = RegisterRequestDto(email, username, password, appLanguage, targetLanguage)

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
        return remoteDataSource.sendOtp(OtpSendRequestDto(email, "SIGNUP"))
            .mapError()
    }

    override suspend fun sendPasswordResetOtp(email: String): LinguaQuestResult<Unit, AuthError> {
        return remoteDataSource.sendOtp(OtpSendRequestDto(email, "PASSWORD_RESET"))
            .mapError()
    }

    override suspend fun verifyEmailOtp(
        email: String,
        otpCode: String
    ): LinguaQuestResult<Unit, AuthError> {
        return remoteDataSource.verifyEmailOtp(OtpVerifyRequestDto(email, otpCode))
            .mapError()
    }

    override suspend fun verifyPasswordResetOtp(
        email: String,
        otpCode: String
    ): LinguaQuestResult<String, AuthError> {
        return remoteDataSource.verifyPasswordResetOtp(OtpVerifyRequestDto(email, otpCode))
            .map { it.resetToken }
            .mapError()
    }

    override suspend fun setNewPassword(
        newPassword: String,
        resetToken: String
    ): LinguaQuestResult<Unit, AuthError> {
        return remoteDataSource.setNewPassword(ResetPasswordRequestDto(resetToken, newPassword))
            .mapError()
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return tokensLocalDataSource.isLoggedIn
    }

    override suspend fun logout(): LinguaQuestResult<Unit, AuthError> {
        tokensLocalDataSource.clearTokens()
        return LinguaQuestResult.Success(Unit)
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