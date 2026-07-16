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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import com.iti.linguaquest.core.sharedComponents.GlobalUiHostViewModel
import com.iti.linguaquest.core.sharedComponents.dialog.GlobalDialogHost
import com.iti.linguaquest.core.sharedComponents.snackbar.AppSnackbarHost
import com.iti.linguaquest.core.sharedComponents.snackbar.AppSnackbarVisuals
import com.iti.linguaquest.features.auth.presentation.otp.view.screen.OTPScreen
import androidx.navigation3.runtime.rememberNavBackStack
import com.iti.linguaquest.features.game.presentation.GameFlowHost

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    globalUiHostViewModel: GlobalUiHostViewModel = hiltViewModel()
) {
    val rootBackStack = rememberNavBackStack(RootScreen.GameFlow(levelId = 1))
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        globalUiHostViewModel.snackbarController.events.collectLatest { event ->
            snackbarHostState.currentSnackbarData?.dismiss()
            val result = snackbarHostState.showSnackbar(
                AppSnackbarVisuals(
                    message = event.message.asString(context),
                    actionLabel = event.actionLabel?.asString(context),
                    duration = event.duration,
                    type = event.type
                )
            )
            if (result == SnackbarResult.ActionPerformed) event.onAction?.invoke()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            AppSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.navigationBarsPadding()
            )
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = rootBackStack,
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            onBack = { rootBackStack.removeLastOrNull() },
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
                        rootBackStack.apply {
                            clear()
                            navigateSingleTop(RootScreen.Onboarding)
                        }
                    }
                }

                entry<RootScreen.Onboarding> {
                    OnboardingScreen(
                        onGetStartedClick = {
                            rootBackStack.navigateSingleTop(RootScreen.Languages)
                        },
                        onLoginClick = {
                            rootBackStack.navigateSingleTop(RootScreen.Login)
                        }
                    )
                }

                entry<RootScreen.Languages> {
                    LanguagesScreen(
                        onContinue = {
                            rootBackStack.navigateSingleTop(RootScreen.Level)
                        }
                    )
                }

                entry<RootScreen.Level> {
                    LevelScreen(
                        onContinue = {
                            rootBackStack.navigateSingleTop(RootScreen.Login)
                        }
                    )
                }

                entry<RootScreen.Login> {
                    LoginScreen(
                        onSignUp = {
                            rootBackStack.navigateSingleTop(RootScreen.Main)
                        },
                        onForgotPassword = {
                            rootBackStack.navigateSingleTop(RootScreen.ForgotPassword)
                        },
                        onLoginSuccess = {
                            rootBackStack.apply {
                                clear()
                                navigateSingleTop(RootScreen.Main)
                            }
                        }
                    )
                }

                entry<RootScreen.SignUp> {
                    SignUpScreen(
                        onNavigateToLogin = { rootBackStack.removeLastOrNull() },
                        onSignUpSuccess = {
                            rootBackStack.apply {
                                clear()
                                navigateSingleTop(RootScreen.Main)
                            }
                        }
                    )
                }

                entry<RootScreen.ForgotPassword> {
                    ForgetPasswordScreen(
                        onBackToLogin = { rootBackStack.removeLastOrNull() },
                        onSendSucceeded = { rootBackStack.removeLastOrNull() }
                    )
                }
                entry<RootScreen.OTP> { screen ->
                    OTPScreen(
                        email = screen.email,
                        isPasswordReset = screen.isPasswordReset,
                        onNavigateBack = { rootBackStack.removeLastOrNull() },
                        onNavigateToLogin = { rootBackStack.navigateSingleTop(RootScreen.Login) },
                        onNavigateToNext = { resetToken ->
                            if (screen.isPasswordReset && resetToken != null) {
                                rootBackStack.navigateSingleTop(RootScreen.NewPassword(resetToken))
                            } else {
                                rootBackStack.navigateSingleTop(RootScreen.Login)
                            }
                        }
                    )
                }

                entry<RootScreen.NewPassword> { screen ->
                    NewPasswordScreen(
                        onBackToLogin = { rootBackStack.removeLastOrNull() },
                        onResetSuccess = {
                            rootBackStack.apply {
                                clear()
                                navigateSingleTop(RootScreen.Main)
                            }
                        },
                        resetToken = screen.resetToken
                    )
                }

                entry<RootScreen.Main> {
                    MainScreen(rootBackStack)
                }

                entry<RootScreen.Details> { key ->
                    DetailsScreen(
                        id = key.id,
                        onBack = {
                            rootBackStack.removeLastOrNull()
                        }
                    )
                }

                entry<RootScreen.GameFlow> { screen ->
                    GameFlowHost(
                        levelId = screen.levelId,
                        rootBackStack = rootBackStack
                    )
                }
            })
    }

    GlobalDialogHost(globalUiHostViewModel.dialogController)
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