package com.iti.linguaquest.core.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.iti.linguaquest.features.profile.presentation.model.Achievement
import com.iti.linguaquest.features.profile.presentation.model.LeaderboardEntry
import com.iti.linguaquest.features.profile.presentation.model.ProfileState
import com.iti.linguaquest.features.profile.presentation.view.ProfileScreen

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
                    GalleryScreen()
                }
                    entry<NestedScreen.Profile> {
                        ProfileScreen(state = mockProfileState)
                    }

            }
        )
    }
}




@Composable
fun GalleryScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.lingo_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = stringResource(R.string.home_screen))

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private val mockProfileState = ProfileState(
    userName = "Explorer Alex",
    level = 12,
    avatarUrl = R.drawable.lingo_writing,
    coins = 1250,
    totalXp = 4500,
    streakDays = 7,
    worldsCount = 2,
    learningLanguageName = "French",
    learningLanguageFlagRes = R.drawable.flag_spain,
    proficiencyLabel = "Intermediate Journey",
    currentMilestoneXp = 2450,
    targetMilestoneXp = 3000,
    achievements = listOf(
        Achievement("1", "Wild Explorer", R.drawable.achievement_cup, "Complete 10 lessons in...")
    ),
    nearbyLeaderboard = listOf(
        LeaderboardEntry(99, "Sacagawea", "Guide", 2750, avatarUrl = R.drawable.lingo_writing),
        LeaderboardEntry(100, "Explorer Sam", "Adventurer", 3150, isCurrentUser = true,avatarUrl = R.drawable.lingo_writing),
        LeaderboardEntry(101, "Zheng He", "Admiral", 2600,avatarUrl = R.drawable.lingo_writing)
    )
)