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
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entryProvider
class AppNavigator {
    val backStack = mutableStateListOf<Any>(Screen.Home)

    fun navigateTo(screen: Screen) {
        backStack.add(screen)
    }

    fun navigateToTopLevel(screen: Screen) {
        if (backStack.lastOrNull() == screen) return
        
        backStack.clear()
        backStack.add(Screen.Home)
        if (screen != Screen.Home) {
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
fun AppNavigation(modifier: Modifier = Modifier) {
    val navigator = remember { AppNavigator() }
    val currentScreen = navigator.backStack.lastOrNull()
    val isTopLevel = BottomNavScreen.entries.any { it.route == currentScreen }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (isTopLevel) {
                NavigationBar {
                    BottomNavScreen.entries.forEach { bottomNavScreen ->
                        NavigationBarItem(
                            selected = currentScreen == bottomNavScreen.route,
                            onClick = { navigator.navigateToTopLevel(bottomNavScreen.route) },
                            icon = { Icon(bottomNavScreen.icon, contentDescription = bottomNavScreen.routeName) },
                            label = { Text(bottomNavScreen.routeName) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = navigator.backStack,
            modifier = Modifier.padding(innerPadding),
            onBack = { navigator.popBackStack() },
            transitionSpec = {
                slideInHorizontally(
                    animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioNoBouncy),
                    initialOffsetX = { fullWidth -> fullWidth }
                ) + fadeIn(animationSpec = tween(300)) togetherWith
                slideOutHorizontally(
                    animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioNoBouncy),
                    targetOffsetX = { fullWidth -> -fullWidth }
                ) + fadeOut(animationSpec = tween(300))
            },
            popTransitionSpec = {
                slideInHorizontally(
                    animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioNoBouncy),
                    initialOffsetX = { fullWidth -> -fullWidth }
                ) + fadeIn(animationSpec = tween(300)) togetherWith
                slideOutHorizontally(
                    animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioNoBouncy),
                    targetOffsetX = { fullWidth -> fullWidth }
                ) + fadeOut(animationSpec = tween(300))
            },
            entryProvider = entryProvider {
//                entry<Screen.Home> {
//                    HomeScreen(
//                        onNavigateToDetails = { id ->
//                            navigator.navigateTo(Screen.Details(id))
//                        }
//                    )
//                }
//
//                entry<Screen.Profile> {
//                    ProfileScreen()
//                }
//
//                entry<Screen.Details> { key ->
//                    DetailsScreen(
//                        id = key.id,
//                        onBack = { navigator.popBackStack() }
//                    )
//                }
            }
        )
    }
}


