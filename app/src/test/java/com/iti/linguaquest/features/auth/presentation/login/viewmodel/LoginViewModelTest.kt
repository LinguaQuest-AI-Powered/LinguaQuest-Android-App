package com.iti.linguaquest.features.auth.presentation.login.viewmodel

import android.util.Patterns
import app.cash.turbine.test
import com.iti.linguaquest.R
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.usecase.CompleteOAuthProfileUseCase
import com.iti.linguaquest.features.auth.domain.usecase.LoginUserUseCase
import com.iti.linguaquest.features.auth.domain.usecase.SignInWithGoogleUseCase
import com.iti.linguaquest.features.auth.presentation.login.contract.LoginEffect
import com.iti.linguaquest.features.auth.presentation.login.contract.LoginIntent
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetNativeLanguageUseCase
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetTargetLanguageUseCase
import com.iti.linguaquest.util.MainDispatcherRule
import com.iti.linguaquest.core.utils.ValidationUtils
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

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val loginUserUseCase: LoginUserUseCase = mockk()
    private val loginWithGoogleUseCase: SignInWithGoogleUseCase = mockk()
    private val snackbarController: SnackbarController = mockk(relaxed = true)
    private val getTargetLanguageUseCase: GetTargetLanguageUseCase = mockk()
    private val getNativeLanguageUseCase: GetNativeLanguageUseCase = mockk()
    private val completeOAuthProfileUseCase: CompleteOAuthProfileUseCase = mockk()
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase = mockk()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        mockkObject(ValidationUtils)
        every { ValidationUtils.isValidEmail(any()) } answers {
            val email = firstArg<String>()
            email.isNotBlank() && email.contains("@") && email.endsWith(".com")
        }
        every { observeNetworkStatusUseCase() } returns flowOf(true)

        viewModel = LoginViewModel(
            loginUserUseCase = loginUserUseCase,
            loginWithGoogleUseCase = loginWithGoogleUseCase,
            snackbarController = snackbarController,
            getTargetLanguageUseCase = getTargetLanguageUseCase,
            getNativeLanguageUseCase = getNativeLanguageUseCase,
            completeOAuthProfileUseCase = completeOAuthProfileUseCase,
            observeNetworkStatusUseCase = observeNetworkStatusUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkObject(ValidationUtils)
    }

    @Test
    fun onIntent_updatesStateWithErrorAndShakesEmail_whenEmailIsBlank() = runTest {
        // Given
        val email = ""
        val password = "Password123"

        // When
        viewModel.onIntent(LoginIntent.LoginClicked(email, password))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.emailError)
            assertEquals(R.string.login_error_email_required, state.emailErrorRes)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(LoginEffect.ShakeEmail, effect)
        }
    }

    @Test
    fun onIntent_updatesStateWithErrorAndShakesEmail_whenEmailIsInvalid() = runTest {
        // Given
        val email = "invalid-email"
        val password = "Password123"

        // When
        viewModel.onIntent(LoginIntent.LoginClicked(email, password))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.emailError)
            assertEquals(R.string.login_error_invalid_email, state.emailErrorRes)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(LoginEffect.ShakeEmail, effect)
        }
    }

    @Test
    fun onIntent_updatesStateWithErrorAndShakesPassword_whenPasswordIsBlank() = runTest {
        // Given
        val email = "test@example.com"
        val password = ""

        // When
        viewModel.onIntent(LoginIntent.LoginClicked(email, password))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.passwordError)
            assertEquals(R.string.login_error_password_required, state.passwordErrorRes)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(LoginEffect.ShakePassword, effect)
        }
    }

    @Test
    fun onIntent_updatesStateWithErrorAndShakesPassword_whenPasswordIsTooShort() = runTest {
        // Given
        val email = "test@example.com"
        val password = "Short1"

        // When
        viewModel.onIntent(LoginIntent.LoginClicked(email, password))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.passwordError)
            assertEquals(R.string.new_password_error_too_short, state.passwordErrorRes)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(LoginEffect.ShakePassword, effect)
        }
    }

    @Test
    fun onIntent_updatesStateWithErrorAndShakesPassword_whenPasswordHasNoUppercase() = runTest {
        // Given
        val email = "test@example.com"
        val password = "lowercase123"

        // When
        viewModel.onIntent(LoginIntent.LoginClicked(email, password))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.passwordError)
            assertEquals(R.string.new_password_error_no_uppercase, state.passwordErrorRes)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(LoginEffect.ShakePassword, effect)
        }
    }

    @Test
    fun onIntent_updatesStateWithErrorAndShakesPassword_whenPasswordHasNoNumber() = runTest {
        // Given
        val email = "test@example.com"
        val password = "NoNumbersHere"

        // When
        viewModel.onIntent(LoginIntent.LoginClicked(email, password))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.passwordError)
            assertEquals(R.string.new_password_error_no_number, state.passwordErrorRes)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(LoginEffect.ShakePassword, effect)
        }
    }

    @Test
    fun onIntent_emitsLoginSucceeded_whenEmailAndPasswordAreValidAndLoginSucceeds() = runTest {
        // Given
        val email = "test@example.com"
        val password = "ValidPassword123"
        coEvery { loginUserUseCase(email, password) } returns LinguaQuestResult.Success(Unit)

        // When
        viewModel.onIntent(LoginIntent.LoginClicked(email, password))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(LoginEffect.LoginSucceeded, effect)
        }
        coVerify(exactly = 1) { loginUserUseCase(email, password) }
    }

    @Test
    fun onIntent_emitsShakeAndUpdatesState_whenLoginFailsWithInvalidCredentials() = runTest {
        // Given
        val email = "test@example.com"
        val password = "ValidPassword123"
        coEvery { loginUserUseCase(email, password) } returns LinguaQuestResult.Failure(AuthError.InvalidCredentials)

        // When
        viewModel.onIntent(LoginIntent.LoginClicked(email, password))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.emailError)
            assertTrue(state.passwordError)
            assertEquals(R.string.login_error_invalid_credentials, state.emailErrorRes)
            assertEquals(R.string.login_error_invalid_credentials, state.passwordErrorRes)
        }
        viewModel.effects.test {
            assertEquals(LoginEffect.ShakeEmail, awaitItem())
            assertEquals(LoginEffect.ShakePassword, awaitItem())
        }
    }

    @Test
    fun onIntent_emitsNavigateToOTP_whenLoginFailsWithEmailNotVerified() = runTest {
        // Given
        val email = "test@example.com"
        val password = "ValidPassword123"
        coEvery { loginUserUseCase(email, password) } returns LinguaQuestResult.Failure(AuthError.EmailNotVerified)

        // When
        viewModel.onIntent(LoginIntent.LoginClicked(email, password))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertFalse(state.emailError)
            assertFalse(state.passwordError)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(LoginEffect.NavigateToOTP(email), effect)
        }
    }

    @Test
    fun onIntent_showsSnackbarAndUpdatesState_whenLoginFailsWithGeneralError() = runTest {
        // Given
        val email = "test@example.com"
        val password = "ValidPassword123"
        coEvery { loginUserUseCase(email, password) } returns LinguaQuestResult.Failure(AuthError.InternalServerError)

        // When
        viewModel.onIntent(LoginIntent.LoginClicked(email, password))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(R.string.error_server, state.generalErrorRes)
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
    fun onIntent_emitsLaunchGoogleSignIn_whenGoogleSignInClicked() = runTest {
        // When
        viewModel.onIntent(LoginIntent.GoogleSignInClicked)

        // Then
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(LoginEffect.LaunchGoogleSignIn, effect)
        }
    }

    @Test
    fun onIntent_emitsShakeAndShowsSnackbar_whenGoogleSignInFailed() = runTest {
        // When
        viewModel.onIntent(LoginIntent.GoogleSignInFailed)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.googleError)
            assertEquals(R.string.login_error_generic, state.generalErrorRes)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(LoginEffect.ShakeGoogleSignIn, effect)
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
    fun onIntent_emitsLoginSucceeded_whenGoogleLoginSucceedsAndProfileIsComplete() = runTest {
        // Given
        val idToken = "google-id-token"
        coEvery { loginWithGoogleUseCase(idToken) } returns LinguaQuestResult.Success(true)

        // When
        viewModel.onIntent(LoginIntent.GoogleLoginSubmitted(idToken))

        // Then
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(LoginEffect.LoginSucceeded, effect)
        }
    }

    @Test
    fun onIntent_completesProfile_whenGoogleLoginSucceedsAndProfileIsIncompleteAndLanguageCached() = runTest {
        // Given
        val idToken = "google-id-token"
        coEvery { loginWithGoogleUseCase(idToken) } returns LinguaQuestResult.Success(false)
        every { getTargetLanguageUseCase() } returns flowOf(2)
        every { getNativeLanguageUseCase() } returns flowOf(1)
        coEvery { completeOAuthProfileUseCase(1, 2, null) } returns LinguaQuestResult.Success(Unit)

        // When
        viewModel.onIntent(LoginIntent.GoogleLoginSubmitted(idToken))

        // Then
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(LoginEffect.LoginSucceeded, effect)
        }
        coVerify(exactly = 1) { completeOAuthProfileUseCase(1, 2, null) }
    }

    @Test
    fun onIntent_emitsNavigateToOAuthLanguageSelection_whenGoogleLoginSucceedsAndProfileIsIncompleteAndNoLanguage() = runTest {
        // Given
        val idToken = "google-id-token"
        coEvery { loginWithGoogleUseCase(idToken) } returns LinguaQuestResult.Success(false)
        every { getTargetLanguageUseCase() } returns flowOf(null)

        // When
        viewModel.onIntent(LoginIntent.GoogleLoginSubmitted(idToken))

        // Then
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(LoginEffect.NavigateToOAuthLanguageSelection, effect)
        }
    }

    @Test
    fun onIntent_emitsNavigateToSignUp_whenSignUpClickedAndTargetLanguageExists() = runTest {
        // Given
        every { getTargetLanguageUseCase() } returns flowOf(2)

        // When
        viewModel.onIntent(LoginIntent.SignUpClicked)

        // Then
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(LoginEffect.NavigateToSignUp, effect)
        }
    }

    @Test
    fun onIntent_emitsNavigateToSignUpWithoutLanguages_whenSignUpClickedAndTargetLanguageIsNull() = runTest {
        // Given
        every { getTargetLanguageUseCase() } returns flowOf(null)

        // When
        viewModel.onIntent(LoginIntent.SignUpClicked)

        // Then
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(LoginEffect.NavigateToSignUpWithoutLanguages, effect)
        }
    }

    @Test
    fun onIntent_emitsNavigateToForgotPassword_whenForgetPasswordClicked() = runTest {
        // When
        viewModel.onIntent(LoginIntent.ForgetPasswordClicked)

        // Then
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(LoginEffect.NavigateToForgotPassword, effect)
        }
    }

    @Test
    fun onIntent_emitsLoginSucceeded_whenOAuthLanguageSelectionCompletedAndProfileCompletesSuccessfully() = runTest {
        // Given
        every { getTargetLanguageUseCase() } returns flowOf(2)
        every { getNativeLanguageUseCase() } returns flowOf(1)
        coEvery { completeOAuthProfileUseCase(1, 2, null) } returns LinguaQuestResult.Success(Unit)

        // When
        viewModel.onIntent(LoginIntent.OAuthLanguageSelectionCompleted)

        // Then
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(LoginEffect.LoginSucceeded, effect)
        }
        coVerify(exactly = 1) { completeOAuthProfileUseCase(1, 2, null) }
    }

    @Test
    fun onIntent_showsSnackbarAndUpdatesState_whenOAuthLanguageSelectionCompletedAndProfileCompletesWithFailure() = runTest {
        // Given
        every { getTargetLanguageUseCase() } returns flowOf(2)
        every { getNativeLanguageUseCase() } returns flowOf(1)
        coEvery { completeOAuthProfileUseCase(1, 2, null) } returns LinguaQuestResult.Failure(AuthError.InternalServerError)

        // When
        viewModel.onIntent(LoginIntent.OAuthLanguageSelectionCompleted)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(R.string.error_server, state.generalErrorRes)
        }
        coVerify(exactly = 1) {
            snackbarController.sendEvent(
                match { event ->
                    event.type == SnackbarType.ERROR
                }
            )
        }
    }
}
