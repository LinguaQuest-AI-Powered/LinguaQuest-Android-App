package com.iti.linguaquest.features.home.presentation.view


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.navigation.SharedBackgroundState
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.features.home.presentation.contract.HomeEffect
import com.iti.linguaquest.features.home.presentation.contract.HomeIntent
import com.iti.linguaquest.core.sharedComponents.state.DataStatus
import com.iti.linguaquest.core.sharedComponents.state.StatefulContentContainer
import com.iti.linguaquest.features.home.presentation.languages.mylanguages.contract.MyLanguagesEffect
import com.iti.linguaquest.features.home.presentation.languages.mylanguages.viewmodel.MyLanguagesViewModel
import com.iti.linguaquest.features.home.presentation.view.components.HomeContent
import com.iti.linguaquest.features.home.presentation.view.components.HomeFabs
import com.iti.linguaquest.features.home.presentation.view.components.HomeOverlays
import com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components.HomeDailyRewardBannerWrapper
import com.iti.linguaquest.features.home.presentation.viewModel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.time.Duration.Companion.milliseconds
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

    val scrollState = rememberScrollState()

    var showCoinRain by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
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

    var wasOffline by remember { mutableStateOf(!isOnline) }
    LaunchedEffect(isOnline) {
        if (isOnline && wasOffline) {
            viewModel.onIntent(HomeIntent.Refresh)
        }
        wasOffline = !isOnline
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
                        viewModel.onIntent(HomeIntent.AddNewLanguageClicked)
                    }
                }
                MyLanguagesEffect.DismissSheet -> {
                    viewModel.onIntent(HomeIntent.DismissLanguageBottomSheet)
                }
                MyLanguagesEffect.SwitchingLanguage -> {
                    viewModel.onIntent(HomeIntent.DismissLanguageBottomSheet)
                    viewModel.onIntent(HomeIntent.PrepareLanguageSwitch)
                }
                is MyLanguagesEffect.LanguageSwitchFailed -> {
                    viewModel.onIntent(HomeIntent.CancelLanguageSwitch)
                }
            }
        }
    }



    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onIntent(HomeIntent.ScreenResumed)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        StatefulContentContainer(
            dataStatus = state.dataStatus,
            onRetry = { viewModel.onIntent(HomeIntent.Retry) },
            onRefresh = { viewModel.onIntent(HomeIntent.Refresh) },
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
                },
                scrollState = scrollState
            )
        }

        if (state.dataStatus is DataStatus.Loaded || state.dataStatus is DataStatus.Refreshing) {
            HomeFabs(
                onDailyMissionClick = { anchor -> guardOnline(anchor) { viewModel.onIntent(HomeIntent.TriggerDailyMission) } },
                onWorldMapClick = { anchor -> guardOnline(anchor) { viewModel.onIntent(HomeIntent.FabClicked) } },
                scrollState = scrollState,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
            )
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

    HomeOverlays(
        state = state,
        myLanguagesState = myLanguagesState,
        onHomeIntent = viewModel::onIntent,
        onMyLanguagesIntent = myLanguagesViewModel::onIntent
    )
}


