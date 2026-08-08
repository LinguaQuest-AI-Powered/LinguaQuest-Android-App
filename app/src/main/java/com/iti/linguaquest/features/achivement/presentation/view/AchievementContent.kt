package com.iti.linguaquest.features.achivement.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBarBackButtonStyle
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.features.achivement.presentation.view.components.AchievementBottomBar
import com.iti.linguaquest.features.achivement.presentation.view.components.AchievementDetailBottomSheet
import com.iti.linguaquest.features.achivement.presentation.view.components.AchievementGridItem
import com.iti.linguaquest.features.achivement.presentation.view.components.AchievementHeader
import com.iti.linguaquest.features.achivement.presentation.view.components.AchievementTabs
import com.iti.linguaquest.features.achivement.presentation.view.model.AchievementItem

@Composable
fun AchievementContent(
    modifier: Modifier = Modifier,
    achievements: List<AchievementItem>,
    earnedCount: Int = 0,
    inProgressCount: Int = 0,
    xpGained: Int = 0,
    onBackClick: () -> Unit,
    onClaimClick: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedAchievement by remember { mutableStateOf<AchievementItem?>(null) }
    val animatedItemIds = remember { mutableSetOf<Int>() }

    val gridState = rememberLazyGridState()

    val filteredAchievements = when (selectedTab) {
        1 -> achievements.filter { it.isEarned }
        2 -> achievements.filter { !it.isEarned }
        else -> achievements
    }

    val showStickyTabs by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 1 ||
                    (gridState.firstVisibleItemIndex == 1 && gridState.firstVisibleItemScrollOffset > 0)
        }
    }

    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        modifier = modifier,
        bottomBar = {
            AchievementBottomBar(
                earnedCount = earnedCount,
                inProgressCount = inProgressCount,
                xpGained = xpGained,
                onClaimClick = onClaimClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
        ) {
            LinguaQuestScreenTopBar(
                title = stringResource(id = R.string.achievements_title),
                onBackClicked = onBackClick,
                isTitleCentered = true,
                containerColor = Color.Transparent,
                titleColor = LocalLinguaQuestColors.current.BrownText,
                titleTextStyle = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                ),
                showDivider = true,
                dividerSpacing = 16.dp,
                applyStatusBarsPadding = false,
                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 0.dp),
                backButtonStyle = LinguaQuestScreenTopBarBackButtonStyle.Circular,
                backButtonSize = 40.dp,
                backButtonBackgroundColor = LocalLinguaQuestColors.current.whiteColor,
                backButtonContentColor = LocalLinguaQuestColors.current.OrangeActive,
                backButtonIconSize = 18.dp
            )

            Box(modifier = Modifier.weight(1f)) {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = paddingValues.calculateStartPadding(layoutDirection),
                        top = 16.dp,
                        end = paddingValues.calculateEndPadding(layoutDirection),
                        bottom = paddingValues.calculateBottomPadding() + 16.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item(span = { GridItemSpan(2) }) {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            AchievementHeader()
                        }
                    }

                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 4.dp, bottom = 4.dp)
                        ) {
                            AchievementTabs(
                                selectedTab = selectedTab,
                                onTabSelected = { selectedTab = it }
                            )
                        }
                    }

                    itemsIndexed(
                        items = filteredAchievements,
                        key = { _, item -> item.id }
                    ) { index, item ->
                        AchievementGridItem(
                            item = item,
                            index = index,
                            onClick = { selectedAchievement = item },
                            animatedIds = animatedItemIds,
                            modifier = Modifier.padding(
                                start = if (index % 2 == 0) 16.dp else 0.dp,
                                end = if (index % 2 == 1) 16.dp else 0.dp
                            )
                        )
                    }
                }

                Column(modifier = Modifier.align(Alignment.TopCenter)) {
                    AnimatedVisibility(
                        visible = showStickyTabs,
                        enter = fadeIn(tween(150)),
                        exit = fadeOut(tween(150))
                    ) {
                        Surface(color = MaterialTheme.colorScheme.background) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 4.dp, bottom = 4.dp)
                            ) {
                                AchievementTabs(
                                    selectedTab = selectedTab,
                                    onTabSelected = { selectedTab = it }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    selectedAchievement?.let { achievement ->
        AchievementDetailBottomSheet(
            achievement = achievement,
            onDismiss = { selectedAchievement = null }
        )
    }
}
