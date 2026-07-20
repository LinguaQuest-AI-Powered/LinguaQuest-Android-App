package com.iti.linguaquest.features.leaderboard.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ShareTopBar
import com.iti.linguaquest.features.leaderboard.domain.model.Leaderboard
import com.iti.linguaquest.features.leaderboard.domain.model.LeaderboardEntry
import com.iti.linguaquest.features.leaderboard.presentation.view.components.LeaderboardListItem
import com.iti.linguaquest.features.leaderboard.presentation.view.components.PodiumSection

@Composable
fun LeaderboardContent(
    leaderboard: Leaderboard,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        ShareTopBar(
            title = R.string.leaderboard_title,
            onBackClick = onBack
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(8.dp))

                PodiumSection(
                    topThree = leaderboard.topThree
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            itemsIndexed(leaderboard.entries) { index, entry ->
                LeaderboardListItem(
                    entry = entry,
                    index = index
                )
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