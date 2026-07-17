package com.iti.linguaquest.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.iti.linguaquest.R
import com.iti.linguaquest.core.mockData.mockProfileState
import com.iti.linguaquest.core.sharedComponents.LinguaQuestTopAppBar
import com.iti.linguaquest.features.home.presentation.view.HomeScreen
import com.iti.linguaquest.features.profile.presentation.model.Achievement
import com.iti.linguaquest.features.profile.presentation.model.LeaderboardEntry
import com.iti.linguaquest.features.profile.presentation.model.ProfileState
import com.iti.linguaquest.features.profile.presentation.view.ProfileScreen
import com.iti.linguaquest.features.gallery.presentation.view.GalleryScreen


@Composable
fun MainScreen(rootBackStack: NavBackStack<NavKey>, modifier: Modifier = Modifier) {
    val nestedBackStack = rememberNavBackStack(NestedScreen.Home)
    val currentScreen = nestedBackStack.lastOrNull()

    Scaffold(
        modifier = modifier.fillMaxSize(),
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
                        }
                    )
                }
                entry<NestedScreen.Gallery> {
                    GalleryScreen(
                        onNavigateToWordDetails = { id ->
                            rootBackStack.navigateSingleTop(RootScreen.Details(id))
                        }
                    )
                }
                entry<NestedScreen.Profile> {
                    ProfileScreen(state = mockProfileState)
                }
            })

    }

}


