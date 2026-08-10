package com.iti.linguaquest.core.navigation

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
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
    openDailyMissionRequested: Boolean = false,
    onOpenDailyMissionHandled: () -> Unit = {},
    viewModel: MainViewModel = hiltViewModel()
) {
    var currentTab by rememberSaveable { mutableStateOf(BottomNavScreen.Home) }
    val saveableStateHolder = rememberSaveableStateHolder()

    val wallet by viewModel.wallet.collectAsStateWithLifecycle()
    val unreadCount by viewModel.unreadNotificationCount.collectAsStateWithLifecycle()

    BackHandler(enabled = currentTab != BottomNavScreen.Home) {
        currentTab = BottomNavScreen.Home
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
                    currentRoute = currentTab.route,
                    onItemClick = { bottomNavScreen ->
                        currentTab = bottomNavScreen
                    },
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        SharedBottomBarState.heightPx = coordinates.size.height
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                saveableStateHolder.SaveableStateProvider(key = currentTab) {
                    when (currentTab) {
                        BottomNavScreen.Home -> {
                            HomeScreen(
                                openDailyMissionRequested = openDailyMissionRequested,
                                onOpenDailyMissionHandled = onOpenDailyMissionHandled,
                                onNavigateToAllWorlds = {
                                    rootBackStack.navigateSingleTop(RootScreen.AllWorlds)
                                },
                                onNavigateToWorldMap = { worldId, totalLevels ->
                                    rootBackStack.navigateSingleTop(RootScreen.Map(worldId, totalLevels))
                                },
                                onNavigateToAddLanguages = {
                                    rootBackStack.navigateSingleTop(RootScreen.AddLanguages)
                                },
                                onNavigateToLevel = { worldId, levelId, levelOrder, totalLevels, targetWord ->
                                    rootBackStack.navigateSingleTop(RootScreen.Map(worldId, totalLevels))
                                    rootBackStack.navigateSingleTop(RootScreen.GameFlow(worldId = worldId, levelId = levelId, levelOrder = levelOrder, targetWord = targetWord))
                                },
                                onNavigateToDailyMissionCamera = { word ->
                                    rootBackStack.navigateSingleTop(RootScreen.DailyMissionCamera(word))
                                }
                            )
                        }
                        BottomNavScreen.Gallery -> {
                            GalleryScreen(
                                onNavigateToReview = { word ->
                                    SharedWordHolder.pendingWord = word
                                    rootBackStack.navigateSingleTop(RootScreen.Review(word.id))
                                }
                            )
                        }
                        BottomNavScreen.Lingos -> {
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
                        BottomNavScreen.Profile -> {
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
                }
            }
        }
    }
}