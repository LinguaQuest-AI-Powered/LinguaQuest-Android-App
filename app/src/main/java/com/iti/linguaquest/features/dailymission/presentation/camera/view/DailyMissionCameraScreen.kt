package com.iti.linguaquest.features.dailymission.presentation.camera.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.features.dailymission.presentation.camera.contract.DailyMissionCameraEffect
import com.iti.linguaquest.features.dailymission.presentation.camera.contract.DailyMissionCameraIntent
import com.iti.linguaquest.features.game.presentation.camera.contract.PermissionStatus
import com.iti.linguaquest.features.dailymission.presentation.camera.viewmodel.DailyMissionCameraViewModel
import com.iti.linguaquest.features.game.presentation.camera.view.CameraContent
import com.iti.linguaquest.features.game.presentation.camera.view.CameraPreviewContent
import com.iti.linguaquest.features.game.presentation.camera.view.component.CameraPermissionView
import com.iti.linguaquest.features.game.presentation.camera.view.component.takePhoto
import com.iti.linguaquest.features.game.presentation.processing.view.component.GameProcessingView

@Composable
fun DailyMissionCameraScreen(
    word: String,
    onBack: () -> Unit,
    snackbarController: SnackbarController,
    viewModel: DailyMissionCameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val soundPlayer = LocalSoundPlayer.current
    val state by viewModel.state.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onIntent(DailyMissionCameraIntent.PermissionResult(true))
        } else {
            val activity = context as? Activity
            val shouldShowRationale = activity?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA)
            } ?: false
            if (shouldShowRationale) {
                viewModel.onIntent(DailyMissionCameraIntent.PermissionResult(false))
            } else {
                viewModel.onIntent(DailyMissionCameraIntent.PermissionResult(false))
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val isAlreadyGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED

                if (isAlreadyGranted) {
                    viewModel.onIntent(DailyMissionCameraIntent.PermissionResult(true))
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        val isAlreadyGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if (isAlreadyGranted) {
            viewModel.onIntent(DailyMissionCameraIntent.PermissionResult(true))
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
        viewModel.onIntent(DailyMissionCameraIntent.InitWord(word))
    }

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    LaunchedEffect(state.isFrontCamera) {
        cameraController.cameraSelector = if (state.isFrontCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    LaunchedEffect(state.isFlashEnabled) {
        cameraController.enableTorch(state.isFlashEnabled)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                DailyMissionCameraEffect.NavigateBack -> onBack()
                is DailyMissionCameraEffect.ShowSnackbar -> {
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = effect.message,
                            type = SnackbarType.INFO
                        )
                    )
                }
            }
        }
    }

    if (state.permissionStatus != PermissionStatus.GRANTED) {
        CameraPermissionView(
            status = state.permissionStatus,
            onGrantClicked = {
                viewModel.onIntent(DailyMissionCameraIntent.GrantPermissionClicked)
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            },
            onBackClicked = { viewModel.onIntent(DailyMissionCameraIntent.BackClicked) }
        )
    } else if (state.capturedUri == null) {
        CameraContent(
            hasPermission = true,
            targetWord = state.word,
            isHintUsed = false,
            isFlashEnabled = state.isFlashEnabled,
            cameraController = cameraController,
            onBackClicked = { viewModel.onIntent(DailyMissionCameraIntent.BackClicked) },
            onToggleFlash = { viewModel.onIntent(DailyMissionCameraIntent.ToggleFlash) },
            onFlipCamera = { viewModel.onIntent(DailyMissionCameraIntent.ToggleCameraLens) },
            onCaptureClicked = {
                soundPlayer.play(AppSound.CAMERA)
                takePhoto(context, cameraController) { uri ->
                    viewModel.onIntent(DailyMissionCameraIntent.CapturePhoto(uri))
                }
            }
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            CameraPreviewContent(
                imageUri = state.capturedUri!!,
                targetWord = state.word,
                onRetryClicked = { viewModel.onIntent(DailyMissionCameraIntent.RetryCapture) },
                onSubmitClicked = { viewModel.onIntent(DailyMissionCameraIntent.SubmitPhoto) }
            )

            if (state.isSubmitting) {
                GameProcessingView(
                    targetWord = state.word,
                    imageUri = state.capturedUri,
                    onStartGameClicked = {},
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
