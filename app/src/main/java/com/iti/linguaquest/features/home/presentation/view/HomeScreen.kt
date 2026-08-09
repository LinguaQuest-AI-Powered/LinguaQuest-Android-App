package com.iti.linguaquest.features.home.presentation.view


import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.navigation.SharedBackgroundState
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.features.home.presentation.contract.HomeEffect
import com.iti.linguaquest.features.home.presentation.contract.HomeIntent
import com.iti.linguaquest.features.home.presentation.contract.HomeState
import com.iti.linguaquest.features.home.presentation.languages.component.MyLanguagesBottomSheet
import com.iti.linguaquest.features.home.presentation.languages.contract.MyLanguagesEffect
import com.iti.linguaquest.features.home.presentation.languages.contract.MyLanguagesIntent
import com.iti.linguaquest.features.home.presentation.languages.viewmodel.MyLanguagesViewModel
import com.iti.linguaquest.features.home.presentation.view.components.HomeContent
import com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components.HomeDailyRewardBannerWrapper
import com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components.HomeDailyRewardDialog
import com.iti.linguaquest.features.home.presentation.view.components.DailyMissionDialog
import com.iti.linguaquest.features.home.presentation.viewModel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.time.Duration.Companion.milliseconds

import com.iti.linguaquest.core.sharedComponents.ErrorView
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.core.sharedComponents.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    openDailyMissionRequested: Boolean = false,
    onOpenDailyMissionHandled: () -> Unit = {},
    onNavigateToAllWorlds: () -> Unit,
    onNavigateToWorldMap: (Int, Int) -> Unit = { _, _ -> },
    onNavigateToLevel: (worldId: Int, levelId: Int, levelOrder: Int, totalLevels: Int, targetWord: String?) -> Unit,
    onNavigateToDailyMissionCamera: (String) -> Unit,
    onWorldMapClick: () -> Unit = {},
    onNavigateToAddLanguages: () -> Unit = {},
    onHeaderDataChanged: (xp: Int, coins: Int) -> Unit = { _, _ -> },
    viewModel: HomeViewModel = hiltViewModel(),
    myLanguagesViewModel: MyLanguagesViewModel = hiltViewModel()
) {
    val soundPlayer = LocalSoundPlayer.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val myLanguagesState by myLanguagesViewModel.state.collectAsStateWithLifecycle()
    var showCoinRain by remember { mutableStateOf(false) }
    var showOfflinePopup by remember { mutableStateOf(false) }
    var offlinePopupAnchor by remember { mutableStateOf<Rect?>(null) }
    var offlinePopupSize by remember { mutableStateOf(IntSize.Zero) }
    var fabBounds by remember { mutableStateOf<Rect?>(null) }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val context = LocalContext.current
    val fallZoneHeight = (configuration.screenHeightDp / 2).dp
    var bannerHeightPx by remember { mutableFloatStateOf(0f) }

    fun guardOnline(anchor: Rect? = null, action: () -> Unit) {
        if (isOnline) {
            action()
        }
    }

    LaunchedEffect(state.xp, state.coins) {
        onHeaderDataChanged(state.xp, state.coins)
    }

    LaunchedEffect(openDailyMissionRequested, isOnline) {
        if (openDailyMissionRequested && isOnline) {
            viewModel.onIntent(HomeIntent.TriggerDailyMission)
            onOpenDailyMissionHandled()
        }
    }



    LaunchedEffect(state.isDailyRewardBannerVisible) {
        if (state.isDailyRewardBannerVisible) {
            soundPlayer.play(AppSound.AddedMoney)
            showCoinRain = true
            delay(3000.milliseconds)
            showCoinRain = false
        }
    }

    LaunchedEffect(state.isDailyRewardBannerVisible) {
        if (state.isDailyRewardBannerVisible) {
            delay(6000.milliseconds)
            viewModel.onIntent(HomeIntent.DismissDailyRewardBanner)
        }
    }

    LaunchedEffect(Unit) {
        SharedBackgroundState.showBackground = true
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HomeEffect.NavigateToWorld -> onNavigateToWorldMap(effect.worldId, effect.totalLevels)
                HomeEffect.NavigateToAllWorlds -> onNavigateToAllWorlds()
                is HomeEffect.NavigateToAddLanguages -> onNavigateToAddLanguages()
                is HomeEffect.NavigateToContinueLevel -> {
                    onNavigateToLevel(effect.worldId, effect.levelId, effect.levelOrder, effect.totalLevels, effect.targetWord?.asString(context))
                }
                is HomeEffect.NavigateToDailyMissionCamera -> {
                    onNavigateToDailyMissionCamera(effect.word)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        myLanguagesViewModel.effect.collectLatest { effect ->
            when (effect) {
                MyLanguagesEffect.NavigateToAddLanguages -> {
                    guardOnline {
                        onNavigateToAddLanguages()
                    }
                }
                MyLanguagesEffect.Dismiss -> {
                    viewModel.onIntent(HomeIntent.DismissLanguageBottomSheet)
                    viewModel.onIntent(HomeIntent.Retry)
                }
            }
        }
    }

    LaunchedEffect(state.isLanguageBottomSheetVisible) {
        if (state.isLanguageBottomSheetVisible) {
            myLanguagesViewModel.onIntent(MyLanguagesIntent.LoadMyLanguages)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        Crossfade(
            targetState = state.isLoading,
            label = "HomeLoadingCrossfade",
            modifier = Modifier.fillMaxSize()
        ) { isLoading ->
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingView(
                        message = stringResource(R.string.loading)
                    )
                }
            } else {
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = { viewModel.onIntent(HomeIntent.Refresh) },
                    modifier = Modifier.fillMaxSize()
                ) {
                    HomeContent(
                        state = state,
                        onSeeMoreClick = { anchor ->
                            guardOnline(anchor) { viewModel.onIntent(HomeIntent.SeeMoreWorldsClicked) }
                        },
                        onWorldClick = { world, anchor ->
                            guardOnline(anchor) { viewModel.onIntent(HomeIntent.WorldClicked(world)) }
                        },
                        onContinueLevelClick = { level, anchor ->
                            guardOnline(anchor) { viewModel.onIntent(HomeIntent.ContinueLevelClicked(level, anchor)) }
                        }
                    )
                }
            }
        }

        if (state.hasError && state.worlds.isEmpty() && state.languageProgress == null) {
            ErrorView(
                message = state.errorMessage ?: com.iti.linguaquest.core.sharedComponents.text.UiText.StringResource(R.string.error_generic),
                onRetry = { viewModel.onIntent(HomeIntent.Refresh) }
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FloatingActionButton(
                onClick = { guardOnline(fabBounds) { viewModel.onIntent(HomeIntent.TriggerDailyMission) } },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_streak),
                    contentDescription = "daily_mission_content_description",
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            FloatingActionButton(
                onClick = { guardOnline(fabBounds) { viewModel.onIntent(HomeIntent.FabClicked) } },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        fabBounds = coordinates.boundsInRoot()
                    }
            ) {
                Image(
                    painter = painterResource(R.drawable.world_home_icon),
                    contentDescription = "world_map_content_description",
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        HomeDailyRewardBannerWrapper(
            isVisible = state.isDailyRewardBannerVisible,
            showCoinRain = showCoinRain,
            fallZoneHeight = fallZoneHeight,
            bannerHeightPx = bannerHeightPx,
            onBannerClick = { viewModel.onIntent(HomeIntent.DailyRewardBannerClicked) },
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }

    HomeDailyRewardDialog(
        state = state,
        onDismissRequest = { viewModel.onIntent(HomeIntent.DismissDailyRewardDialog) },
        onClaimClick = { viewModel.onIntent(HomeIntent.ClaimDailyRewardClicked) }
    )

    if (state.isLanguageBottomSheetVisible) {
        MyLanguagesBottomSheet(
            languages = myLanguagesState.languages,
            isLoading = myLanguagesState.isLoading,
            isSettingActive = myLanguagesState.isSettingActive,
            languagePendingRemoval = myLanguagesState.languagePendingRemoval,
            removingLanguageId = myLanguagesState.removingLanguageId,
            onDismiss = { myLanguagesViewModel.onIntent(MyLanguagesIntent.Dismiss) },
            onAddNewLanguageClick = { myLanguagesViewModel.onIntent(MyLanguagesIntent.AddNewLanguageClicked) },
            onLanguageSelect = { selectedId ->
                myLanguagesViewModel.onIntent(MyLanguagesIntent.SetActiveLanguage(selectedId))
            },
            onRemoveLanguageClick = { lang ->
                myLanguagesViewModel.onIntent(MyLanguagesIntent.RequestRemoveLanguage(lang))
            },
            onConfirmRemoveLanguage = {
                myLanguagesViewModel.onIntent(MyLanguagesIntent.ConfirmRemoveLanguage)
            },
            onDismissRemoveDialog = {
                myLanguagesViewModel.onIntent(MyLanguagesIntent.DismissRemoveDialog)
            }
        )
    }

    DailyMissionDialog(
        state = state.dailyMissionState,
        onDismissRequest = { viewModel.onIntent(HomeIntent.DismissDailyMissionDialog) },
        onStartCamera = { word -> viewModel.onIntent(HomeIntent.StartDailyMissionCamera(word)) }
    )
}


