package com.iti.linguaquest.features.auth.presentation.otp.viewmodel

import app.cash.turbine.test
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.usecase.SendPasswordResetOtpUseCase
import com.iti.linguaquest.features.auth.domain.usecase.SendRegistrationOtpUseCase
import com.iti.linguaquest.features.auth.domain.usecase.VerifyEmailOtpUseCase
import com.iti.linguaquest.features.auth.domain.usecase.VerifyPasswordResetOtpUseCase
import com.iti.linguaquest.features.auth.presentation.otp.contract.OTPEffect
import com.iti.linguaquest.features.auth.presentation.otp.contract.OTPIntent
import com.iti.linguaquest.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OTPViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val verifyEmailOtpUseCase: VerifyEmailOtpUseCase = mockk()
    private val verifyPasswordResetOtpUseCase: VerifyPasswordResetOtpUseCase = mockk()
    private val sendRegistrationOtpUseCase: SendRegistrationOtpUseCase = mockk(relaxed = true)
    private val sendPasswordResetOtpUseCase: SendPasswordResetOtpUseCase = mockk(relaxed = true)
    private val snackbarController: SnackbarController = mockk(relaxed = true)
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase = mockk()

    private lateinit var viewModel: OTPViewModel

    @Before
    fun setup() {
        every { observeNetworkStatusUseCase() } returns flowOf(true)

        viewModel = OTPViewModel(
            verifyEmailOtpUseCase = verifyEmailOtpUseCase,
            verifyPasswordResetOtpUseCase = verifyPasswordResetOtpUseCase,
            sendRegistrationOtpUseCase = sendRegistrationOtpUseCase,
            sendPasswordResetOtpUseCase = sendPasswordResetOtpUseCase,
            snackbarController = snackbarController,
            observeNetworkStatusUseCase = observeNetworkStatusUseCase
        )
    }

    @Test
    fun onIntent_initializesAndStartsTimerForRegistration_whenInitializeWithIsPasswordResetFalse() = runTest {
        // Given
        val email = "test@example.com"

        // When
        viewModel.onIntent(OTPIntent.Initialize(email, isPasswordReset = false))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.isTimerActive)
        }
        coVerify(exactly = 1) { sendRegistrationOtpUseCase(email) }
        coVerify(exactly = 0) { sendPasswordResetOtpUseCase(any()) }
        coVerify(exactly = 1) {
            snackbarController.sendEvent(
                match { event ->
                    event.type == SnackbarType.SUCCESS
                }
            )
        }
    }

    @Test
    fun onIntent_initializesAndStartsTimerForPasswordReset_whenInitializeWithIsPasswordResetTrue() = runTest {
        // Given
        val email = "test@example.com"

        // When
        viewModel.onIntent(OTPIntent.Initialize(email, isPasswordReset = true))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.isTimerActive)
        }
        coVerify(exactly = 1) { sendPasswordResetOtpUseCase(email) }
        coVerify(exactly = 0) { sendRegistrationOtpUseCase(any()) }
    }

    @Test
    fun onIntent_updatesStateOtpCodeAndEnablesVerify_whenOtpCodeChangedAndLengthIs4() = runTest {
        // When
        viewModel.onIntent(OTPIntent.OnOtpCodeChanged("1234"))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals("1234", state.otpCode)
            assertTrue(state.isVerifyEnabled)
        }
    }

    @Test
    fun onIntent_updatesStateOtpCodeAndDisablesVerify_whenOtpCodeChangedAndLengthIsLessThan4() = runTest {
        // When
        viewModel.onIntent(OTPIntent.OnOtpCodeChanged("12"))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals("12", state.otpCode)
            assertFalse(state.isVerifyEnabled)
        }
    }

    @Test
    fun onIntent_verifiesEmailOtpAndNavigates_whenVerifyClickedForEmailRegistrationAndSucceeds() = runTest {
        // Given
        val email = "test@example.com"
        val code = "1234"
        viewModel.onIntent(OTPIntent.Initialize(email, isPasswordReset = false))
        viewModel.onIntent(OTPIntent.OnOtpCodeChanged(code))
        coEvery { verifyEmailOtpUseCase(email, code) } returns LinguaQuestResult.Success(true)

        // When
        viewModel.onIntent(OTPIntent.OnVerifyClicked)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
        }
        viewModel.effect.test {
            val effect = awaitItem()
            assertEquals(OTPEffect.NavigateToNextScreen(null), effect)
        }
        coVerify(exactly = 1) { verifyEmailOtpUseCase(email, code) }
    }

    @Test
    fun onIntent_verifiesEmailOtpAndShowsSnackbar_whenVerifyClickedForEmailRegistrationAndFails() = runTest {
        // Given
        val email = "test@example.com"
        val code = "1234"
        viewModel.onIntent(OTPIntent.Initialize(email, isPasswordReset = false))
        viewModel.onIntent(OTPIntent.OnOtpCodeChanged(code))
        coEvery { verifyEmailOtpUseCase(email, code) } returns LinguaQuestResult.Failure(AuthError.InvalidOtp)

        // When
        viewModel.onIntent(OTPIntent.OnVerifyClicked)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
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
    fun onIntent_verifiesPasswordResetOtpAndNavigates_whenVerifyClickedForPasswordResetAndSucceeds() = runTest {
        // Given
        val email = "test@example.com"
        val code = "1234"
        val resetToken = "valid_reset_token"
        viewModel.onIntent(OTPIntent.Initialize(email, isPasswordReset = true))
        viewModel.onIntent(OTPIntent.OnOtpCodeChanged(code))
        coEvery { verifyPasswordResetOtpUseCase(email, code) } returns LinguaQuestResult.Success(resetToken)

        // When
        viewModel.onIntent(OTPIntent.OnVerifyClicked)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
        }
        viewModel.effect.test {
            val effect = awaitItem()
            assertEquals(OTPEffect.NavigateToNextScreen(resetToken), effect)
        }
        coVerify(exactly = 1) { verifyPasswordResetOtpUseCase(email, code) }
    }

    @Test
    fun onIntent_emitsNavigateBack_whenBackClicked() = runTest {
        // When
        viewModel.onIntent(OTPIntent.OnBackClicked)

        // Then
        viewModel.effect.test {
            val effect = awaitItem()
            assertEquals(OTPEffect.NavigateBack, effect)
        }
    }

    @Test
    fun onIntent_emitsNavigateToLogin_whenBackToLoginClicked() = runTest {
        // When
        viewModel.onIntent(OTPIntent.OnBackToLoginClicked)

        // Then
        viewModel.effect.test {
            val effect = awaitItem()
            assertEquals(OTPEffect.NavigateToLogin, effect)
        }
    }
}
