package com.iti.linguaquest.features.leaderboard.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState

import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LingoSpinningIcon
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.leaderboard.domain.model.Leaderboard
import com.iti.linguaquest.features.leaderboard.domain.model.LeaderboardEntry
import com.iti.linguaquest.features.leaderboard.presentation.view.components.LeaderboardListItem
import com.iti.linguaquest.features.leaderboard.presentation.view.components.PodiumSection

@Composable
fun LeaderboardContent(
    modifier: Modifier = Modifier,
    leaderboard: Leaderboard,
    onBack: () -> Unit,
    onLoadMore: () -> Unit = {},
    isLoadingMore: Boolean = false,
    endReached: Boolean = true,

    ) {
    val listState = rememberLazyListState()




    val animatedItemIds = remember { mutableSetOf<Int>() }
    val shouldLoadMore by remember(endReached, isLoadingMore) {
        derivedStateOf {
            if (endReached || isLoadingMore) {
                false
            } else {
                val layoutInfo = listState.layoutInfo
                val totalItems = layoutInfo.totalItemsCount
                val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                totalItems > 0 && lastVisibleIndex >= totalItems - 1
            }
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 8.dp, bottom = 24.dp)
    ) {
        LinguaQuestScreenTopBar(
            title = stringResource(id = R.string.leaderboard_title),
            onBackClicked = onBack
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(8.dp))

                PodiumSection(
                    topThree = leaderboard.topThree,
                    animatedIds = animatedItemIds
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            itemsIndexed(
                items = leaderboard.entries,
                key = { _, entry -> entry.userId }
            ) { index, entry ->
                LeaderboardListItem(
                    entry = entry,
                    index = index,
                    animatedIds = animatedItemIds
                )
            }

            if (isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        LingoSpinningIcon()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardContentPreview() {
    LinguaQuestTheme {
        LeaderboardContent(
            leaderboard = Leaderboard(
                myRank = 100,
                topThree = listOf(
                    LeaderboardEntry(
                        rank = 1,
                        userId = 1,
                        username = "Marco Polo",
                        photoUrl = null,
                        level = 20,
                        xp = 4250,
                        isCurrentUser = false,
                    ),
                    LeaderboardEntry(
                        rank = 2,
                        userId = 2,
                        username = "Amelia",
                        photoUrl = null,
                        level = 19,
                        xp = 3890,
                        isCurrentUser = false,
                    ),
                    LeaderboardEntry(
                        rank = 3,
                        userId = 3,
                        username = "Ibn Battuta",
                        photoUrl = null,
                        level = 18,
                        xp = 3420,
                        isCurrentUser = false,
                    )
                ),
                entries = listOf(
                    LeaderboardEntry(
                        rank = 98,
                        userId = 4,
                        username = "Ferdinand M.",
                        photoUrl = null,
                        level = 13,
                        xp = 2900,
                        isCurrentUser = false,
                    ),
                    LeaderboardEntry(
                        rank = 99,
                        userId = 5,
                        username = "Sacagawea",
                        photoUrl = null,
                        level = 13,
                        xp = 2750,
                        isCurrentUser = false,
                    ),
                    LeaderboardEntry(
                        rank = 100,
                        userId = 6,
                        username = "Explorer Sam",
                        photoUrl = null,
                        level = 12,
                        xp = 3150,
                        isCurrentUser = true,
                    )
                )
            ),
            onBack = {}
        )
    }
}
