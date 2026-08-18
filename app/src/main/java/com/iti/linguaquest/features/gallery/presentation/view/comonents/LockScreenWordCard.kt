package com.iti.linguaquest.features.gallery.presentation.view.comonents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord

@Composable
fun LockScreenWordCard(
    word: LockScreenWord,
    onWordClick: (Int, Rect) -> Unit,
    onSpeakClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardBounds = remember(word.id) { arrayOf(Rect.Zero) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                cardBounds[0] = coordinates.boundsInRoot()
            }
            .clickable { onWordClick(word.id, cardBounds[0]) },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = LinguaQuestTheme.colors.ProfileCardColor
        ),
        border = BorderStroke(1.dp, LinguaQuestTheme.colors.ProfileCardBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusPill(
                    text = word.difficulty.ifBlank { word.proficiencyLevel.ifBlank { "WORD" } },
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    textColor = MaterialTheme.colorScheme.primary
                )
                
                IconButton(
                    onClick = onSpeakClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
                        contentDescription = "Speak",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = word.word,
                color = LinguaQuestTheme.colors.blackColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = word.meaning.ifBlank { word.translation },
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = word.exampleSentence,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LanguageBadge(label = compactLanguageLabel(word.targetLanguage))
                LanguageBadge(label = compactLanguageLabel(word.nativeLanguage))
            }
        }
    }
}

@Composable
private fun StatusPill(
    text: String,
    containerColor: androidx.compose.ui.graphics.Color,
    textColor: androidx.compose.ui.graphics.Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(containerColor)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text.uppercase(),
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

@Composable
private fun LanguageBadge(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

private fun compactLanguageLabel(language: String): String {
    val lower = language.trim().lowercase()
    return when {
        lower in listOf("arabic", "ar", "العربية") -> "AR"
        lower in listOf("english", "en", "الإنجليزية", "الانجليزية") -> "EN"
        lower in listOf("spanish", "español", "es", "الإسبانية", "الاسبانية") -> "ES"
        lower in listOf("french", "français", "fr", "الفرنسية") -> "FR"
        lower in listOf("german", "deutsch", "de", "الألمانية", "الالمانية") -> "DE"
        lower in listOf("italian", "italiano", "it", "الإيطالية", "الايطالية") -> "IT"
        lower in listOf("chinese", "中文", "zh", "الصينية") -> "ZH"
        lower in listOf("japanese", "日本語", "ja", "اليابانية") -> "JA"
        lower in listOf("korean", "한국어", "ko", "الكورية") -> "KO"
        lower in listOf("portuguese", "português", "pt", "البرتغالية") -> "PT"
        else -> {
            val cleaned = lower.filter { it.isLetterOrDigit() }
            if (cleaned.isBlank()) "--" else cleaned.take(2).uppercase()
        }
    }
}
