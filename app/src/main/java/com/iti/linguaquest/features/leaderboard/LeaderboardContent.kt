package com.iti.linguaquest.features.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ShareTopBar
import com.iti.linguaquest.features.leaderboard.components.LeaderboardListItem
import com.iti.linguaquest.features.leaderboard.components.PodiumSection
import com.iti.linguaquest.features.profile.presentation.model.LeaderboardEntry

@Composable
fun LeaderboardContent(
    entries: List<LeaderboardEntry>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.Background)
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
            val topThree = entries.filter { it.rank <= 3 }.sortedBy { it.rank }
            val rest = entries.filter { it.rank > 3 }.sortedBy { it.rank }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                PodiumSection(topThree = topThree)
                Spacer(modifier = Modifier.height(16.dp))
            }

            itemsIndexed(rest) { index, entry ->
                LeaderboardListItem(entry = entry, index = index)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardContentPreview() {
    LinguaQuestTheme {
        LeaderboardContent(
            entries = listOf(
                LeaderboardEntry(1, "Marco Polo", "Legend", 4250, R.drawable.lingo_splash_parrot),
                LeaderboardEntry(2, "Amelia", "Explorer", 3890, R.drawable.lingo_writing),
                LeaderboardEntry(3, "Ibn Battuta", "Traveler", 3420, R.drawable.lingo_splash_1),
                LeaderboardEntry(98, "Ferdinand M.", "Novice", 2900, R.drawable.lingo_splash_2),
                LeaderboardEntry(99, "Sacagawea", "Guide", 2750, R.drawable.lingo_splash_3),
                LeaderboardEntry(100, "Explorer Sam", "Adventurer", 3150, R.drawable.lingo_splash_4, isCurrentUser = true),
                LeaderboardEntry(101, "Zheng He", "Admiral", 2600, R.drawable.lingo_splash_5),
                LeaderboardEntry(102, "Xuanzang", "Monk", 2550, R.drawable.lingo_splash_6)
            ),
            onBack = {}
        )
    }
}
