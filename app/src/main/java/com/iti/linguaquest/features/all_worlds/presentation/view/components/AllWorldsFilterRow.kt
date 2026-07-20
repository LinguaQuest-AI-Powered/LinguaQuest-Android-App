package com.iti.linguaquest.features.all_worlds.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.theme.AppColors.BrownText
import com.iti.linguaquest.core.theme.AppColors.PrimaryColor
import com.iti.linguaquest.features.home.presentation.view.components.WorldDifficulty

import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R

@Composable
fun AllWorldsFilterRow(
    selectedFilter: WorldDifficulty?,
    onFilterSelected: (WorldDifficulty?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            FilterChip(
                text = stringResource(R.string.all_filter),
                isSelected = selectedFilter == null,
                dotColor = null,
                onClick = { onFilterSelected(null) }
            )
        }
        
        items(WorldDifficulty.entries.toTypedArray()) { difficulty ->
            val stringResId = when (difficulty) {
                WorldDifficulty.EASY -> R.string.easy
                WorldDifficulty.MEDIUM -> R.string.medium
                WorldDifficulty.HARD -> R.string.hard
            }
            FilterChip(
                text = stringResource(stringResId).lowercase().replaceFirstChar { it.uppercase() },
                isSelected = selectedFilter == difficulty,
                dotColor = difficulty.badgeColor,
                onClick = { onFilterSelected(difficulty) }
            )
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    dotColor: Color?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) PrimaryColor else com.iti.linguaquest.core.theme.AppColors.White
    val textColor = if (isSelected) com.iti.linguaquest.core.theme.AppColors.White else BrownText
    val borderColor = if (isSelected) PrimaryColor else com.iti.linguaquest.core.theme.AppColors.SocialBorderColor

    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .border(width = 2.dp, color = borderColor, shape = CircleShape)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (dotColor != null) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
