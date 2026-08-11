package com.iti.linguaquest.features.home.presentation.languages.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LingoSpinningIcon
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.home.presentation.languages.contract.MyLanguageUiModel

@Composable
fun MyLanguageItem(
    language: MyLanguageUiModel,
    onClick: () -> Unit,
    onRemoveClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    isRemoving: Boolean = false,
    isEditMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    val alpha = if (isEditMode && language.isCurrent) 0.5f else 1f
    val backgroundColor = if (language.isCurrent && !isEditMode) LinguaQuestTheme.colors.ChipBackground else Color.Transparent
    val borderColor = if (language.isCurrent && !isEditMode) MaterialTheme.colorScheme.primary else Color.Transparent

    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(alpha)
            .clip(RoundedCornerShape(32.dp))
            .background(backgroundColor)
            .border(
                width = if (language.isCurrent && !isEditMode) 1.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(32.dp)
            )
            .clickable(enabled = enabled && !(isEditMode && language.isCurrent), onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(LinguaQuestTheme.colors.whiteColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = language.flagEmoji,
                style = AppTextStyles.LessonTitle.copy(fontSize = 28.sp, textAlign = TextAlign.Center)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = language.name,
                style = AppTextStyles.LessonTitle.copy(
                    fontWeight = FontWeight.Bold,
                    color = LinguaQuestTheme.colors.titleAndCationsColor
                )
            )
            Text(
                text = stringResource(R.string.my_languages_level_format, language.level),
                style = AppTextStyles.Caption.copy(color = LinguaQuestTheme.colors.textFieldPlaceholder)
            )
        }

        if (isRemoving) {
            LingoSpinningIcon(size = 24.dp)
        } else if (isEditMode && !language.isCurrent && onRemoveClick != null) {
            IconButton(onClick = onRemoveClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.remove),
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(22.dp)
                )
            }
        } else if (!isEditMode && language.isCurrent) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = stringResource(R.string.cd_selected),
                tint = LinguaQuestTheme.colors.SuccessAccent,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
