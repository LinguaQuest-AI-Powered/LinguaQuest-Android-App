package com.iti.linguaquest.core.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.iti.linguaquest.R

class NestedNavigator {
    val backStack = mutableStateListOf<NestedScreen>(NestedScreen.Home)

    fun navigateToTopLevel(screen: NestedScreen) {
        if (backStack.lastOrNull() == screen) return
        
        backStack.clear()
        backStack.add(NestedScreen.Home)
        if (screen != NestedScreen.Home) {
            backStack.add(screen)
        }
    }

    fun popBackStack() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.size - 1)
        }
    }
}

@Composable
fun MainScreen(rootNavigator: RootNavigator, modifier: Modifier = Modifier) {
    val nestedNavigator = remember { NestedNavigator() }
    val currentScreen = nestedNavigator.backStack.lastOrNull()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                BottomNavScreen.entries.forEach { bottomNavScreen ->
                    NavigationBarItem(
                        selected = currentScreen == bottomNavScreen.route,
                        onClick = { nestedNavigator.navigateToTopLevel(bottomNavScreen.route) },
                        icon = { Icon(bottomNavScreen.icon, contentDescription = stringResource(id = bottomNavScreen.labelRes)) },
                        label = { Text(stringResource(id = bottomNavScreen.labelRes)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = nestedNavigator.backStack,
            modifier = Modifier.padding(innerPadding),
            onBack = { nestedNavigator.popBackStack() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<NestedScreen.Home> {
                    HomeScreen(
                        onNavigateToDetails = { id ->
                            rootNavigator.navigateTo(RootScreen.Details(id))
                        }
                    )
                }

                entry<NestedScreen.Profile> {
                    ProfileScreen()
                }
            }
        )
    }
}

@Composable
fun HomeScreen(onNavigateToDetails: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(text = stringResource(R.string.home_screen))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onNavigateToDetails(1) }) {
            Text(stringResource(R.string.go_to_details_1))
        }
    }
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(text = stringResource(R.string.profile_screen))
    }
}
