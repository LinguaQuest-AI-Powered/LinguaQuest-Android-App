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
import com.iti.linguaquest.features.game.presentation.failure.view.GameFailureScreen
import com.iti.linguaquest.features.game.presentation.game.view.GameScreen
import com.iti.linguaquest.features.game.presentation.proccessing.view.CameraProcessingScreen
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel
import com.iti.linguaquest.features.game.presentation.success.view.GameSuccessScreen

@Composable
fun GameFlowHost(
    levelId: Int,
    rootBackStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
    sharedViewModel: GameSharedViewModel = hiltViewModel() // Scoped to this host
) {
    // Initialize the nested back stack starting at the GameLobby
    val gameBackStack = rememberNavBackStack(GameFlowScreen.GameLobby)

    NavDisplay(
        backStack = gameBackStack,
        modifier = modifier.fillMaxSize(),
        onBack = {
            // If we are at the root of the game flow, pop the entire flow from the main app
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
                    onSubmitPhoto = { gameBackStack.navigateSingleTop(GameFlowScreen.Processing) },
                    onBack = { gameBackStack.removeLastOrNull() }
                )
            }
            entry<GameFlowScreen.Processing> {
                CameraProcessingScreen(
                    sharedViewModel = sharedViewModel,
                    onSuccess = {
                        gameBackStack.clear()
                        gameBackStack.navigateSingleTop(GameFlowScreen.Success)
                    },
                    onFailure = {
                        gameBackStack.clear()
                        gameBackStack.navigateSingleTop(GameFlowScreen.Failure)
                    }
                )
            }
            entry<GameFlowScreen.Success> {
                GameSuccessScreen(
                    sharedViewModel = sharedViewModel,
                    onNextLevel = { /* Handle logic, perhaps pop back to lobby */ },
                    onExit = { rootBackStack.removeLastOrNull() }
                )
            }
            entry<GameFlowScreen.Failure> {
                GameFailureScreen(
                    sharedViewModel = sharedViewModel,
                    onRetry = {
                        gameBackStack.clear()
                        gameBackStack.navigateSingleTop(GameFlowScreen.Camera)
                    },
                    onExit = { rootBackStack.removeLastOrNull() }
                )
            }
        }
    )
}