package com.iti.linguaquest.features.onBoarding.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.onBoarding.presentation.contract.levelContract.ProficiencyLevel

@Composable
fun LevelCard(level: ProficiencyLevel, isSelected: Boolean, onClick: () -> Unit) {
    val iconRes = when (level) {
        ProficiencyLevel.BEGINNER -> R.drawable.ic_leaf
        ProficiencyLevel.INTERMEDIATE -> R.drawable.ic_star
        ProficiencyLevel.ADVANCED -> R.drawable.ic_medal
    }
    val iconBg = if (isSelected)
        MaterialTheme.colorScheme.tertiary
    else
        LinguaQuestTheme.colors.cardLevelFilledColor
    val iconTint =
        if (isSelected) LinguaQuestTheme.colors.whiteColor else MaterialTheme.colorScheme.primary

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = if (isSelected) 3.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = iconBg, modifier = Modifier.size(44.dp)) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = stringResource(level.displayNameRes), style = AppTextStyles.SectionTitle,
                    color = LinguaQuestTheme.colors.blackColor
                )
                Text(
                    text = stringResource(level.descriptionRes),
                    style = AppTextStyles.Caption,
                    color = LinguaQuestTheme.colors.titleAndCationsColor
                )
            }
        }
    }
}