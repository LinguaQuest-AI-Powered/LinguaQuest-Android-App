
package com.iti.linguaquest.core.navigation
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import com.iti.linguaquest.features.onBoarding.view.LanguagesScreen
import com.iti.linguaquest.features.onBoarding.view.LevelScreen
import com.iti.linguaquest.features.onBoarding.view.LinguaQuestSplashScreen
import com.iti.linguaquest.features.onBoarding.view.OnboardingScreen
import kotlinx.coroutines.delay
import com.iti.linguaquest.features.auth.presentation.login.view.LoginScreen
import com.iti.linguaquest.features.auth.presentation.signup.view.SignUpScreen
import com.iti.linguaquest.features.auth.presentation.forgetpassword.view.ForgetPasswordScreen
import com.iti.linguaquest.features.auth.presentation.newpassword.view.NewPasswordScreen
import com.iti.linguaquest.features.auth.presentation.otp.view.screen.OTPScreen
import kotlin.time.Duration.Companion.milliseconds

class RootNavigator {

    val backStack = mutableStateListOf<RootScreen>(RootScreen.Splash)
    fun navigateTo(screen: RootScreen) {
        backStack.add(screen)
    }

    fun popBackStack() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.size - 1)
        }
    }
}


@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val rootNavigator = remember { RootNavigator() }

    NavDisplay(
        backStack = rootNavigator.backStack,
        modifier = modifier.fillMaxSize(),
        onBack = { rootNavigator.popBackStack() },
        transitionSpec = {
            slideInHorizontally(
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioNoBouncy
                ),
                initialOffsetX = { fullWidth -> fullWidth }
            ) + fadeIn(animationSpec = tween(300)) togetherWith
                    slideOutHorizontally(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioNoBouncy
                        ),
                        targetOffsetX = { fullWidth -> -fullWidth }
                    ) + fadeOut(animationSpec = tween(300))
        },
        popTransitionSpec = {
            slideInHorizontally(
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioNoBouncy
                ),
                initialOffsetX = { fullWidth -> -fullWidth }
            ) + fadeIn(animationSpec = tween(300)) togetherWith
                    slideOutHorizontally(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioNoBouncy
                        ),
                        targetOffsetX = { fullWidth -> fullWidth }
                    ) + fadeOut(animationSpec = tween(300))
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<RootScreen.Splash> {
                LinguaQuestSplashScreen()

                LaunchedEffect(Unit) {
                    delay(2000.milliseconds)
                    rootNavigator.navigateTo(RootScreen.Onboarding)
                }
            }

            entry<RootScreen.Onboarding> {
                OnboardingScreen(
                    onGetStartedClick = {
                        rootNavigator.navigateTo(RootScreen.Languages)
                    },
                    onLoginClick = {
                        rootNavigator.navigateTo(RootScreen.Login)
                    }
                )
            }

            entry<RootScreen.Languages> {
                LanguagesScreen(
                    onContinue = {
                        rootNavigator.navigateTo(RootScreen.Level)
                    }
                )
            }

            entry<RootScreen.Level> {
                LevelScreen(
                    onContinue = {
                        rootNavigator.navigateTo(RootScreen.Login)
                    }
                )
            }

            entry<RootScreen.Login> {
                LoginScreen(
                    onSignUp = {
                        rootNavigator.navigateTo(RootScreen.SignUp)
                    },
                    onForgotPassword = {
                        rootNavigator.navigateTo(RootScreen.ForgotPassword)
                    },
                    onLoginSuccess = {
                        rootNavigator.navigateTo(RootScreen.Main)
                    }
                )
            }

            entry<RootScreen.SignUp> {
                SignUpScreen(
                    onNavigateToLogin = { rootNavigator.popBackStack() },
                    onSignUpSuccess = { email -> rootNavigator.navigateTo(RootScreen.OTP(email, false)) }
                )
            }

            entry<RootScreen.ForgotPassword> {
                ForgetPasswordScreen(
                    onBackToLogin = { rootNavigator.popBackStack() },
                    onSendSucceeded = { email -> rootNavigator.navigateTo(RootScreen.OTP(email, true)) }
                )
            }

            entry<RootScreen.OTP> { screen ->
                OTPScreen(
                    email = screen.email,
                    isPasswordReset = screen.isPasswordReset,
                    onNavigateBack = { rootNavigator.popBackStack() },
                    onNavigateToLogin = { rootNavigator.navigateTo(RootScreen.Login) },
                    onNavigateToNext = { resetToken ->
                        if (screen.isPasswordReset && resetToken != null) {
                            rootNavigator.navigateTo(RootScreen.NewPassword(resetToken))
                        } else {
                            rootNavigator.navigateTo(RootScreen.Login)
                        }
                    }
                )
            }

            entry<RootScreen.NewPassword> { screen ->
                NewPasswordScreen(
                    resetToken = screen.resetToken,
                    onBackToLogin = { rootNavigator.popBackStack() },
                    onResetSuccess = { rootNavigator.navigateTo(RootScreen.Login) }
                )
            }

            entry<RootScreen.Main> {
                MainScreen(rootNavigator)
            }
        })
}