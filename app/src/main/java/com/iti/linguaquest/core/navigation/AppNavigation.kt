
package com.iti.linguaquest.core.navigation
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R
import com.iti.linguaquest.features.auth.presentation.forgetpassword.view.ForgetPasswordScreen
import com.iti.linguaquest.features.auth.presentation.newpassword.view.NewPasswordScreen
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
                        // User already has an account
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
                    onNavigateToLogin = { rootNavigator.popBackStack() }, // Assuming login is right behind signup in stack
                    onSignUpSuccess = { rootNavigator.navigateTo(RootScreen.Main) }
                )
            }

            entry<RootScreen.ForgotPassword> {
                ForgetPasswordScreen(
                    onBackToLogin = { rootNavigator.popBackStack() },
                    onSendSucceeded = { rootNavigator.popBackStack() }
                )
            }
            entry<RootScreen.NewPassword> {
                NewPasswordScreen(
                    onBackToLogin = { rootNavigator.popBackStack() },
                    onResetSuccess = { rootNavigator.navigateTo(RootScreen.Main) }
                )
            }

            entry<RootScreen.Main> {
                MainScreen(rootNavigator)
            }

            entry<RootScreen.Details> { key ->
                DetailsScreen(
                    id = key.id,
                    onBack = {
                        rootNavigator.popBackStack()
                    }
                )
            }
        })
}


@Composable
fun DetailsScreen(id: Int, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = stringResource(R.string.details_screen_id, id))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text(stringResource(R.string.pop_screen))
        }
    }
}