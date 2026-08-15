package com.iti.linguaquest.features.auth.presentation.newpassword.viewmodel

import app.cash.turbine.test
import com.iti.linguaquest.R
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.utils.ValidationUtils
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.usecase.SetNewPasswordUseCase
import com.iti.linguaquest.features.auth.presentation.newpassword.contract.NewPasswordEffect
import com.iti.linguaquest.features.auth.presentation.newpassword.contract.NewPasswordIntent
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

class NewPasswordViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val setNewPasswordUseCase: SetNewPasswordUseCase = mockk()
    private val snackbarController: SnackbarController = mockk(relaxed = true)
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase = mockk()

    private lateinit var viewModel: NewPasswordViewModel

    @Before
    fun setup() {
        mockkObject(ValidationUtils)
        every { ValidationUtils.getPasswordValidationErrorRes(any(), any()) } answers {
            val pass = firstArg<String>()
            if (pass.isBlank()) R.string.new_password_error_required else null
        }
        every { observeNetworkStatusUseCase() } returns flowOf(true)

        viewModel = NewPasswordViewModel(
            setNewPasswordUseCase = setNewPasswordUseCase,
            snackbarController = snackbarController,
            observeNetworkStatusUseCase = observeNetworkStatusUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkObject(ValidationUtils)
    }

    @Test
    fun onIntent_validatesPasswordAndEmitsError_whenPasswordIsBlank() = runTest {
        // Given
        val resetToken = "valid_reset_token"
        val password = ""
        val confirmPassword = ""

        // When
        viewModel.onIntent(NewPasswordIntent.ResetPasswordClicked(resetToken, password, confirmPassword))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.newPasswordError)
            assertEquals(R.string.new_password_error_required, state.newPasswordErrorRes)
        }
        viewModel.effects.test {
            assertEquals(NewPasswordEffect.ShakeNewPassword, awaitItem())
            assertEquals(NewPasswordEffect.ShakeConfirmPassword, awaitItem())
        }
    }

    @Test
    fun onIntent_validatesConfirmPasswordAndEmitsError_whenPasswordsDoNotMatch() = runTest {
        // Given
        val resetToken = "valid_reset_token"
        val password = "Password123"
        val confirmPassword = "DifferentPassword123"

        // When
        viewModel.onIntent(NewPasswordIntent.ResetPasswordClicked(resetToken, password, confirmPassword))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.confirmPasswordError)
            assertEquals(R.string.new_password_error_no_match, state.confirmPasswordErrorRes)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(NewPasswordEffect.ShakeConfirmPassword, effect)
        }
    }

    @Test
    fun onIntent_resetsPasswordAndNavigates_whenInputsAreValidAndSucceeds() = runTest {
        // Given
        val resetToken = "valid_reset_token"
        val password = "Password123"
        val confirmPassword = "Password123"
        coEvery { setNewPasswordUseCase(password, resetToken) } returns LinguaQuestResult.Success(Unit)

        // When
        viewModel.onIntent(NewPasswordIntent.ResetPasswordClicked(resetToken, password, confirmPassword))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(NewPasswordEffect.ResetSucceeded, effect)
        }
        coVerify(exactly = 1) { setNewPasswordUseCase(password, resetToken) }
    }

    @Test
    fun onIntent_resetsPasswordAndShowsSnackbar_whenInputsAreValidAndFails() = runTest {
        // Given
        val resetToken = "valid_reset_token"
        val password = "Password123"
        val confirmPassword = "Password123"
        coEvery { setNewPasswordUseCase(password, resetToken) } returns LinguaQuestResult.Failure(AuthError.ResetTokenExpired)

        // When
        viewModel.onIntent(NewPasswordIntent.ResetPasswordClicked(resetToken, password, confirmPassword))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(R.string.login_error_reset_token_expired, state.generalErrorRes)
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
        viewModel.onIntent(NewPasswordIntent.BackToLoginClicked)

        // Then
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(NewPasswordEffect.NavigateBackToLogin, effect)
        }
    }
}
