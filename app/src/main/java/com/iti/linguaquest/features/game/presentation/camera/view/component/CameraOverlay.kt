package com.iti.linguaquest.features.game.presentation.camera.view.component

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.iti.linguaquest.features.game.presentation.camera.contract.CameraEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun CameraOverlay(
    effectFlow: Flow<CameraEffect>,
    onPermissionResult: (Boolean) -> Unit,
    onNavigateToProcessing: (Uri) -> Unit,
    onNavigateBack: () -> Unit
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onPermissionResult(isGranted)
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }


    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when (effect) {
                is CameraEffect.NavigateToProcessing -> {
                    onNavigateToProcessing(effect.imageUri)
                }
                CameraEffect.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }
}