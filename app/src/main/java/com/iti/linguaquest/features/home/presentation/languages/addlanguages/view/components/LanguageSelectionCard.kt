package com.iti.linguaquest.features.home.presentation.languages.addlanguages.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.home.presentation.languages.addlanguages.contract.LanguageUiItem

@Composable
fun LanguageSelectionCard(
    language: LanguageUiItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEffectivelySelected = isSelected || language.isAdded
    val borderColor = if (isEffectivelySelected) MaterialTheme.colorScheme.primary else LinguaQuestTheme.colors.textFieldBorder
    val checkmarkTint = if (language.isAdded) LinguaQuestTheme.colors.textFieldPlaceholder else LinguaQuestTheme.colors.SuccessAccent
    val backgroundColor = if (language.isAdded) LinguaQuestTheme.colors.textFieldFill else LinguaQuestTheme.colors.whiteColor

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .border(
                width = if (isEffectivelySelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(onClick = {
                if (!language.isAdded) {
                    onClick()
                }
            })
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            if (isEffectivelySelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = stringResource(R.string.cd_selected),
                    tint = checkmarkTint,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(2.dp, LinguaQuestTheme.colors.textFieldBorder, CircleShape)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(LinguaQuestTheme.colors.whiteColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = language.flagEmoji,
                    style = AppTextStyles.Word.copy(fontSize = 40.sp, textAlign = TextAlign.Center)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = language.name,
                style = AppTextStyles.Translation.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = LinguaQuestTheme.colors.titleAndCationsColor
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageSelectionCardPreview() {
    LinguaQuestTheme {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            LanguageSelectionCard(
                language = LanguageUiItem(
                    id = 1,
                    name = "Spanish",
                    flagEmoji = "🇪🇸",
                    isAdded = false
                ),
                isSelected = false,
                onClick = {},
                modifier = Modifier
                    .width(160.dp)
                    .height(180.dp)
            )
            LanguageSelectionCard(
                language = LanguageUiItem(
                    id = 2,
                    name = "French",
                    flagEmoji = "🇫🇷",
                    isAdded = true
                ),
                isSelected = false,
                onClick = {},
                modifier = Modifier
                    .width(160.dp)
                    .height(180.dp)
            )
        }
    }
}
