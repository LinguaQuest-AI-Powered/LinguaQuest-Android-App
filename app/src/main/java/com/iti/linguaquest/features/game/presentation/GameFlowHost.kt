package com.iti.linguaquest.features.game.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
import com.iti.linguaquest.features.game.presentation.level.LevelScreen
import com.iti.linguaquest.features.game.presentation.processing.view.GameProcessingScreen
import com.iti.linguaquest.features.game.presentation.result.view.GameResultScreen
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel

@Composable
fun GameFlowHost(
    worldId: Int,
    levelNumber: Int,
    rootBackStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
    sharedViewModel: GameSharedViewModel = hiltViewModel()
) {
    val gameBackStack = rememberNavBackStack(GameFlowScreen.Level)
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
            entry<GameFlowScreen.Level> {
                LevelScreen(
                    worldId = worldId,
                    levelNumber = levelNumber,
                    sharedViewModel = sharedViewModel,
                    onBack = { rootBackStack.removeLastOrNull() },
                    onStartCamera = {
                        gameBackStack.navigateSingleTop(GameFlowScreen.Camera)
                    }
                )
            }
            entry<GameFlowScreen.Camera> {
                CameraScreen(
                    sharedViewModel = sharedViewModel,
                    onSubmitPhoto = {
                        gameBackStack.navigateSingleTop(GameFlowScreen.Processing)
                    },
                    onBack = { gameBackStack.removeLastOrNull() }
                )
            }

            entry<GameFlowScreen.Processing> {
                GameProcessingScreen(
                    sharedViewModel = sharedViewModel,
                    onNavigateToResult = {
                        gameBackStack.navigateSingleTop(GameFlowScreen.Result)
                    }
                )
            }
            entry<GameFlowScreen.Result> {
                GameResultScreen(
                    sharedViewModel = sharedViewModel,
                    onNavigateToCamera = {
                        while (gameBackStack.lastOrNull() != GameFlowScreen.Camera && gameBackStack.isNotEmpty()) {
                            gameBackStack.removeLastOrNull()
                        }
                        if (gameBackStack.isEmpty() || gameBackStack.lastOrNull() != GameFlowScreen.Camera) {
                            gameBackStack.clear()
                            gameBackStack.navigateSingleTop(GameFlowScreen.Level)
                            gameBackStack.navigateSingleTop(GameFlowScreen.Camera)
                        }
                    },
                    onNavigateToLevel = {
                        while (gameBackStack.lastOrNull() != GameFlowScreen.Level && gameBackStack.isNotEmpty()) {
                            gameBackStack.removeLastOrNull()
                        }
                    },
                    onNavigateToNextLevel = { rootBackStack.removeLastOrNull() },
                    onExit = { rootBackStack.removeLastOrNull() }
                )
            }
        }
    )
}