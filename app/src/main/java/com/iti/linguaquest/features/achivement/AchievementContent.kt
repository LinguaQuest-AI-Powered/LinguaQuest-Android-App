package com.iti.linguaquest.features.achivement

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.utils.ShareTopBar
import com.iti.linguaquest.features.achivement.components.AchievementBottomBar
import com.iti.linguaquest.features.achivement.components.AchievementGridItem
import com.iti.linguaquest.features.achivement.components.AchievementHeader
import com.iti.linguaquest.features.achivement.components.AchievementTabs
import com.iti.linguaquest.features.achivement.model.AchievementItem
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementContent(
    achievements: List<AchievementItem>,
    onBackClick: () -> Unit,
    onClaimClick: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    var headerVisible by remember { mutableStateOf(false) }
    var tabsVisible   by remember { mutableStateOf(false) }
    var gridVisible   by remember { mutableStateOf(false) }

    val gridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        delay(50.milliseconds)
        headerVisible = true
        delay(50.milliseconds)
        tabsVisible = true
        delay(30.milliseconds)
        gridVisible = true
    }

    val filteredAchievements = when (selectedTab) {
        1    -> achievements.filter { it.isEarned }
        2    -> achievements.filter { !it.isEarned }
        else -> achievements
    }

    val showStickyTabs by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 1 ||
                    (gridState.firstVisibleItemIndex == 1 && gridState.firstVisibleItemScrollOffset > 0)
        }
    }

    Scaffold(
        bottomBar = {
            AchievementBottomBar(
                earnedCount    = achievements.count { it.isEarned },
                inProgressCount = achievements.count { !it.isEarned },
                xpGained       = 240,
                onClaimClick   = onClaimClick
            )
        },
        containerColor = AppColors.Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.Background)
                .statusBarsPadding()
        ) {
            ShareTopBar(
                title       = R.string.achievements_title,
                onBackClick = onBackClick
            )

            Box(modifier = Modifier.weight(1f)) {
                LazyVerticalGrid(
                    state                 = gridState,
                    columns               = GridCells.Fixed(2),
                    modifier              = Modifier.fillMaxSize(),
                    contentPadding        = PaddingValues(
                        top    = 16.dp,
                        bottom = paddingValues.calculateBottomPadding() + 16.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement   = Arrangement.spacedBy(16.dp)
                ) {
                    item(span = { GridItemSpan(2) }) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = headerVisible,
                            enter   = fadeIn(tween(250)) + slideInVertically(
                                initialOffsetY = { -30 },
                                animationSpec  = tween(250)
                            )
                        ) {
                            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                                AchievementHeader()
                            }
                        }
                    }

                    item(span = { GridItemSpan(2) }) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = tabsVisible,
                            enter   = fadeIn(tween(220)) + slideInVertically(
                                initialOffsetY = { -20 },
                                animationSpec  = tween(220)
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 4.dp, bottom = 4.dp)
                            ) {
                                AchievementTabs(
                                    selectedTab   = selectedTab,
                                    onTabSelected = { selectedTab = it }
                                )
                            }
                        }
                    }

                    if (gridVisible) {
                        items(filteredAchievements, key = { it.id }) { item ->
                            val index = filteredAchievements.indexOf(item)
                            AchievementGridItem(
                                item  = item,
                                index = index,
                                modifier = Modifier.padding(
                                    start = if (index % 2 == 0) 16.dp else 0.dp,
                                    end   = if (index % 2 == 1) 16.dp else 0.dp
                                )
                            )
                        }
                    }
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible  = showStickyTabs,
                    enter    = fadeIn(tween(150)),
                    exit     = fadeOut(tween(150)),
                    modifier = Modifier.align(Alignment.TopCenter)
                ) {
                    Surface(color = AppColors.Background) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(top = 4.dp, bottom = 4.dp)
                        ) {
                            AchievementTabs(
                                selectedTab   = selectedTab,
                                onTabSelected = { selectedTab = it }
                            )
                        }
                    }
                }
            }
        }
    }
}