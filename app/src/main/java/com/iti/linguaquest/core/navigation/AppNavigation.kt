package com.iti.linguaquest.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import com.iti.linguaquest.features.notification.presentation.view.NotificationScreen
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
import com.iti.linguaquest.R
import kotlinx.coroutines.flow.collectLatest
import com.iti.linguaquest.core.session.SessionEvent
import com.iti.linguaquest.core.sharedComponents.GlobalUiHostViewModel
import com.iti.linguaquest.core.sharedComponents.dialog.GlobalDialogHost
import com.iti.linguaquest.core.sharedComponents.snackbar.AppSnackbarHost
import com.iti.linguaquest.core.sharedComponents.snackbar.AppSnackbarVisuals
import com.iti.linguaquest.core.sharedComponents.InAppNotificationBanner
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.features.achivement.presentation.view.AchievementScreen
import com.iti.linguaquest.features.all_worlds.presentation.view.AllWorldsScreen
import com.iti.linguaquest.features.auth.presentation.otp.view.screen.OTPScreen
import com.iti.linguaquest.features.map.presentation.MapScreen
import com.iti.linguaquest.features.game.presentation.GameFlowHost
import com.iti.linguaquest.features.help.presentation.help.view.HelpScreen
import com.iti.linguaquest.features.home.presentation.languages.view.AddLanguagesScreen
import com.iti.linguaquest.features.leaderboard.presentation.view.LeaderboardScreen
import com.iti.linguaquest.features.lockscreen.presentation.view.LockScreenSettingsScreen
import com.iti.linguaquest.features.lockscreen.presentation.view.LockScreenWordDetailScreen
import com.iti.linguaquest.features.onBoarding.presentation.view.LevelScreen
import com.iti.linguaquest.features.profile.presentation.editprofile.view.EditProfileScreen
import com.iti.linguaquest.features.review.presentation.view.ReviewScreen
import com.iti.linguaquest.features.roleplay.presentation.view.RoleplayScreen
import com.iti.linguaquest.features.mindreader.presentation.view.MindReaderScreen
import com.iti.linguaquest.features.onBoarding.presentation.viewModel.splashViewModel.SplashViewModel
import com.iti.linguaquest.features.roleplay.presentation.viewModel.RoleplayViewModel
import com.iti.linguaquest.features.voicegame.presentation.view.VoiceResultScreen
import com.iti.linguaquest.features.voicegame.presentation.view.VoiceGameScreen
import com.iti.linguaquest.features.setting.presentation.SettingScreen
import com.iti.linguaquest.features.setting.presentation.about_app.AboutAppScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    openHomeRequested: Boolean = false,
    openLockScreenWordId: Int? = null,
    onOpenHomeHandled: () -> Unit = {},
    onOpenLockScreenWordHandled: () -> Unit = {},
    modifier: Modifier = Modifier,
    globalUiHostViewModel: GlobalUiHostViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val soundPlayer = LocalSoundPlayer.current
    val rootBackStack = rememberNavBackStack(RootScreen.Splash)
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(openHomeRequested) {
        if (openHomeRequested) {
            rootBackStack.apply {
                clear()
                navigateSingleTop(RootScreen.Main)
            }
            onOpenHomeHandled()
        }
    }

    LaunchedEffect(openLockScreenWordId) {
        val wordId = openLockScreenWordId ?: return@LaunchedEffect
        rootBackStack.apply {
            clear()
            navigateSingleTop(RootScreen.LockScreenWordDetail(wordId))
        }
        onOpenLockScreenWordHandled()
    }

    LaunchedEffect(Unit) {
        globalUiHostViewModel.sessionEventBus.events.collectLatest { event ->
            when (event) {
                is SessionEvent.SessionExpired -> {
                    rootBackStack.apply {
                        clear()
                        navigateSingleTop(RootScreen.Login())
                    }
                    globalUiHostViewModel.snackbarController.sendEvent(
                        SnackbarEvent(
                            message = UiText.StringResource(R.string.login_error_token_not_valid),
                            type = SnackbarType.WARNING
                        )
                    )
                }
                is SessionEvent.LoggedOut -> {
                    rootBackStack.apply {
                        clear()
                        navigateSingleTop(RootScreen.Onboarding)
                    }
                }
            }
        }
    }

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
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            NavDisplay(
                backStack = rootBackStack,
                modifier = Modifier.fillMaxSize(),
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
                    val splashViewModel: SplashViewModel =
                        hiltViewModel()
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
                                "OAUTH" -> rootBackStack.navigateSingleTop(
                                    RootScreen.Login(
                                        isOAuthLanguageSelectionCompleted = true
                                    )
                                )

                                "OAUTH_SIGNUP" -> rootBackStack.navigateSingleTop(
                                    RootScreen.SignUp(
                                        isOAuthLanguageSelectionCompleted = true
                                    )
                                )

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
                        onNavigateToOTP = { email ->
                            rootBackStack.navigateSingleTop(RootScreen.OTP(email, false))
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
                    MainScreen(rootBackStack, viewModel = mainViewModel)
                }

                entry<RootScreen.Notification> {
                    NotificationScreen(
                        onBackClick = { rootBackStack.removeLastOrNull() }
                    )
                }

                entry<RootScreen.Map> { screen ->
                    MapScreen(
                        worldId = screen.worldId,
                        onBack = { rootBackStack.removeLastOrNull() },
                        onNavigateToLevel = { levelId, levelOrder, targetWord ->
                            rootBackStack.navigateSingleTop(
                                RootScreen.GameFlow(
                                    worldId = screen.worldId,
                                    levelId = levelId,
                                    levelOrder = levelOrder,
                                    targetWord = targetWord
                                )
                            )
                        }
                    )
                }

                entry<RootScreen.GameFlow> { screen ->
                    GameFlowHost(
                        worldId = screen.worldId,
                        levelId = screen.levelId,
                        levelOrder = screen.levelOrder,
                        targetWord = screen.targetWord,
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
                        onNavigateBack = { rootBackStack.removeLastOrNull() },
                        onEvaluationComplete = { result ->
                            SharedVoiceResultHolder.pendingResult = result
                            rootBackStack.navigateSingleTop(RootScreen.VoiceResult)
                        }
                    )
                }

                entry<RootScreen.MindReader> {
                    MindReaderScreen(
                        onNavigateBack = { rootBackStack.removeLastOrNull() }
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
                        },
                        onLockScreenVocabularyClick = {
                            rootBackStack.navigateSingleTop(RootScreen.LockScreenVocabulary)
                        },
                        onHelpSupportClick = {
                            rootBackStack.navigateSingleTop(RootScreen.HelpSupport)
                        },
                        onAboutAppClick = {
                            rootBackStack.navigateSingleTop(RootScreen.AboutApp)
                        }
                    )
                }

                entry<RootScreen.HelpSupport> {
                    HelpScreen(
                        onBack = { rootBackStack.removeLastOrNull() },
                    )
                }


                entry<RootScreen.AboutApp> {
                    AboutAppScreen(
                        onBackClick = { rootBackStack.removeLastOrNull() }
                    )
                }

                entry<RootScreen.LockScreenVocabulary> {
                    LockScreenSettingsScreen(
                        onBack = { rootBackStack.removeLastOrNull() }
                    )
                }

                entry<RootScreen.LockScreenWordDetail> { screen ->
                    LockScreenWordDetailScreen(
                        wordId = screen.wordId,
                        onBack = { rootBackStack.removeLastOrNull() },
                        onNavigateToReview = { lockScreenWord ->
                            SharedWordHolder.pendingWord =
                                com.iti.linguaquest.core.database.word.WordEntity(
                                    id = lockScreenWord.id,
                                    sourceWord = lockScreenWord.word,
                                    translatedWord = lockScreenWord.translation,
                                    sourceLanguage = lockScreenWord.targetLanguage,
                                    targetLanguage = lockScreenWord.nativeLanguage,
                                    category = lockScreenWord.proficiencyLevel,
                                    imagePath = "android.resource://com.iti.linguaquest/${R.drawable.lingo_searching}"
                                )
                            rootBackStack.navigateSingleTop(RootScreen.Review(lockScreenWord.id))
                        }
                    )
                }

                entry<RootScreen.Leaderboard> {

                    LeaderboardScreen(
                        onBack = { rootBackStack.removeLastOrNull() })
                }

                entry<RootScreen.AllWorlds> {
                    AllWorldsScreen(
                        onNavigateBack = { rootBackStack.removeLastOrNull() },
                        onNavigateToWorldDetails = { worldId ->
                            rootBackStack.navigateSingleTop(RootScreen.Map(worldId))
                        }
                    )
                }

                entry<RootScreen.RoleplayList> {
                    com.iti.linguaquest.features.roleplay.presentation.view.RoleplayListScreen(
                        onNavigateBack = { rootBackStack.removeLastOrNull() },
                        onRoleplaySelected = { scenarioId ->
                            rootBackStack.navigateSingleTop(RootScreen.Roleplay(scenarioId))
                        }
                    )
                }

                entry<RootScreen.Roleplay> { screen ->
                    val viewModel: RoleplayViewModel = hiltViewModel()

                    RoleplayScreen(
                        scenarioId = screen.scenarioId,
                        onNavigateHome = { rootBackStack.removeLastOrNull() },
                        viewModel = viewModel
                    )
                }

                entry<RootScreen.AddLanguages> {
                    AddLanguagesScreen(
                        onNavigateBack = { rootBackStack.removeLastOrNull() }
                    )
                }

                entry<RootScreen.Achievement> {
                    AchievementScreen(
                        onBackClick = { rootBackStack.removeLastOrNull() },
                    )

                }
                entry<RootScreen.EditProfile> {
                    EditProfileScreen(
                        onBackClick = { rootBackStack.removeLastOrNull() }
                    )
                }

                GlobalDialogHost(globalUiHostViewModel.dialogController)
            })

            val notificationMessage by globalUiHostViewModel.notificationBannerController.notificationMessage.collectAsState()
            var activeNotification by remember { androidx.compose.runtime.mutableStateOf<com.iti.linguaquest.core.sharedComponents.NotificationBannerState?>(null) }
            
            LaunchedEffect(notificationMessage) {
                if (notificationMessage != null) {
                    mainViewModel.refreshUnreadCount()
                    activeNotification = notificationMessage
                    soundPlayer.play(AppSound.Notification)
                    delay(6000)
                    soundPlayer.play(AppSound.NotificationDisappear)
                    globalUiHostViewModel.notificationBannerController.hideNotification()
                }
            }

            AnimatedVisibility(
                visible = notificationMessage != null,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                ) + fadeIn(tween(400)),
                exit = slideOutVertically(
                    targetOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeOut(tween(300)),
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                activeNotification?.let { notif ->
                    InAppNotificationBanner(
                        title = notif.title,
                        message = notif.message,
                        onClick = {
                            globalUiHostViewModel.notificationBannerController.hideNotification()
                            val isAchievement = notif.type?.contains("ACHIEVEMENT", ignoreCase = true) == true ||
                                                notif.title.contains("Achievement", ignoreCase = true) ||
                                                notif.title.contains("Trophy", ignoreCase = true)
                            if (isAchievement) {
                                rootBackStack.navigateSingleTop(RootScreen.Achievement)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    )
                }
            }
        }
    }
}
