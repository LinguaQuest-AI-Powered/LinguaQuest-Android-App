package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.features.notification.domain.usecase.RegisterDeviceTokenUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.model.AuthUser
import com.iti.linguaquest.features.auth.domain.model.GoogleSignInResult
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SignInWithGoogleUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var registerDeviceTokenUseCase: RegisterDeviceTokenUseCase
    private lateinit var syncUserNativeLanguageUseCase: SyncUserNativeLanguageUseCase
    private lateinit var useCase: SignInWithGoogleUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        registerDeviceTokenUseCase = mockk(relaxed = true)
        syncUserNativeLanguageUseCase = mockk(relaxed = true)
        useCase = SignInWithGoogleUseCase(authRepository, registerDeviceTokenUseCase, syncUserNativeLanguageUseCase)
    }

    @Test
    fun invokeReturnsSuccessWhenGoogleSignInSucceeds() = runTest {
        val idToken = "valid_token"
        val authUser = AuthUser(1, "test", null, null, true, emptyList())
        val googleResult = GoogleSignInResult(profileComplete = true, user = authUser)
        coEvery { authRepository.signInWithGoogle(idToken) } returns LinguaQuestResult.Success(googleResult)

        val result = useCase(idToken)

        assertEquals(LinguaQuestResult.Success(true), result)
        coVerify(exactly = 1) { authRepository.signInWithGoogle(idToken) }
    }

    @Test
    fun invokeReturnsErrorWhenGoogleSignInFails() = runTest {
        val idToken = "invalid_token"
        val error: AuthError = AuthError.InvalidIdToken
        coEvery { authRepository.signInWithGoogle(idToken) } returns LinguaQuestResult.Failure(error)

        val result = useCase(idToken)

        assertEquals(LinguaQuestResult.Failure(error), result)
        coVerify(exactly = 1) { authRepository.signInWithGoogle(idToken) }
    }
}
