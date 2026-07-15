package com.iti.linguaquest.core.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import com.iti.linguaquest.features.auth.presentation.login.view.LoginScreen
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R

class RootNavigator {
    val backStack = mutableStateListOf<RootScreen>(RootScreen.Login)

    fun navigateTo(screen: RootScreen) {
        backStack.add(screen)
    }

    fun popBackStack() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.size - 1)
        }
    }
}


@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val rootNavigator = remember { RootNavigator() }

    NavDisplay(
        backStack = rootNavigator.backStack,
        modifier = modifier.fillMaxSize(),
        onBack = { rootNavigator.popBackStack() },
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
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<RootScreen.Login> {
                LoginScreen(
                    onSignUp = { rootNavigator.navigateTo(RootScreen.SignUp) },
                    onForgotPassword = { rootNavigator.navigateTo(RootScreen.ForgotPassword) },
                    onLoginSuccess = { rootNavigator.navigateTo(RootScreen.Main) }
                )
            }

            entry<RootScreen.Main> {
                Text(text = stringResource(R.string.main_screen), modifier = Modifier.fillMaxSize().padding(16.dp))
            }
            entry<RootScreen.SignUp> {
                Text(text = stringResource(R.string.sign_up_screen), modifier = Modifier.fillMaxSize().padding(16.dp))
            }
            entry<RootScreen.ForgotPassword> {
                Text(text = stringResource(R.string.forgot_password_screen), modifier = Modifier.fillMaxSize().padding(16.dp))
            }

            entry<RootScreen.Details> { key ->
                DetailsScreen(
                    id = key.id,
                    onBack = { rootNavigator.popBackStack() }
                )
            }
        }
    )
}


@Composable
fun DetailsScreen(id: Int, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(text = stringResource(R.string.details_screen_id, id))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text(stringResource(R.string.pop_screen))
        }
    }
}
