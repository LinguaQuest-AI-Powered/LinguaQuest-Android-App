package com.iti.linguaquest.features.auth.data.datasource.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthRemoteDataSourceImplTest {

    private lateinit var apiService: AuthApiService
    private lateinit var dataSource: AuthRemoteDataSourceImpl

    @Before
    fun setUp() {
        apiService = mockk()
        dataSource = AuthRemoteDataSourceImpl(apiService)
    }

    @Test
    fun register_returnsSuccess_whenApiCallSucceeds() = runTest {
        // Given
        val request = RegisterRequestDto("test@example.com", "username", "Password123", "English", "Spanish")
        val responseData = RegisterResponseDataDto(
            id = 1,
            email = "test@example.com",
            username = "username",
            nativeLanguage = null,
            targetLanguage = "Spanish",
            isVerified = false
        )
        val successResponse = SuccessResponseDto(success = true, data = responseData)
        coEvery { apiService.register(request) } returns successResponse

        // When
        val result = dataSource.register(request)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        assertEquals(responseData, (result as LinguaQuestResult.Success).data)
        coVerify(exactly = 1) { apiService.register(request) }
    }

    @Test
    fun register_returnsFailure_whenApiCallThrowsException() = runTest {
        // Given
        val request = RegisterRequestDto("test@example.com", "username", "Password123", "English", "Spanish")
        coEvery { apiService.register(request) } throws RuntimeException("Network Error")

        // When
        val result = dataSource.register(request)

        // Then
        assertTrue(result is LinguaQuestResult.Failure)
        assertEquals(LinguaQuestDataError.Remote.UNKNOWN, (result as LinguaQuestResult.Failure).error)
    }

    @Test
    fun login_returnsSuccess_whenApiCallSucceeds() = runTest {
        // Given
        val request = LoginRequestDto("test@example.com", "Password123")
        val responseData = LoginResponseDataDto(
            accessToken = "access_token",
            refreshToken = "refresh_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            user = UserDto(1, "test", null, null, true, emptyList())
        )
        val successResponse = SuccessResponseDto(success = true, data = responseData)
        coEvery { apiService.login(request) } returns successResponse

        // When
        val result = dataSource.login(request)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        assertEquals(responseData, (result as LinguaQuestResult.Success).data)
        coVerify(exactly = 1) { apiService.login(request) }
    }

    @Test
    fun login_returnsFailure_whenApiCallThrowsException() = runTest {
        // Given
        val request = LoginRequestDto("test@example.com", "Password123")
        coEvery { apiService.login(request) } throws RuntimeException("Network Error")

        // When
        val result = dataSource.login(request)

        // Then
        assertTrue(result is LinguaQuestResult.Failure)
        assertEquals(LinguaQuestDataError.Remote.UNKNOWN, (result as LinguaQuestResult.Failure).error)
    }

    @Test
    fun loginWithGoogle_returnsSuccess_whenApiCallSucceeds() = runTest {
        // Given
        val request = OAuthGoogleRequestDto("id-token")
        val responseData = OAuthResponseDataDto(
            accessToken = "access_token",
            refreshToken = "refresh_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            isNewUser = true,
            profileComplete = false,
            user = UserDto(1, "google_user", null, null, true, emptyList())
        )
        val successResponse = SuccessResponseDto(success = true, data = responseData)
        coEvery { apiService.loginWithGoogle(request) } returns successResponse

        // When
        val result = dataSource.loginWithGoogle(request)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        assertEquals(responseData, (result as LinguaQuestResult.Success).data)
        coVerify(exactly = 1) { apiService.loginWithGoogle(request) }
    }

    @Test
    fun completeOAuthProfile_returnsSuccess_whenApiCallSucceeds() = runTest {
        // Given
        val request = CompleteProfileRequestDto(1, 2, "new_username")
        val responseData = OAuthResponseDataDto(
            accessToken = "access_token",
            refreshToken = "refresh_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            isNewUser = false,
            profileComplete = true,
            user = UserDto(1, "new_username", null, null, true, emptyList())
        )
        val successResponse = SuccessResponseDto(success = true, data = responseData)
        coEvery { apiService.completeOAuthProfile(request) } returns successResponse

        // When
        val result = dataSource.completeOAuthProfile(request)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        assertEquals(responseData, (result as LinguaQuestResult.Success).data)
        coVerify(exactly = 1) { apiService.completeOAuthProfile(request) }
    }

    @Test
    fun sendOtp_returnsSuccess_whenApiCallSucceeds() = runTest {
        // Given
        val request = OtpSendRequestDto("test@example.com", "SIGNUP")
        val successResponse = SuccessResponseDto(success = true, data = Unit)
        coEvery { apiService.sendOtp(request) } returns successResponse

        // When
        val result = dataSource.sendOtp(request)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        coVerify(exactly = 1) { apiService.sendOtp(request) }
    }

    @Test
    fun verifyEmailOtp_returnsSuccess_whenApiCallSucceeds() = runTest {
        // Given
        val request = OtpVerifyRequestDto("test@example.com", "123456")
        val responseData = VerifyEmailResponseDto(isVerified = true)
        val successResponse = SuccessResponseDto(success = true, data = responseData)
        coEvery { apiService.verifyEmailOtp(request) } returns successResponse

        // When
        val result = dataSource.verifyEmailOtp(request)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        assertEquals(responseData, (result as LinguaQuestResult.Success).data)
        coVerify(exactly = 1) { apiService.verifyEmailOtp(request) }
    }

    @Test
    fun logout_returnsSuccess_whenApiCallSucceeds() = runTest {
        // Given
        val request = LogoutRequestDto("refresh_token")
        val successResponse = SuccessResponseDto(success = true, data = Unit)
        coEvery { apiService.logout(request) } returns successResponse

        // When
        val result = dataSource.logout(request)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        coVerify(exactly = 1) { apiService.logout(request) }
    }

    @Test
    fun refreshToken_returnsSuccess_whenApiCallSucceeds() = runTest {
        // Given
        val request = RefreshTokenRequestDto("refresh_token")
        val responseData = RefreshTokenResponseDataDto(
            accessToken = "new_access_token",
            refreshToken = "new_refresh_token",
            tokenType = "Bearer",
            expiresIn = 3600
        )
        val successResponse = SuccessResponseDto(success = true, data = responseData)
        coEvery { apiService.refreshToken(request) } returns successResponse

        // When
        val result = dataSource.refreshToken(request)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        assertEquals(responseData, (result as LinguaQuestResult.Success).data)
        coVerify(exactly = 1) { apiService.refreshToken(request) }
    }
}
