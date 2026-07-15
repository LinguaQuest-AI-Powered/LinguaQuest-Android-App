package com.iti.linguaquest.features.onBoarding.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.features.onBoarding.contract.ProficiencyLevel

@Composable
 fun LevelCard(level: ProficiencyLevel, isSelected: Boolean, onClick: () -> Unit) {
    val iconRes = when (level) {
        ProficiencyLevel.BEGINNER -> R.drawable.ic_leaf
        ProficiencyLevel.INTERMEDIATE -> R.drawable.ic_star
        ProficiencyLevel.ADVANCED -> R.drawable.ic_medal
    }
    val iconBg = if (level == ProficiencyLevel.BEGINNER)
        Color(0xFF4DD0C8)
    else
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
    val iconTint = if (level == ProficiencyLevel.BEGINNER) Color.White else MaterialTheme.colorScheme.secondary

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = if (isSelected) 3.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = iconBg, modifier = Modifier.size(44.dp)) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(painter = painterResource(iconRes), contentDescription = null, tint = iconTint)
                }
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(text = level.displayName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = level.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}