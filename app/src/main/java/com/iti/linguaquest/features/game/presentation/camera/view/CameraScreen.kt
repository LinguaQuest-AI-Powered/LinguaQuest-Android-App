package com.iti.linguaquest.features.game.presentation.camera.view

import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.linguaquest.features.game.presentation.camera.viewmodel.CameraViewModel
import com.iti.linguaquest.features.game.presentation.camera.contract.CameraIntent
import com.iti.linguaquest.features.game.presentation.camera.contract.PermissionStatus
import com.iti.linguaquest.features.game.presentation.camera.view.component.CameraOverlay
import com.iti.linguaquest.features.game.presentation.camera.view.component.CameraPermissionView
import com.iti.linguaquest.features.game.presentation.camera.view.component.takePhoto
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel

@Composable
fun CameraScreen(
    sharedViewModel: GameSharedViewModel,
    onSubmitPhoto: () -> Unit,
    onBack: () -> Unit,
    viewModel: CameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val sharedState by sharedViewModel.sharedState.collectAsState()
    val cameraState by viewModel.state.collectAsState()

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    LaunchedEffect(cameraState.isFrontCamera) {
        cameraController.cameraSelector = if (cameraState.isFrontCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    LaunchedEffect(cameraState.isFlashEnabled) {
        cameraController.enableTorch(cameraState.isFlashEnabled)
    }

    CameraOverlay(
        effectFlow = viewModel.effect,
        onPermissionResult = { isGranted ->
            viewModel.onIntent(CameraIntent.PermissionResult(isGranted))
        },
        onNavigateToProcessing = { uri ->
            sharedViewModel.setCapturedImage(uri)
            onSubmitPhoto()
        },
        onNavigateBack = onBack
    )
    if (cameraState.permissionStatus != PermissionStatus.GRANTED) {
        CameraPermissionView(
            status = cameraState.permissionStatus,
            onGrantClicked = { viewModel.onIntent(CameraIntent.GrantPermissionClicked) },
            onBackClicked = { viewModel.onIntent(CameraIntent.BackClicked) }
        )
    } else if (cameraState.capturedUri == null) {
        CameraContent(
            hasPermission = true,
            targetWord = sharedState.targetWord,
            isHintUsed = sharedState.isHintUsed,
            isFlashEnabled = cameraState.isFlashEnabled,
            cameraController = cameraController,
            onBackClicked = { viewModel.onIntent(CameraIntent.BackClicked) },
            onToggleFlash = { viewModel.onIntent(CameraIntent.ToggleFlash) },
            onFlipCamera = { viewModel.onIntent(CameraIntent.ToggleCameraLens) },
            onCaptureClicked = {
                takePhoto(context, cameraController) { uri ->
                    viewModel.onIntent(CameraIntent.CapturePhoto(uri))
                }
            }
        )
    } else {
        CameraPreviewContent(
            imageUri = cameraState.capturedUri!!,
            onRetryClicked = { viewModel.onIntent(CameraIntent.RetryCapture) },
            onSubmitClicked = { viewModel.onIntent(CameraIntent.SubmitPhoto) }
        )
    }
}