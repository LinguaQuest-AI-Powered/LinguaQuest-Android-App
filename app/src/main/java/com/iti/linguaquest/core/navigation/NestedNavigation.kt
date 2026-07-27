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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import com.iti.linguaquest.features.home.presentation.view.HomeScreen
import com.iti.linguaquest.features.profile.presentation.view.ProfileScreen
import com.iti.linguaquest.features.gallery.presentation.view.GalleryScreen
import com.iti.linguaquest.features.roleplay.domain.model.ScenarioId


@Composable
fun MainScreen(
    rootBackStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val nestedBackStack = rememberNavBackStack(NestedScreen.Home)
    val currentScreen = nestedBackStack.lastOrNull()
    val wallet by viewModel.wallet.collectAsStateWithLifecycle()
    val soundPlayer = LocalSoundPlayer.current

    LaunchedEffect(currentScreen) {
        viewModel.refreshWallet()
    }
    var previousWallet by remember { mutableStateOf<Wallet?>(null) }
    LaunchedEffect(wallet.xp, wallet.coins) {
        val previous = previousWallet
        if (previous != null && (wallet.xp != previous.xp || wallet.coins != previous.coins)) {
            soundPlayer.play(AppSound.AddedMoney)
        }
        previousWallet = wallet
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
                    lives = wallet.coins
                )
            },
            bottomBar = {
                GameBottomNavBar(
                    items = BottomNavScreen.entries,
                    currentRoute = currentScreen,
                    onItemClick = { bottomNavScreen ->
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
                            onNavigateToVoiceGame = {
                                rootBackStack.navigateSingleTop(RootScreen.VoiceGame)
                            },
                            onNavigateToRoleplayList = {
                                rootBackStack.navigateSingleTop(RootScreen.RoleplayList)
                            },
                            onNavigateToAllWorlds = {
                                rootBackStack.navigateSingleTop(RootScreen.AllWorlds)
                            },
                            onNavigateToWorldMap = { worldId ->
                                rootBackStack.navigateSingleTop(RootScreen.Map(worldId))
                            },
                            onNavigateToAddLanguages = {
                                rootBackStack.navigateSingleTop(RootScreen.AddLanguages)
                            }
                        )
                    }
                    entry<NestedScreen.Gallery> {
                        GalleryScreen(
                            onNavigateToReview = { word ->
                                SharedWordHolder.pendingWord = word
                                rootBackStack.navigateSingleTop(RootScreen.Review(word.id))
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
    }
}