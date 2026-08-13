package com.iti.linguaquest.features.profile.presentation.view

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.navigation.SharedBackgroundState
import com.iti.linguaquest.core.sharedComponents.LoadingView
import com.iti.linguaquest.core.utils.createImageCaptureUri
import com.iti.linguaquest.core.sharedComponents.state.DataStatus
import com.iti.linguaquest.core.sharedComponents.state.StatefulContentContainer
import com.iti.linguaquest.features.profile.presentation.contract.ProfileEffect
import com.iti.linguaquest.features.profile.presentation.contract.ProfileIntent
import com.iti.linguaquest.features.profile.presentation.view.components.ProfileContent
import com.iti.linguaquest.features.profile.presentation.view.components.ProfileOverlays
import com.iti.linguaquest.features.profile.presentation.viewModel.ProfileViewModel
import kotlinx.coroutines.flow.collectLatest
import com.iti.linguaquest.core.tutorial.presentation.LocalTutorialManager
import com.iti.linguaquest.core.tutorial.domain.model.TutorialIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit = {},
    onViewAllAchievementsClick: () -> Unit = {},
    onViewAllLeaderboardClick: () -> Unit = {},
    onNavigateHome: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val tutorialManager = LocalTutorialManager.current
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showAvatarSheet by remember { mutableStateOf(false) }
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    fun guardOnline(action: () -> Unit) {
        if (isOnline) {
            action()
        }
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) pendingCameraUri?.let { viewModel.onIntent(ProfileIntent.AvatarPicked(it)) }
        }
    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) pendingCameraUri?.let { cameraLauncher.launch(it) }
        }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.onIntent(ProfileIntent.AvatarPicked(it)) }
    }


    LaunchedEffect(Unit) {
        SharedBackgroundState.showBackground = true
    }

    LaunchedEffect(tutorialManager, uiState.hasData) {
        if (uiState.hasData) {
            tutorialManager?.onIntent(
                TutorialIntent.StartProfileTour(
                    force = false,
                    hasAchievements = uiState.profile.achievements.isNotEmpty(),
                    hasLeaderboard = uiState.profile.nearbyLeaderboard.isNotEmpty()
                )
            )
        }
    }

    var wasOffline by remember { mutableStateOf(!isOnline) }
    LaunchedEffect(isOnline) {
        if (isOnline && wasOffline) {
            viewModel.onIntent(ProfileIntent.Refresh)
        }
        wasOffline = !isOnline
    }

    LaunchedEffect(Unit) {
        if (!uiState.hasData && uiState.dataStatus is DataStatus.Loading) {
            viewModel.onIntent(ProfileIntent.Retry)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ProfileEffect.NavigateToSettings -> onSettingsClick()
                ProfileEffect.NavigateToAllAchievements -> onViewAllAchievementsClick()
                ProfileEffect.NavigateToAllLeaderboard -> onViewAllLeaderboardClick()
                ProfileEffect.NavigateToHome -> onNavigateHome()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        StatefulContentContainer(
            dataStatus = uiState.dataStatus,
            onRetry = { viewModel.onIntent(ProfileIntent.Refresh) },
            onRefresh = { viewModel.onIntent(ProfileIntent.Refresh) },
            onErrorDismiss = onNavigateHome,
            modifier = Modifier.fillMaxSize(),
            loadingContent = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingView(
                        message = stringResource(R.string.loading)
                    )
                }
            }
        ) {
            ProfileContent(
                state = uiState.profile,
                isAvatarUploading = uiState.isAvatarUploading,
                onSettingsClick = { viewModel.onIntent(ProfileIntent.SettingsClicked) },
                onEditAvatarClick = { guardOnline { showAvatarSheet = true } },
                onViewAllAchievementsClick = { guardOnline { viewModel.onIntent(ProfileIntent.ViewAllAchievementsClicked) } },
                onViewAllLeaderboardClick = { guardOnline { viewModel.onIntent(ProfileIntent.ViewAllLeaderboardClicked) } },
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    ProfileOverlays(
        showAvatarSheet = showAvatarSheet,
        onDismissAvatarSheet = { showAvatarSheet = false },
        onTakePhotoClick = {
            showAvatarSheet = false
            val uri = createImageCaptureUri(context)
            pendingCameraUri = uri
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        },
        onChooseFromGalleryClick = {
            showAvatarSheet = false
            galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    )
}