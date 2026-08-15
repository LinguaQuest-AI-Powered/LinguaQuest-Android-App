package com.iti.linguaquest.features.auth.presentation.signup.viewmodel

import app.cash.turbine.test
import com.iti.linguaquest.R
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.utils.ValidationUtils
import com.iti.linguaquest.features.auth.domain.model.AuthError
import com.iti.linguaquest.features.auth.domain.usecase.CompleteOAuthProfileUseCase
import com.iti.linguaquest.features.auth.domain.usecase.RegisterUserUseCase
import com.iti.linguaquest.features.auth.domain.usecase.SignInWithGoogleUseCase
import com.iti.linguaquest.features.auth.presentation.signup.contract.SignUpEffect
import com.iti.linguaquest.features.auth.presentation.signup.contract.SignUpIntent
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetNativeLanguageUseCase
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetTargetLanguageUseCase
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

class SignUpViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val signUpWithEmailUseCase: RegisterUserUseCase = mockk()
    private val loginWithGoogleUseCase: SignInWithGoogleUseCase = mockk()
    private val getTargetLanguageUseCase: GetTargetLanguageUseCase = mockk()
    private val getNativeLanguageUseCase: GetNativeLanguageUseCase = mockk()
    private val completeOAuthProfileUseCase: CompleteOAuthProfileUseCase = mockk()
    private val snackbarController: SnackbarController = mockk(relaxed = true)
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase = mockk()

    private lateinit var viewModel: SignUpViewModel

    @Before
    fun setup() {
        mockkObject(ValidationUtils)
        every { ValidationUtils.isValidEmail(any()) } answers {
            val email = firstArg<String>()
            email.isNotBlank() && email.contains("@") && email.endsWith(".com")
        }
        every { observeNetworkStatusUseCase() } returns flowOf(true)

        viewModel = SignUpViewModel(
            signUpWithEmailUseCase = signUpWithEmailUseCase,
            loginWithGoogleUseCase = loginWithGoogleUseCase,
            getTargetLanguageUseCase = getTargetLanguageUseCase,
            getNativeLanguageUseCase = getNativeLanguageUseCase,
            completeOAuthProfileUseCase = completeOAuthProfileUseCase,
            snackbarController = snackbarController,
            observeNetworkStatusUseCase = observeNetworkStatusUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkObject(ValidationUtils)
    }

    @Test
    fun onIntent_updatesStateWithErrorAndShakesUsername_whenUsernameIsBlank() = runTest {
        // Given
        val username = ""
        val email = "test@example.com"
        val password = "Password123"
        val confirmPassword = "Password123"

        // When
        viewModel.onIntent(SignUpIntent.SignUpClicked(username, email, password, confirmPassword))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.usernameError)
            assertEquals(R.string.signup_error_name_required, state.usernameErrorRes)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(SignUpEffect.ShakeUsername, effect)
        }
    }

    @Test
    fun onIntent_updatesStateWithErrorAndShakesUsername_whenUsernameIsTooShort() = runTest {
        // Given
        val username = "a"
        val email = "test@example.com"
        val password = "Password123"
        val confirmPassword = "Password123"

        // When
        viewModel.onIntent(SignUpIntent.SignUpClicked(username, email, password, confirmPassword))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.usernameError)
            assertEquals(R.string.signup_error_name_too_short, state.usernameErrorRes)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(SignUpEffect.ShakeUsername, effect)
        }
    }

    @Test
    fun onIntent_updatesStateWithErrorAndShakesEmail_whenEmailIsBlank() = runTest {
        // Given
        val username = "username"
        val email = ""
        val password = "Password123"
        val confirmPassword = "Password123"

        // When
        viewModel.onIntent(SignUpIntent.SignUpClicked(username, email, password, confirmPassword))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.emailError)
            assertEquals(R.string.login_error_email_required, state.emailErrorRes)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(SignUpEffect.ShakeEmail, effect)
        }
    }

    @Test
    fun onIntent_updatesStateWithErrorAndShakesEmail_whenEmailIsInvalid() = runTest {
        // Given
        val username = "username"
        val email = "invalid-email"
        val password = "Password123"
        val confirmPassword = "Password123"

        // When
        viewModel.onIntent(SignUpIntent.SignUpClicked(username, email, password, confirmPassword))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.emailError)
            assertEquals(R.string.login_error_invalid_email, state.emailErrorRes)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(SignUpEffect.ShakeEmail, effect)
        }
    }

    @Test
    fun onIntent_updatesStateWithErrorAndShakesPassword_whenPasswordIsBlank() = runTest {
        // Given
        val username = "username"
        val email = "test@example.com"
        val password = ""
        val confirmPassword = ""

        // When
        viewModel.onIntent(SignUpIntent.SignUpClicked(username, email, password, confirmPassword))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.passwordError)
            assertEquals(R.string.login_error_password_required, state.passwordErrorRes)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(SignUpEffect.ShakePassword, effect)
        }
    }

    @Test
    fun onIntent_updatesStateWithErrorAndShakesConfirmPassword_whenPasswordsDoNotMatch() = runTest {
        // Given
        val username = "username"
        val email = "test@example.com"
        val password = "Password123"
        val confirmPassword = "DifferentPassword123"

        // When
        viewModel.onIntent(SignUpIntent.SignUpClicked(username, email, password, confirmPassword))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.confirmPasswordError)
            assertEquals(R.string.signup_error_passwords_do_not_match, state.confirmPasswordErrorRes)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(SignUpEffect.ShakeConfirmPassword, effect)
        }
    }

    @Test
    fun onIntent_emitsSignUpSucceeded_whenAllFieldsAreValidAndSignUpSucceeds() = runTest {
        // Given
        val username = "username"
        val email = "test@example.com"
        val password = "Password123"
        val confirmPassword = "Password123"
        coEvery { signUpWithEmailUseCase(email, username, password) } returns LinguaQuestResult.Success(Unit)

        // When
        viewModel.onIntent(SignUpIntent.SignUpClicked(username, email, password, confirmPassword))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(SignUpEffect.SignUpSucceeded(email), effect)
        }
        coVerify(exactly = 1) { signUpWithEmailUseCase(email, username, password) }
    }

    @Test
    fun onIntent_emitsShakeAndUpdatesState_whenSignUpFailsWithValidationError() = runTest {
        // Given
        val username = "username"
        val email = "test@example.com"
        val password = "Password123"
        val confirmPassword = "Password123"
        coEvery { signUpWithEmailUseCase(email, username, password) } returns LinguaQuestResult.Failure(AuthError.InvalidEmail)

        // When
        viewModel.onIntent(SignUpIntent.SignUpClicked(username, email, password, confirmPassword))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.emailError)
            assertEquals(R.string.login_error_invalid_email, state.emailErrorRes)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(SignUpEffect.ShakeEmail, effect)
        }
    }

    @Test
    fun onIntent_showsSnackbarAndUpdatesState_whenSignUpFailsWithGeneralError() = runTest {
        // Given
        val username = "username"
        val email = "test@example.com"
        val password = "Password123"
        val confirmPassword = "Password123"
        coEvery { signUpWithEmailUseCase(email, username, password) } returns LinguaQuestResult.Failure(AuthError.InternalServerError)

        // When
        viewModel.onIntent(SignUpIntent.SignUpClicked(username, email, password, confirmPassword))

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
        viewModel.onIntent(SignUpIntent.GoogleSignInClicked)

        // Then
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(SignUpEffect.LaunchGoogleSignIn, effect)
        }
    }

    @Test
    fun onIntent_emitsShakeAndShowsSnackbar_whenGoogleSignInFailed() = runTest {
        // When
        viewModel.onIntent(SignUpIntent.GoogleSignInFailed)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.googleError)
            assertEquals(R.string.login_error_generic, state.generalErrorRes)
        }
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(SignUpEffect.ShakeGoogleSignIn, effect)
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
    fun onIntent_emitsNavigateToMain_whenGoogleLoginSucceedsAndProfileIsComplete() = runTest {
        // Given
        val idToken = "google-id-token"
        coEvery { loginWithGoogleUseCase(idToken) } returns LinguaQuestResult.Success(true)

        // When
        viewModel.onIntent(SignUpIntent.GoogleLoginSubmitted(idToken))

        // Then
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(SignUpEffect.NavigateToMain, effect)
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
        viewModel.onIntent(SignUpIntent.GoogleLoginSubmitted(idToken))

        // Then
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(SignUpEffect.NavigateToMain, effect)
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
        viewModel.onIntent(SignUpIntent.GoogleLoginSubmitted(idToken))

        // Then
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(SignUpEffect.NavigateToOAuthLanguageSelection, effect)
        }
    }

    @Test
    fun onIntent_emitsNavigateToLogin_whenLoginClicked() = runTest {
        // When
        viewModel.onIntent(SignUpIntent.LoginClicked)

        // Then
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(SignUpEffect.NavigateToLogin, effect)
        }
    }

    @Test
    fun onIntent_emitsNavigateToMain_whenOAuthLanguageSelectionCompletedAndProfileCompletesSuccessfully() = runTest {
        // Given
        every { getTargetLanguageUseCase() } returns flowOf(2)
        every { getNativeLanguageUseCase() } returns flowOf(1)
        coEvery { completeOAuthProfileUseCase(1, 2, null) } returns LinguaQuestResult.Success(Unit)

        // When
        viewModel.onIntent(SignUpIntent.OAuthLanguageSelectionCompleted)

        // Then
        viewModel.effects.test {
            val effect = awaitItem()
            assertEquals(SignUpEffect.NavigateToMain, effect)
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
        viewModel.onIntent(SignUpIntent.OAuthLanguageSelectionCompleted)

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
