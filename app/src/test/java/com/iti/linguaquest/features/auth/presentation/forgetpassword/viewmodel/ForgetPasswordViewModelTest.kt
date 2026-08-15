package com.iti.linguaquest.features.auth.presentation.forgetpassword.viewmodel

import app.cash.turbine.test
import com.iti.linguaquest.R
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.utils.ValidationUtils
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.usecase.SendPasswordResetOtpUseCase
import com.iti.linguaquest.features.auth.presentation.forgetpassword.contract.ForgetPasswordEffect
import com.iti.linguaquest.features.auth.presentation.forgetpassword.contract.ForgetPasswordIntent
import com.iti.linguaquest.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ForgetPasswordViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val sendPasswordResetOtpUseCase: SendPasswordResetOtpUseCase = mockk()
    private val snackbarController: SnackbarController = mockk(relaxed = true)
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase = mockk()

    private lateinit var viewModel: ForgetPasswordViewModel

    @Before
    fun setup() {
        mockkObject(ValidationUtils)
        every { ValidationUtils.isValidEmail(any()) } answers {
            val email = firstArg<String>()
            email.isNotBlank() && email.contains("@") && email.endsWith(".com")
        }
        every { observeNetworkStatusUseCase() } returns flowOf(true)

        viewModel = ForgetPasswordViewModel(
            sendPasswordResetOtpUseCase = sendPasswordResetOtpUseCase,
            snackbarController = snackbarController,
            observeNetworkStatusUseCase = observeNetworkStatusUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkObject(ValidationUtils)
    }

    @Test
    fun onIntent_validatesEmailAndEmitsError_whenEmailIsInvalid() = runTest {
        // Given
        val email = "invalid-email"

        // When
        viewModel.onIntent(ForgetPasswordIntent.SendClicked(email))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.emailError)
            assertEquals(R.string.forget_password_error_invalid_email, state.emailErrorRes)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(ForgetPasswordEffect.ShakeEmail, effect)
        }
    }

    @Test
    fun onIntent_sendsOtpAndNavigates_whenEmailIsValidAndSucceeds() = runTest {
        // Given
        val email = "test@example.com"
        coEvery { sendPasswordResetOtpUseCase(email) } returns LinguaQuestResult.Success(Unit)

        // When
        viewModel.onIntent(ForgetPasswordIntent.SendClicked(email))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(ForgetPasswordEffect.SendSucceeded(email), effect)
        }
        coVerify(exactly = 1) { sendPasswordResetOtpUseCase(email) }
    }

    @Test
    fun onIntent_sendsOtpAndShowsSnackbar_whenEmailIsValidAndFails() = runTest {
        // Given
        val email = "test@example.com"
        coEvery { sendPasswordResetOtpUseCase(email) } returns LinguaQuestResult.Failure(AuthError.EmailNotFound)

        // When
        viewModel.onIntent(ForgetPasswordIntent.SendClicked(email))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(R.string.login_error_email_not_found, state.generalErrorRes)
        }
        coVerify(exactly = 1) {
            snackbarController.sendEvent(
                match { event ->
                    event.type == SnackbarType.ERROR
                }
            )
        }
    }

    @Test
    fun onIntent_emitsNavigateBackToLogin_whenBackToLoginClicked() = runTest {
        // When
        viewModel.onIntent(ForgetPasswordIntent.BackToLoginClicked)

        // Then
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(ForgetPasswordEffect.NavigateBackToLogin, effect)
        }
    }
}
