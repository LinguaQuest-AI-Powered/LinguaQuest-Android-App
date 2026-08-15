package com.iti.linguaquest.features.auth.data.repository

import com.iti.linguaquest.core.cache.data.datasource.SessionManagerDataSource
import com.iti.linguaquest.core.cache.data.datasource.UserPreferencesLocalDataSource
import com.iti.linguaquest.core.cache.token.TokensLocalDataSource
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.session.SessionEvent
import com.iti.linguaquest.core.session.SessionEventBus
import com.iti.linguaquest.features.auth.data.datasource.remote.*
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.model.AuthUser
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {

    private val remoteDataSource: AuthRemoteDataSource = mockk()
    private val tokensLocalDataSource: TokensLocalDataSource = mockk(relaxed = true)
    private val userPreferencesLocalDataSource: UserPreferencesLocalDataSource = mockk(relaxed = true)
    private val sessionManagerDataSource: SessionManagerDataSource = mockk(relaxed = true)
    private val sessionEventBus: SessionEventBus = mockk(relaxed = true)

    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        every { userPreferencesLocalDataSource.nativeLanguageName } returns flowOf("English")
        every { userPreferencesLocalDataSource.targetLanguageName } returns flowOf("Spanish")
        every { sessionManagerDataSource.lastLoggedInUserId } returns flowOf(null)

        repository = AuthRepositoryImpl(
            remoteDataSource = remoteDataSource,
            tokensLocalDataSource = tokensLocalDataSource,
            userPreferencesLocalDataSource = userPreferencesLocalDataSource,
            sessionManagerDataSource = sessionManagerDataSource,
            sessionEventBus = sessionEventBus
        )
    }

    @Test
    fun register_returnsSuccess_whenRemoteCallSucceeds() = runTest {
        // Given
        val email = "test@example.com"
        val username = "username"
        val password = "Password123"
        val request = RegisterRequestDto(email, username, password, "English", "Spanish")
        val response = RegisterResponseDataDto(1, email, username, null, "Spanish", false)
        coEvery { remoteDataSource.register(request) } returns LinguaQuestResult.Success(response)

        // When
        val result = repository.register(email, username, password)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        coVerify(exactly = 1) { remoteDataSource.register(request) }
    }

    @Test
    fun register_returnsFailure_whenRemoteCallFails() = runTest {
        // Given
        val email = "test@example.com"
        val username = "username"
        val password = "Password123"
        val request = RegisterRequestDto(email, username, password, "English", "Spanish")
        coEvery { remoteDataSource.register(request) } returns LinguaQuestResult.Failure(LinguaQuestDataError.Auth.EMAIL_ALREADY_EXISTS)

        // When
        val result = repository.register(email, username, password)

        // Then
        assertTrue(result is LinguaQuestResult.Failure)
        assertEquals(AuthError.EmailAlreadyExists, (result as LinguaQuestResult.Failure).error)
    }

    @Test
    fun login_savesTokensAndReturnsSuccess_whenRemoteCallSucceeds() = runTest {
        // Given
        val email = "test@example.com"
        val password = "Password123"
        val request = LoginRequestDto(email, password)
        val response = LoginResponseDataDto(
            accessToken = "access",
            refreshToken = "refresh",
            tokenType = "Bearer",
            expiresIn = 3600,
            user = UserDto(1, "test", null, null, true, emptyList())
        )
        coEvery { remoteDataSource.login(request) } returns LinguaQuestResult.Success(response)

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        val user = (result as LinguaQuestResult.Success).data
        assertEquals("test", user.username)
        coVerify {
            sessionManagerDataSource.saveLastLoggedInUserId(1)
            tokensLocalDataSource.saveTokens("access", "refresh")
            sessionManagerDataSource.saveIsLoggedIn(true)
            userPreferencesLocalDataSource.clearTargetLanguage()
        }
    }

    @Test
    fun signInWithGoogle_savesTokensAndReturnsSuccess_whenRemoteCallSucceeds() = runTest {
        // Given
        val idToken = "id-token"
        val request = OAuthGoogleRequestDto(idToken)
        val response = OAuthResponseDataDto(
            accessToken = "access",
            refreshToken = "refresh",
            tokenType = "Bearer",
            expiresIn = 3600,
            isNewUser = false,
            profileComplete = true,
            user = UserDto(1, "test", null, null, true, emptyList())
        )
        coEvery { remoteDataSource.loginWithGoogle(request) } returns LinguaQuestResult.Success(response)

        // When
        val result = repository.signInWithGoogle(idToken)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        val data = (result as LinguaQuestResult.Success).data
        assertTrue(data.profileComplete)
        coVerify {
            tokensLocalDataSource.saveTokens("access", "refresh")
            sessionManagerDataSource.saveIsLoggedIn(true)
        }
    }

    @Test
    fun completeOAuthProfile_savesTokensAndReturnsSuccess_whenRemoteCallSucceeds() = runTest {
        // Given
        val request = CompleteProfileRequestDto(1, 2, "test_user")
        val response = OAuthResponseDataDto(
            accessToken = "access",
            refreshToken = "refresh",
            tokenType = "Bearer",
            expiresIn = 3600,
            isNewUser = false,
            profileComplete = true,
            user = UserDto(1, "test_user", null, null, true, emptyList())
        )
        coEvery { remoteDataSource.completeOAuthProfile(request) } returns LinguaQuestResult.Success(response)

        // When
        val result = repository.completeOAuthProfile(1, 2, "test_user")

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        coVerify {
            tokensLocalDataSource.saveTokens("access", "refresh")
            sessionManagerDataSource.saveIsLoggedIn(true)
        }
    }

    @Test
    fun logout_clearsLocalSessionDataAndEmitsEvent_whenCalled() = runTest {
        // Given
        every { tokensLocalDataSource.refreshToken } returns flowOf("refresh")
        coEvery { remoteDataSource.logout(any()) } returns LinguaQuestResult.Success(Unit)

        // When
        val result = repository.logout()

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        coVerify {
            remoteDataSource.logout(LogoutRequestDto("refresh"))
            tokensLocalDataSource.clearTokens()
            userPreferencesLocalDataSource.clearOnboardingPreferences()
            sessionManagerDataSource.saveFirstTime(true)
            sessionManagerDataSource.saveIsLoggedIn(false)
            sessionManagerDataSource.clearSessionData()
            sessionEventBus.emit(SessionEvent.LoggedOut)
        }
    }
}
