package com.iti.linguaquest.features.home.presentation.view


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
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
import com.iti.linguaquest.core.sharedComponents.offline.NoInternetMiniPopup
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.features.home.presentation.contract.HomeEffect
import com.iti.linguaquest.features.home.presentation.contract.HomeIntent
import com.iti.linguaquest.features.home.presentation.contract.HomeState
import com.iti.linguaquest.features.home.presentation.languages.component.MyLanguagesBottomSheet
import com.iti.linguaquest.features.home.presentation.languages.contract.MyLanguagesEffect
import com.iti.linguaquest.features.home.presentation.languages.contract.MyLanguagesIntent
import com.iti.linguaquest.features.home.presentation.languages.viewmodel.MyLanguagesViewModel
import com.iti.linguaquest.features.home.utils.calculatePopupOffset
import com.iti.linguaquest.features.home.presentation.view.components.ExploreWorldsSection
import com.iti.linguaquest.features.home.presentation.view.components.LanguageProgressCard
import com.iti.linguaquest.features.home.presentation.view.components.VoicePractiseCard
import com.iti.linguaquest.features.home.presentation.view.components.WorldItem
import com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components.CoinRainOverlay
import com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components.DailyRewardCard
import com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components.DailyStreakBonusBanner
import com.iti.linguaquest.features.home.presentation.viewModel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.time.Duration.Companion.milliseconds

import com.iti.linguaquest.core.sharedComponents.ErrorView
import com.iti.linguaquest.core.sharedComponents.LoadingView
import androidx.compose.ui.res.stringResource

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToVoiceGame: () -> Unit,
    onNavigateToRoleplayList: () -> Unit,
    onNavigateToAllWorlds: () -> Unit,
    onNavigateToWorldMap: (Int) -> Unit,
    onWorldMapClick: () -> Unit = {},
    onNavigateToAddLanguages: () -> Unit,
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
    val fallZoneHeight = (configuration.screenHeightDp / 2).dp
    var bannerHeightPx by remember { mutableFloatStateOf(0f) }

    fun guardOnline(anchor: Rect? = null, action: () -> Unit) {
        if (isOnline) {
            action()
        } else {
            offlinePopupAnchor = anchor
            showOfflinePopup = true
        }
    }

    LaunchedEffect(state.xp, state.coins) {
        onHeaderDataChanged(state.xp, state.coins)
    }



    LaunchedEffect(state.isDailyRewardBannerVisible) {
        if (state.isDailyRewardBannerVisible) {
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
                is HomeEffect.NavigateToVoiceGame -> onNavigateToVoiceGame()
                is HomeEffect.NavigateToRoleplayList -> onNavigateToRoleplayList()
                is HomeEffect.NavigateToWorld -> onNavigateToWorldMap(effect.worldId)
                HomeEffect.NavigateToAllWorlds -> onNavigateToAllWorlds()
                is HomeEffect.NavigateToAddLanguages -> onNavigateToAddLanguages()
            }
        }
    }

    LaunchedEffect(Unit) {
        myLanguagesViewModel.effect.collectLatest { effect ->
            when (effect) {
                MyLanguagesEffect.NavigateToAddLanguages -> {
                    guardOnline {
                        viewModel.onIntent(HomeIntent.DismissLanguageBottomSheet)
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

        if (state.isLoading) {
            LoadingView()
        } else if (state.hasError) {
            ErrorView(
                message = stringResource(R.string.error_generic),
                onRetry = { viewModel.onIntent(HomeIntent.Retry) }
            )
        } else {

            HomeContent(
                state = state,
                onSeeMoreClick = { anchor ->
                    guardOnline(anchor) { viewModel.onIntent(HomeIntent.SeeMoreWorldsClicked) }
                },
                onWorldClick = { world, anchor ->
                    guardOnline(anchor) { viewModel.onIntent(HomeIntent.WorldClicked(world)) }
                },
                onStartVoiceClick = { anchor ->
                    guardOnline(anchor) { viewModel.onIntent(HomeIntent.StartVoicePractiseClicked) }
                },
                onRoleplayClick = { anchor ->
                    guardOnline(anchor) { viewModel.onIntent(HomeIntent.RoleplayCardClicked) }
                }
            )
        }

        FloatingActionButton(
            onClick = { guardOnline(fabBounds) { viewModel.onIntent(HomeIntent.FabClicked) } },
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
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

        AnimatedVisibility(
            visible = state.isDailyRewardBannerVisible,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            ) + fadeIn(tween(400)),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            ) + fadeOut(tween(300)),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(fallZoneHeight)
            ) {
                AnimatedVisibility(
                    visible = showCoinRain,
                    enter = fadeIn(tween(200)),
                    exit = fadeOut(tween(800)),
                    modifier = Modifier.matchParentSize()
                ) {
                    CoinRainOverlay(
                        modifier = Modifier.fillMaxSize(),
                        coinCount = 30,
                        startYPx = bannerHeightPx
                    )
                }

                DailyStreakBonusBanner(
                    onClick = {
                        soundPlayer.play(AppSound.DAILY_REWARD)
                        viewModel.onIntent(HomeIntent.DailyRewardBannerClicked)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }

        if (showOfflinePopup) {
            val popupOffset = remember(
                offlinePopupAnchor,
                offlinePopupSize,
                configuration.screenWidthDp,
                configuration.screenHeightDp
            ) {
                calculatePopupOffset(
                    anchor = offlinePopupAnchor,
                    popupSize = offlinePopupSize,
                    screenWidthDp = configuration.screenWidthDp,
                    screenHeightDp = configuration.screenHeightDp,
                    density = density
                )
            }

            NoInternetMiniPopup(
                isOnline = isOnline,
                modifier = Modifier
                    .offset { popupOffset }
                    .onSizeChanged { offlinePopupSize = it },
                onDismiss = {
                    showOfflinePopup = false
                    offlinePopupAnchor = null
                }
            )
        }
    }

    if (state.isDailyRewardDialogVisible) {
        Dialog(
            onDismissRequest = { viewModel.onIntent(HomeIntent.DismissDailyRewardDialog) },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                DailyRewardCard(
                    currentDay = state.dailyReward?.currentDay ?: 1,
                    rewardAmount = state.dailyReward?.rewardCoins ?: 0,
                    onClaimClick = {
                        soundPlayer.play(AppSound.COIN)
                        viewModel.onIntent(HomeIntent.ClaimDailyRewardClicked)
                    }
                )
            }
        }
    }

    if (state.isLanguageBottomSheetVisible) {
        MyLanguagesBottomSheet(
            languages = myLanguagesState.languages,
            isLoading = myLanguagesState.isLoading,
            isSettingActive = myLanguagesState.isSettingActive,
            onDismiss = { myLanguagesViewModel.onIntent(MyLanguagesIntent.Dismiss) },
            onAddNewLanguageClick = { myLanguagesViewModel.onIntent(MyLanguagesIntent.AddNewLanguageClicked) },
            onLanguageSelect = { selectedId ->
                myLanguagesViewModel.onIntent(MyLanguagesIntent.SetActiveLanguage(selectedId))
            }
        )
    }
}



@Composable
fun HomeContent(
    state: HomeState,
    onSeeMoreClick: (Rect) -> Unit,
    onWorldClick: (WorldItem, Rect) -> Unit,
    onStartVoiceClick: (Rect) -> Unit,
    onRoleplayClick: (Rect) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        state.languageProgress?.let { progress ->
            LanguageProgressCard(
                languageName = progress.languageName,
                level = progress.level,
                streakDays = progress.streakDays,
                progress = progress.progress,
                flagSource = progress.flagSource,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        if (state.worlds.isNotEmpty()) {
            ExploreWorldsSection(
                worlds = state.worlds,
                onSeeMoreClick = onSeeMoreClick,
                onWorldClick = onWorldClick,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        VoicePractiseCard(
            onStartClick = onStartVoiceClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        com.iti.linguaquest.features.home.presentation.view.components.RoleplayCard(
            onStartClick = onRoleplayClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
