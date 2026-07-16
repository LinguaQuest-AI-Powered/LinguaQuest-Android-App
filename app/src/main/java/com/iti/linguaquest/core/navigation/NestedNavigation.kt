package com.iti.linguaquest.core.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
                    ProfileScreen()
                }
            }
        )
    }
}

@Composable
fun HomeScreen(
    onNavigateToDetails: (Int) -> Unit,
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

            Button(onClick = { onNavigateToDetails(1) }) {
                Text(stringResource(R.string.go_to_details_1))
            }
        }
    }
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = stringResource(R.string.profile_screen))
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