package com.iti.linguaquest.core.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
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
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LinguaQuestTopAppBar
import com.iti.linguaquest.features.home.presentation.view.HomeScreen
import com.iti.linguaquest.features.profile.presentation.view.ProfileScreen
import com.iti.linguaquest.features.gallery.presentation.view.GalleryScreen


@Composable
fun MainScreen(rootBackStack: NavBackStack<NavKey>, modifier: Modifier = Modifier) {
    val nestedBackStack = rememberNavBackStack(NestedScreen.Home)
    val currentScreen = nestedBackStack.lastOrNull()

    DisposableEffect(Unit) {
        onDispose { SharedBottomBarState.heightPx = 0 }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (SharedBackgroundState.showBackground) {
            Image(
                painter = painterResource(id = R.drawable.lingo_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
            )
        }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            topBar = {
                LinguaQuestTopAppBar(
                    xp = 1250,
                    lives = 45
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
                            onNavigateToDetails = { id ->
                                rootBackStack.navigateSingleTop(RootScreen.Details(id))
                            },
                            onNavigateToAllWorlds = {
                                rootBackStack.navigateSingleTop(RootScreen.AllWorlds)
                            },
                            onNavigateToWorldMap = { worldId ->
                                rootBackStack.navigateSingleTop(RootScreen.Map(worldId))
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
                            }
                        )
                    }

                }
            )
        }
    }
}