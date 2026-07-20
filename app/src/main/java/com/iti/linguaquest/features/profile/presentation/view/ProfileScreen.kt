package com.iti.linguaquest.features.profile.presentation.view

import androidx.activity.result.PickVisualMediaRequest
import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.utils.createImageCaptureUri
import com.iti.linguaquest.features.profile.presentation.contract.ProfileEffect
import com.iti.linguaquest.features.profile.presentation.contract.ProfileIntent
import com.iti.linguaquest.features.profile.presentation.model.ProfileState
import com.iti.linguaquest.features.profile.presentation.view.components.*
import com.iti.linguaquest.features.profile.presentation.viewModel.ProfileViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    onSettingsClick: () -> Unit = {},
    onChangeLanguageClick: () -> Unit = {},
    onViewAllAchievementsClick: () -> Unit = {},
    onViewAllLeaderboardClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showAvatarSheet by rememberSaveable { mutableStateOf(false) }
    var pendingCameraUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) pendingCameraUri?.let { viewModel.onIntent(ProfileIntent.AvatarPicked(it)) }
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) pendingCameraUri?.let { cameraLauncher.launch(it) }
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> uri?.let { viewModel.onIntent(ProfileIntent.AvatarPicked(it)) } }

    LaunchedEffect(Unit) {
        com.iti.linguaquest.core.navigation.SharedBackgroundState.showBackground = false
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ProfileEffect.NavigateToSettings -> onSettingsClick()
                ProfileEffect.NavigateToChangeLanguage -> onChangeLanguageClick()
                ProfileEffect.NavigateToAllAchievements -> onViewAllAchievementsClick()
                ProfileEffect.NavigateToAllLeaderboard -> onViewAllLeaderboardClick()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            ProfileContent(
                state = uiState.profile,
                onSettingsClick = { viewModel.onIntent(ProfileIntent.SettingsClicked) },
                onEditAvatarClick = { showAvatarSheet = true },
                onChangeLanguageClick = { viewModel.onIntent(ProfileIntent.ChangeLanguageClicked) },
                onViewAllAchievementsClick = { viewModel.onIntent(ProfileIntent.ViewAllAchievementsClicked) },
                onViewAllLeaderboardClick = { viewModel.onIntent(ProfileIntent.ViewAllLeaderboardClicked) },
                modifier = Modifier.fillMaxSize()
            )
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
    onSettingsClick: () -> Unit,
    onEditAvatarClick: () -> Unit,
    onChangeLanguageClick: () -> Unit,
    onViewAllAchievementsClick: () -> Unit,
    onViewAllLeaderboardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { ProfileHeader(state, onEditAvatarClick) }
        item { StatsGrid(state) }
        item { LearningProgressCard(state, onChangeLanguageClick) }
        item { SettingsRow(onClick = onSettingsClick) }
        item { SectionHeader(stringResource(R.string.achievements_title), onViewAllAchievementsClick) }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.achievements, key = { it.id }) { AchievementCard(it) }
            }
        }
        item { SectionHeader(stringResource(R.string.leaderboard_title), onViewAllLeaderboardClick) }
        items(state.nearbyLeaderboard, key = { it.rank }) { LeaderboardRow(it) }
    }
}