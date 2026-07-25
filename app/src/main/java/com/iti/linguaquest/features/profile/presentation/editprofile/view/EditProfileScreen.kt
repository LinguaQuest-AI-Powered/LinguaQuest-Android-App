package com.iti.linguaquest.features.profile.presentation.editprofile.view

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.linguaquest.core.utils.createImageCaptureUri
import com.iti.linguaquest.features.profile.presentation.editprofile.contract.EditProfileIntent
import com.iti.linguaquest.features.profile.presentation.editprofile.viewmodel.EditProfileViewModel

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onIntent(EditProfileIntent.UploadPhoto(it)) }
    }

    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraImageUri?.let { viewModel.onIntent(EditProfileIntent.UploadPhoto(it)) }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraImageUri?.let { cameraLauncher.launch(it) }
        }
    }

    EditProfileScreenContent(
        modifier = modifier,
        displayName = state.displayName,
        onDisplayNameChange = { viewModel.onIntent(EditProfileIntent.OnDisplayNameChanged(it)) },
        oldPassword = state.oldPassword,
        onOldPasswordChange = { viewModel.onIntent(EditProfileIntent.OnOldPasswordChanged(it)) },
        newPassword = state.newPassword,
        onNewPasswordChange = { viewModel.onIntent(EditProfileIntent.OnNewPasswordChanged(it)) },
        avatarModel = state.avatarModel,
        isLoading = state.isLoading,
         isSavingName = state.isSavingName,
        isSavingPassword = state.isSavingPassword,
        onGalleryClick = { galleryLauncher.launch("image/*") },
        onCameraClick = {
            val uri = createImageCaptureUri(context)
            cameraImageUri = uri
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        },
        onBackClick = onBackClick,
         onSaveNameClick = { viewModel.onIntent(EditProfileIntent.SaveNameChanges) },
        onSavePasswordClick = { viewModel.onIntent(EditProfileIntent.SavePasswordChanges) },
        onCancelClick = onBackClick,
        displayNameError = state.displayNameError,
        oldPasswordError = state.oldPasswordError,
        newPasswordError = state.newPasswordError
    )
}