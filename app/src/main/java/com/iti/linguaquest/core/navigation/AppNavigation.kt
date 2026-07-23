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
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import com.iti.linguaquest.features.onBoarding.presentation.view.LanguagesScreen
import com.iti.linguaquest.features.onBoarding.presentation.view.LinguaQuestSplashScreen
import com.iti.linguaquest.features.onBoarding.presentation.view.OnboardingScreen
import kotlinx.coroutines.delay
import com.iti.linguaquest.features.auth.presentation.login.view.LoginScreen
import com.iti.linguaquest.features.auth.presentation.signup.view.SignUpScreen
import com.iti.linguaquest.features.auth.presentation.forgetpassword.view.ForgetPasswordScreen
import com.iti.linguaquest.features.auth.presentation.newpassword.view.NewPasswordScreen
import kotlin.time.Duration.Companion.milliseconds
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.rememberNavBackStack
import kotlinx.coroutines.flow.collectLatest
import com.iti.linguaquest.core.sharedComponents.GlobalUiHostViewModel
import com.iti.linguaquest.core.sharedComponents.dialog.GlobalDialogHost
import com.iti.linguaquest.core.sharedComponents.snackbar.AppSnackbarHost
import com.iti.linguaquest.core.sharedComponents.snackbar.AppSnackbarVisuals
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.features.achivement.AchievementScreen
import com.iti.linguaquest.features.all_worlds.presentation.view.AllWorldsScreen
import com.iti.linguaquest.features.auth.presentation.otp.view.screen.OTPScreen
import com.iti.linguaquest.features.editprofile.presentation.EditProfileScreen
import com.iti.linguaquest.features.map.presentation.MapScreen
import com.iti.linguaquest.features.game.presentation.GameFlowHost
import com.iti.linguaquest.features.home.presentation.languages.view.AddLanguagesScreen
import com.iti.linguaquest.features.leaderboard.presentation.LeaderboardScreen
import com.iti.linguaquest.features.onBoarding.presentation.view.LevelScreen
import com.iti.linguaquest.features.review.presentation.view.ReviewScreen
import com.iti.linguaquest.features.voicegame.presentation.view.VoiceResultScreen
import com.iti.linguaquest.features.voicegame.presentation.view.VoiceGameScreen
import com.iti.linguaquest.features.setting.presentation.SettingScreen


@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    globalUiHostViewModel: GlobalUiHostViewModel = hiltViewModel()
) {
    val soundPlayer = LocalSoundPlayer.current
    val rootBackStack = rememberNavBackStack(RootScreen.Splash)
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        globalUiHostViewModel.snackbarController.events.collectLatest { event ->

            soundPlayer.play(AppSound.POP)

            snackbarHostState.currentSnackbarData?.dismiss()
            val result = snackbarHostState.showSnackbar(
                AppSnackbarVisuals(
                    message = event.message.asString(context),
                    title = event.title?.asString(context),
                    actionLabel = event.actionLabel?.asString(context),
                    duration = event.duration,
                    type = event.type,
                    showCloseIcon = event.showCloseIcon,
                    icon = event.icon
                )
            )
            if (result == SnackbarResult.ActionPerformed) event.onAction?.invoke()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            val density = LocalDensity.current
            val bottomBarHeightPx = SharedBottomBarState.heightPx
            val bottomBarHeightDp = with(density) { bottomBarHeightPx.toDp() }

            AppSnackbarHost(
                hostState = snackbarHostState,
                modifier = if (bottomBarHeightPx > 0) {
                    Modifier.padding(bottom = bottomBarHeightDp)
                } else {
                    Modifier.navigationBarsPadding()
                }
            )
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = rootBackStack,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
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
                    val splashViewModel: com.iti.linguaquest.features.onBoarding.presentation.viewModel.splashViewModel.SplashViewModel = hiltViewModel()
                    val destination by splashViewModel.destination.collectAsState()

                    LinguaQuestSplashScreen()

                    LaunchedEffect(destination) {
                        destination?.let { dest ->
                            delay(2000.milliseconds)
                            rootBackStack.apply {
                                clear()
                                navigateSingleTop(dest)
                            }
                        }
                    }
                }

                entry<RootScreen.Onboarding> {
                    OnboardingScreen(
                        onGetStartedClick = {
                            rootBackStack.navigateSingleTop(RootScreen.Languages())
                        },
                        onLoginClick = {
                            rootBackStack.navigateSingleTop(RootScreen.Login())
                        }
                    )
                }

                entry<RootScreen.Languages> { screen ->
                    LanguagesScreen(
                        onContinue = {
                            rootBackStack.navigateSingleTop(RootScreen.OnboardingLevel(flow = screen.flow))
                        }
                    )
                }

                entry<RootScreen.OnboardingLevel> { screen ->
                    LevelScreen(
                        onContinue = {
                            when (screen.flow) {
                                "SIGN_UP" -> rootBackStack.navigateSingleTop(RootScreen.SignUp())
                                "OAUTH" -> rootBackStack.navigateSingleTop(RootScreen.Login(isOAuthLanguageSelectionCompleted = true))
                                "OAUTH_SIGNUP" -> rootBackStack.navigateSingleTop(RootScreen.SignUp(isOAuthLanguageSelectionCompleted = true))
                                else -> rootBackStack.navigateSingleTop(RootScreen.Login())
                            }
                        }
                    )
                }

                entry<RootScreen.Login> { screen ->
                    LoginScreen(
                        isOAuthLanguageSelectionCompleted = screen.isOAuthLanguageSelectionCompleted,
                        onSignUp = {
                            rootBackStack.navigateSingleTop(RootScreen.SignUp())
                        },
                        onSignUpWithoutLanguages = {
                            rootBackStack.navigateSingleTop(RootScreen.Languages(flow = "SIGN_UP"))
                        },
                        onForgotPassword = {
                            rootBackStack.navigateSingleTop(RootScreen.ForgotPassword)
                        },
                        onOAuthLanguageSelection = {
                            rootBackStack.navigateSingleTop(RootScreen.Languages(flow = "OAUTH"))
                        },
                        onLoginSuccess = {
                            rootBackStack.apply {
                                clear()
                                navigateSingleTop(RootScreen.Main)
                            }
                        }
                    )
                }

                entry<RootScreen.SignUp> { screen ->
                    SignUpScreen(
                        isOAuthLanguageSelectionCompleted = screen.isOAuthLanguageSelectionCompleted,
                        onNavigateToLogin = { rootBackStack.popToLogin() },
                        onNavigateToMain = {
                            rootBackStack.apply {
                                clear()
                                navigateSingleTop(RootScreen.Main)
                            }
                        },
                        onOAuthLanguageSelection = {
                            rootBackStack.navigateSingleTop(RootScreen.Languages(flow = "OAUTH_SIGNUP"))
                        },
                        onSignUpSuccess = { email ->
                            rootBackStack.navigateSingleTop(RootScreen.OTP(email, false))
                        }
                    )
                }



                entry<RootScreen.ForgotPassword> {
                    ForgetPasswordScreen(
                        onBackToLogin = { rootBackStack.popToLogin() },
                        onSendSucceeded = {
                            rootBackStack.navigateSingleTop(
                                RootScreen.OTP(
                                    it,
                                    true
                                )
                            )
                        }
                    )
                }
                entry<RootScreen.OTP> { screen ->
                    OTPScreen(
                        email = screen.email,
                        isPasswordReset = screen.isPasswordReset,
                        onNavigateBack = { rootBackStack.removeLastOrNull() },
                        onNavigateToLogin = { rootBackStack.popToLogin() },
                        onNavigateToNext = { resetToken ->
                            if (screen.isPasswordReset && resetToken != null) {
                                rootBackStack.navigateSingleTop(RootScreen.NewPassword(resetToken))
                            } else {
                                rootBackStack.popToLogin()
                            }
                        }
                    )
                }

                entry<RootScreen.NewPassword> { screen ->
                    NewPasswordScreen(
                        onBackToLogin = { rootBackStack.popToLogin() },
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

                entry<RootScreen.Map> { screen ->
                    MapScreen(
                        worldId = screen.worldId,
                        onBack = { rootBackStack.removeLastOrNull() },
                        onNavigateToLevel = { levelNum ->
                            rootBackStack.navigateSingleTop(
                                RootScreen.GameFlow(
                                    worldId = screen.worldId,
                                    levelNumber = levelNum
                                )
                            )
                        }
                    )
                }

                entry<RootScreen.GameFlow> { screen ->
                    GameFlowHost(
                        worldId = screen.worldId,
                        levelNumber = screen.levelNumber,
                        rootBackStack = rootBackStack
                    )
                }
                entry<RootScreen.Review> { key ->
                    val word = SharedWordHolder.pendingWord
                    if (word != null) {
                        SharedWordHolder.pendingWord = null
                        ReviewScreen(
                            word = word,
                            onBack = { rootBackStack.removeLastOrNull() }
                        )
                    } else {
                        LaunchedEffect(Unit) { rootBackStack.removeLastOrNull() }
                    }
                }
                entry<RootScreen.VoiceGame> { screen ->
                    VoiceGameScreen(
                        sentence = screen.sentence,
                        lessonId = screen.lessonId,
                        onNavigateBack = { rootBackStack.removeLastOrNull() },
                        onEvaluationComplete = { result ->
                            SharedVoiceResultHolder.pendingResult = result
                            rootBackStack.navigateSingleTop(RootScreen.VoiceResult)
                        }
                    )
                }

                entry<RootScreen.VoiceResult> {
                    val result = SharedVoiceResultHolder.pendingResult
                    if (result != null) {
                        VoiceResultScreen(
                            result = result,
                            onContinue = {
                                SharedVoiceResultHolder.pendingResult = null
                                SharedVoiceResultHolder.autoGenerateNextSentence = true
                                rootBackStack.removeLastOrNull()
                            },
                            onRetry = {
                                SharedVoiceResultHolder.pendingResult = null
                                rootBackStack.removeLastOrNull()
                            },
                            onHome = {
                                SharedVoiceResultHolder.pendingResult = null
                                rootBackStack.apply { clear(); navigateSingleTop(RootScreen.Main) }
                            }
                        )
                    } else {
                        LaunchedEffect(Unit) { rootBackStack.removeLastOrNull() }
                    }
                }

                entry<RootScreen.Settings> {
                    SettingScreen(
                        onBack = { rootBackStack.removeLastOrNull() },
                        onEdit = {
                            rootBackStack.navigateSingleTop(RootScreen.EditProfile)
                        },
                        onLogout = {
                            rootBackStack.apply {
                                clear()
                                navigateSingleTop(RootScreen.Onboarding)
                            }
                        }
                    )
                }

                entry<RootScreen.Leaderboard> {

                    LeaderboardScreen(
                        onBack = { rootBackStack.removeLastOrNull() })}

                entry<RootScreen.AllWorlds> {
                    AllWorldsScreen(
                        onNavigateBack = { rootBackStack.removeLastOrNull() },
                        onNavigateToWorldDetails = { worldId ->
                            rootBackStack.navigateSingleTop(RootScreen.Map(worldId))
                        }
                    )
                }

                entry<RootScreen.Roleplay> {
                    com.iti.linguaquest.features.roleplay.presentation.view.RoleplayScreen(
                        onNavigateHome = { rootBackStack.removeLastOrNull() }
                    )
                }

                entry<RootScreen.AddLanguages> {
                    AddLanguagesScreen(
                        onNavigateBack = { rootBackStack.removeLastOrNull() }
                    )
                }

                entry<RootScreen.Achievement> {
                    AchievementScreen (
                        onBackClick = { rootBackStack.removeLastOrNull() },
                     )

                }
                entry<RootScreen.EditProfile> {
                    EditProfileScreen(
                         initialDisplayName = "",
                        initialTagline = "",
                        avatarModel = null,
                        onBackClick = { rootBackStack.removeLastOrNull() },
                        onChangePhotoClick = {    },
                        onSave = { displayName, tagline ->
                             rootBackStack.removeLastOrNull()
                        }
                    )
                }
                GlobalDialogHost(globalUiHostViewModel.dialogController)
            })
    }
}