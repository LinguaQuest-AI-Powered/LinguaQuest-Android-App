package com.iti.linguaquest.features.auth.data.repository


import com.iti.linguaquest.core.cache.data.datasource.UserPreferencesLocalDataSource
import com.iti.linguaquest.core.cache.token.TokensLocalDataSource
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.asEmptyDataResult
import com.iti.linguaquest.core.result.map
import com.iti.linguaquest.core.result.onSuccess
import com.iti.linguaquest.core.session.SessionEvent
import com.iti.linguaquest.core.session.SessionEventBus
import com.iti.linguaquest.features.auth.data.datasource.remote.AuthRemoteDataSource
import com.iti.linguaquest.features.auth.data.datasource.remote.OtpSendRequestDto
import com.iti.linguaquest.features.auth.data.datasource.remote.OtpVerifyRequestDto
import com.iti.linguaquest.features.auth.data.datasource.remote.ResetPasswordRequestDto
import com.iti.linguaquest.features.auth.data.datasource.remote.LoginRequestDto
import com.iti.linguaquest.features.auth.data.datasource.remote.OAuthGoogleRequestDto
import com.iti.linguaquest.features.auth.data.datasource.remote.RegisterRequestDto
import com.iti.linguaquest.features.auth.data.datasource.remote.LogoutRequestDto
import com.iti.linguaquest.features.auth.data.datasource.remote.RefreshTokenRequestDto
import com.iti.linguaquest.features.auth.data.mapper.toAuthError
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import com.iti.linguaquest.core.cache.data.datasource.SessionManagerDataSource
import com.iti.linguaquest.features.auth.data.datasource.remote.CompleteProfileRequestDto
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import com.iti.linguaquest.features.auth.domain.model.AuthUser
import com.iti.linguaquest.features.auth.domain.model.GoogleSignInResult
import com.iti.linguaquest.features.auth.data.mapper.toAuthUser
import com.iti.linguaquest.features.auth.data.mapper.toGoogleSignInResult

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val tokensLocalDataSource: TokensLocalDataSource,
    private val userPreferencesLocalDataSource: UserPreferencesLocalDataSource,
    private val sessionManagerDataSource: SessionManagerDataSource,
    private val sessionEventBus: SessionEventBus
) : AuthRepository {



    override suspend fun register(
        email: String,
        username: String,
        password: String,
    ): LinguaQuestResult<Unit, AuthError> {

        val nativeLanguageName = userPreferencesLocalDataSource.nativeLanguageName.first() ?: "English"
        val targetLanguageName = userPreferencesLocalDataSource.targetLanguageName.first() ?: "English"

        val request = RegisterRequestDto(email, username, password, nativeLanguageName, targetLanguageName)

        return remoteDataSource.register(request)
            .map { Unit }
            .mapError()
    }

    override suspend fun login(
        email: String,
        password: String
    ): LinguaQuestResult<AuthUser, AuthError> {

        val request = LoginRequestDto(email, password)
        return remoteDataSource.login(request)
            .onSuccess { response ->
                val lastId = sessionManagerDataSource.lastLoggedInUserId.first()
                if (lastId != null && lastId != response.user.id) {
                    sessionManagerDataSource.clearLocalGeneratedData()
                }
                sessionManagerDataSource.saveLastLoggedInUserId(response.user.id)
                
                userPreferencesLocalDataSource.clearTargetLanguage()
                tokensLocalDataSource.saveTokens(response.accessToken, response.refreshToken)
                sessionManagerDataSource.saveIsLoggedIn(true)
                sessionManagerDataSource.saveFirstTime(false)
            }
            .map { it.user.toAuthUser() }
            .mapError()
    }

    override suspend fun signInWithGoogle(idToken: String): LinguaQuestResult<GoogleSignInResult, AuthError> {
        val request = OAuthGoogleRequestDto(idToken)
        return remoteDataSource.loginWithGoogle(request)
            .onSuccess { response ->
                val lastId = sessionManagerDataSource.lastLoggedInUserId.first()
                if (lastId != null && lastId != response.user.id) {
                    sessionManagerDataSource.clearLocalGeneratedData()
                }
                sessionManagerDataSource.saveLastLoggedInUserId(response.user.id)
                
                tokensLocalDataSource.saveTokens(response.accessToken, response.refreshToken)
                if (response.profileComplete) {
                    userPreferencesLocalDataSource.clearTargetLanguage()
                    sessionManagerDataSource.saveIsLoggedIn(true)
                    sessionManagerDataSource.saveFirstTime(false)
                }
            }
            .map { it.toGoogleSignInResult() }
            .mapError()
    }

    override suspend fun completeOAuthProfile(
        nativeLanguageId: Int,
        targetLanguageId: Int,
        username: String?
    ): LinguaQuestResult<Unit, AuthError> {
        val request = CompleteProfileRequestDto(nativeLanguageId, targetLanguageId, username)
        return remoteDataSource.completeOAuthProfile(request)
            .onSuccess { response ->
                tokensLocalDataSource.saveTokens(response.accessToken, response.refreshToken)
                sessionManagerDataSource.saveIsLoggedIn(true)
                sessionManagerDataSource.saveFirstTime(false)
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
    ): LinguaQuestResult<Boolean, AuthError> {
        return remoteDataSource.verifyEmailOtp(OtpVerifyRequestDto(email, otpCode))
            .map { it.isVerified }
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
        return combine(
            sessionManagerDataSource.isLoggedIn,
            tokensLocalDataSource.accessToken
        ) { isLoggedIn, accessToken ->
            isLoggedIn && !accessToken.isNullOrBlank()
        }
    }

    override suspend fun logout(): LinguaQuestResult<Unit, AuthError> {
        val refreshToken = tokensLocalDataSource.refreshToken.first() ?: ""
        if (refreshToken.isNotEmpty()) {
            remoteDataSource.logout(LogoutRequestDto(refreshToken))
        }
        tokensLocalDataSource.clearTokens()
        userPreferencesLocalDataSource.clearOnboardingPreferences()
        sessionManagerDataSource.saveFirstTime(true)
        sessionManagerDataSource.saveIsLoggedIn(false)
        sessionManagerDataSource.clearSessionData()
        sessionEventBus.emit(SessionEvent.LoggedOut)
        
        return LinguaQuestResult.Success(Unit)
    }

    override suspend fun refreshToken(refreshToken: String): LinguaQuestResult<Unit, AuthError> {
        return remoteDataSource.refreshToken(RefreshTokenRequestDto(refreshToken))
            .onSuccess { response ->
                tokensLocalDataSource.saveTokens(response.accessToken, response.refreshToken)
                sessionManagerDataSource.saveIsLoggedIn(true)
                sessionManagerDataSource.saveFirstTime(false)
            }
            .asEmptyDataResult()
            .mapError()
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