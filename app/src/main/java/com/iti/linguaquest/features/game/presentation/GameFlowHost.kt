package com.iti.linguaquest.features.game.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.iti.linguaquest.core.navigation.GameFlowScreen
import com.iti.linguaquest.core.navigation.navigateSingleTop
import com.iti.linguaquest.features.game.presentation.camera.view.CameraScreen
import com.iti.linguaquest.features.game.presentation.game.view.GameScreen
import com.iti.linguaquest.features.game.presentation.result.view.GameResultScreen
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel

@Composable
fun GameFlowHost(
    levelId: Int,
    rootBackStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
    sharedViewModel: GameSharedViewModel = hiltViewModel()
) {
    val gameBackStack = rememberNavBackStack(GameFlowScreen.GameLobby)

    NavDisplay(
        backStack = gameBackStack,
        modifier = modifier.fillMaxSize(),
        onBack = {
            if (gameBackStack.size == 1) {
                rootBackStack.removeLastOrNull()
            } else {
                gameBackStack.removeLastOrNull()
            }
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<GameFlowScreen.GameLobby> {
                GameScreen(
                    sharedViewModel = sharedViewModel,
                    onStartCamera = { gameBackStack.navigateSingleTop(GameFlowScreen.Camera) },
                    onExit = { rootBackStack.removeLastOrNull() }
                )
            }
            entry<GameFlowScreen.Camera> {
                CameraScreen(
                    sharedViewModel = sharedViewModel,
                    onSubmitPhoto = {
                        gameBackStack.navigateSingleTop(GameFlowScreen.Result)
                    },
                    onBack = { gameBackStack.removeLastOrNull() }
                )
            }
            entry<GameFlowScreen.Result> {
                GameResultScreen(
                    sharedViewModel = sharedViewModel,
                    onNavigateToCamera = {
                        gameBackStack.clear()
                        gameBackStack.navigateSingleTop(GameFlowScreen.Camera)
                    },
                    onNavigateToNextLevel = { /* Handle logic, perhaps pop back to lobby */ },
                    onExit = { rootBackStack.removeLastOrNull() }
                )
            }
        }
    )
}