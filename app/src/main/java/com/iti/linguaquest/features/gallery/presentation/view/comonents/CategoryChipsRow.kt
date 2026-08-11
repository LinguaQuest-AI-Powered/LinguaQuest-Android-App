package com.iti.linguaquest.features.gallery.presentation.view.comonents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun CategoryChipsRow(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            
            val backgroundColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surface
            }
            
            val contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
            
            val lowerCategory = category.trim().lowercase()

            val displayCategory = when (lowerCategory) {
                "beginner", "easy", "سهل" -> stringResource(R.string.easy).uppercase()
                "intermediate", "medium", "متوسط" -> stringResource(R.string.medium).uppercase()
                "advanced", "hard", "صعب" -> stringResource(R.string.hard).uppercase()
                "all items", "الكل", "all" -> stringResource(R.string.all_filter)
                else -> category.uppercase()
            }
            
            val dotColor = when (lowerCategory) {
                "beginner", "easy", "سهل" -> LinguaQuestTheme.colors.SuccessAccent
                "intermediate", "medium", "متوسط" -> LinguaQuestTheme.colors.Amber
                "advanced", "hard", "صعب" -> LinguaQuestTheme.colors.ErrorAccent
                else -> Color.Transparent
            }
            
            val borderModifier = if (!isSelected) {
                Modifier.border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), RoundedCornerShape(50))
            } else {
                Modifier
            }

            Box(
                modifier = Modifier
                    .then(borderModifier)
                    .clip(RoundedCornerShape(50))
                    .background(backgroundColor)
                    .clickable { onCategorySelected(category) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = displayCategory,
                        color = contentColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    if (dotColor != Color.Transparent) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(dotColor)
                        )
                    }
                }
            }
        }
    }
}
