package com.iti.linguaquest.features.profile.presentation.view

import androidx.activity.result.PickVisualMediaRequest
import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.navigation.SharedBackgroundState
import com.iti.linguaquest.core.utils.createImageCaptureUri
import com.iti.linguaquest.features.profile.presentation.contract.ProfileEffect
import com.iti.linguaquest.features.profile.presentation.contract.ProfileIntent
import com.iti.linguaquest.features.profile.presentation.model.ProfileState
import com.iti.linguaquest.features.profile.presentation.view.components.*
import com.iti.linguaquest.core.sharedComponents.ErrorView
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.profile.presentation.viewModel.ProfileViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit = {},
    onViewAllAchievementsClick: () -> Unit = {},
    onViewAllLeaderboardClick: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showAvatarSheet by remember { mutableStateOf(false) }
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    var showOfflinePopup by remember { mutableStateOf(false) }

    fun guardOnline(action: () -> Unit) {
        if (isOnline) {
            action()
        } else {
            showOfflinePopup = true
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.onIntent(ProfileIntent.AvatarPicked(it)) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            pendingCameraUri?.let { viewModel.onIntent(ProfileIntent.AvatarPicked(it)) }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val uri = createImageCaptureUri(context)
            pendingCameraUri = uri
            cameraLauncher.launch(uri)
        }
    }

    LaunchedEffect(Unit) {
        SharedBackgroundState.showBackground = true
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ProfileEffect.NavigateToSettings -> onSettingsClick()
                ProfileEffect.NavigateToAllAchievements -> onViewAllAchievementsClick()
                ProfileEffect.NavigateToAllLeaderboard -> onViewAllLeaderboardClick()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.onIntent(ProfileIntent.Refresh) },
            modifier = Modifier.fillMaxSize()
        ) {
            if (uiState.hasError && uiState.profile.userName.isBlank()) {
                ErrorView(
                    message = uiState.errorMessage ?: UiText.StringResource(R.string.error_generic),
                    onRetry = { viewModel.onIntent(ProfileIntent.Refresh) },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
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
    }

    if (showAvatarSheet) {
        AvatarPickerBottomSheet(
            onDismiss = { showAvatarSheet = false },
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
}

@Composable
fun ProfileContent(
    state: ProfileState,
    isAvatarUploading: Boolean = false,
    onSettingsClick: () -> Unit,
    onEditAvatarClick: () -> Unit,
    onViewAllAchievementsClick: () -> Unit,
    onViewAllLeaderboardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { ProfileHeader(state, onEditAvatarClick, isAvatarUploading) }
        item { StatsGrid(state) }
        item { LearningProgressCard(state) }
        item { SettingsRow(onClick = onSettingsClick) }
        if (state.achievements.isNotEmpty()){
            item { SectionHeader(stringResource(R.string.achievements_title), onViewAllAchievementsClick) }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.achievements, key = { it.id }) { AchievementCard(it) }
                }
            }
        }
        if (state.nearbyLeaderboard.isNotEmpty()) {
            item { SectionHeader(stringResource(R.string.leaderboard_title), onViewAllLeaderboardClick) }
            items(state.nearbyLeaderboard, key = { it.rank }) { LeaderboardRow(it) }
        }
    }

}
