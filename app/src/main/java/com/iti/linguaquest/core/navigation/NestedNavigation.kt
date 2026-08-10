package com.iti.linguaquest.core.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.iti.linguaquest.features.lockscreen.presentation.view.LockScreenWordDetailScreen
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LinguaQuestTopAppBar
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.home.presentation.view.HomeScreen
import com.iti.linguaquest.features.profile.presentation.view.ProfileScreen
import com.iti.linguaquest.features.gallery.presentation.view.GalleryScreen
import com.iti.linguaquest.features.lingos.presentation.view.LingosScreen


@Composable
fun MainScreen(
    rootBackStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val nestedBackStack = if (viewModel.lastActiveTab == NestedScreen.Home) {
        rememberNavBackStack(NestedScreen.Home)
    } else {
        rememberNavBackStack(NestedScreen.Home, viewModel.lastActiveTab)
    }
    val currentScreen = nestedBackStack.lastOrNull()
    val wallet by viewModel.wallet.collectAsStateWithLifecycle()
    val unreadCount by viewModel.unreadNotificationCount.collectAsStateWithLifecycle()


    val currentRootScreen = rootBackStack.lastOrNull()
    LaunchedEffect(currentRootScreen) {
        if (currentRootScreen == RootScreen.Main) {
            viewModel.refreshUnreadCount()
        }
    }

    DisposableEffect(Unit) {
        onDispose { SharedBottomBarState.heightPx = 0 }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (SharedBackgroundState.showBackground) {
            Image(
                painter = painterResource(id = R.drawable.lingo_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                colorFilter = if (SharedBackgroundState.showDarkEffect && LinguaQuestTheme.colors.isDark) {
                    ColorFilter.tint(
                        Color.Black.copy(alpha = 0.75f),
                        BlendMode.SrcOver
                    )
                } else null
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            )
        }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                LinguaQuestTopAppBar(
                    xp = wallet.xp,
                    coins = wallet.coins,
                    unreadCount = unreadCount,
                    onBellClick = {
                        rootBackStack.navigateSingleTop(RootScreen.Notification)
                    }
                )
            },

            bottomBar = {
                GameBottomNavBar(
                    items = BottomNavScreen.entries,
                    currentRoute = currentScreen,
                    onItemClick = { bottomNavScreen ->
                        viewModel.lastActiveTab = bottomNavScreen.route
                        nestedBackStack.apply {
                            clear()
                            navigateSingleTop(NestedScreen.Home)
                            if (bottomNavScreen.route != NestedScreen.Home) {
                                navigateSingleTop(bottomNavScreen.route)
                            }
                        }
                    },
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        SharedBottomBarState.heightPx = coordinates.size.height
                    }
                )
            }
        ) { innerPadding ->
            NavDisplay(
                backStack = nestedBackStack,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                onBack = { nestedBackStack.removeLastOrNull() },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                entryProvider = entryProvider {
                    entry<NestedScreen.Home> {
                        HomeScreen(
                            onNavigateToAllWorlds = {
                                rootBackStack.navigateSingleTop(RootScreen.AllWorlds)
                            },
                            onNavigateToWorldMap = { worldId ->
                                rootBackStack.navigateSingleTop(RootScreen.Map(worldId))
                            },
                            onNavigateToAddLanguages = {
                                rootBackStack.navigateSingleTop(RootScreen.AddLanguages)
                            },
                            onNavigateToLevel = { worldId, levelId, levelOrder, targetWord ->
                                rootBackStack.navigateSingleTop(RootScreen.Map(worldId))
                                rootBackStack.navigateSingleTop(RootScreen.GameFlow(worldId = worldId, levelId = levelId, levelOrder = levelOrder, targetWord = targetWord))
                            }
                        )
                    }
                    entry<NestedScreen.Gallery> {
                        GalleryScreen(
                            onNavigateToReview = { word ->
                                SharedWordHolder.pendingWord = word
                                rootBackStack.navigateSingleTop(RootScreen.Review(word.id))
                            },
                            onShowLockScreenWordDialog = { wordId ->
                                viewModel.showLockScreenWordDialog(wordId)
                            }
                        )
                    }
                    entry<NestedScreen.Lingos> {
                        LingosScreen(
                            onNavigateToVoiceGame = {
                                rootBackStack.navigateSingleTop(RootScreen.VoiceGame)
                            },
                            onNavigateToRoleplayList = {
                                rootBackStack.navigateSingleTop(RootScreen.RoleplayList)
                            },
                            onNavigateToMindReader = {
                                rootBackStack.navigateSingleTop(RootScreen.MindReader())
                            }
                        )
                    }
                    entry<NestedScreen.Profile> {
                        ProfileScreen(
                            onSettingsClick = {
                                rootBackStack.navigateSingleTop(RootScreen.Settings)
                            },
                            onViewAllLeaderboardClick = {
                                rootBackStack.navigateSingleTop(RootScreen.Leaderboard)
                            },
                            onViewAllAchievementsClick = {
                                rootBackStack.navigateSingleTop(RootScreen.Achievement)
                            }
                        )
                    }

                }
            )
        }

        val showLockScreenWordDialogId by viewModel.showLockScreenWordDialogId.collectAsStateWithLifecycle()
        
        LaunchedEffect(showLockScreenWordDialogId) {
            if (showLockScreenWordDialogId != null) {
                if (nestedBackStack.lastOrNull() != NestedScreen.Gallery) {
                    viewModel.lastActiveTab = NestedScreen.Gallery
                    nestedBackStack.apply {
                        clear()
                        navigateSingleTop(NestedScreen.Home)
                        navigateSingleTop(NestedScreen.Gallery)
                    }
                }
            }
        }

        if (showLockScreenWordDialogId != null) {
            Dialog(
                onDismissRequest = { viewModel.hideLockScreenWordDialog() },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                LockScreenWordDetailScreen(
                    wordId = showLockScreenWordDialogId!!,
                    onBack = { viewModel.hideLockScreenWordDialog() },
                    onNavigateToReview = { lockScreenWord ->
                        viewModel.hideLockScreenWordDialog()
                        SharedWordHolder.pendingWord = com.iti.linguaquest.core.database.word.WordEntity(
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
        }
    }
}