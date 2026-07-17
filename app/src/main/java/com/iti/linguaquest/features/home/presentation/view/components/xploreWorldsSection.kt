package com.iti.linguaquest.features.home.presentation.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme


@Composable
fun ExploreWorldsSection(
    worlds: List<WorldItem>,
    onSeeMoreClick: () -> Unit,
    onWorldClick: (WorldItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.explore_worlds),
                style = AppTextStyles.SectionTitle.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = LinguaQuestTheme.colors.blackColor
            )
            Text(
                text = stringResource(R.string.see_more),
                style = AppTextStyles.LessonTitle.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = LinguaQuestTheme.colors.iconsColor,

                modifier = Modifier
                    .wrapContentSize()
                    .clickable { onSeeMoreClick() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(worlds, key = { it.id }) { world ->
                WorldCard(world = world, onClick = { onWorldClick(world) })
            }
        }
    }
}


