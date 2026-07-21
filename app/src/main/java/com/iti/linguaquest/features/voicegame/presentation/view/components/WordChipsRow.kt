package com.iti.linguaquest.features.voicegame.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val SuccessGreen = Color(0xFF3E8E5A)
private val WrongChipBg = Color(0xFFE8DDC9)
private val WrongChipText = Color(0xFF8B6F47)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WordChipsRow(correctWords: List<String>, wrongWords: List<String>) {
    // correctWords is the prefix and wrongWords the suffix of the original sentence
    // split (see VoiceGameViewModel.generateFakeResult), so this order reconstructs
    // the original sentence — matches the mockup's single mixed-order row.
    val orderedWords = correctWords.map { it to true } + wrongWords.map { it to false }
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        orderedWords.forEach { (word, isCorrect) ->
            val bg = if (isCorrect) SuccessGreen.copy(alpha = 0.15f) else WrongChipBg
            val fg = if (isCorrect) SuccessGreen else WrongChipText
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(bg)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(word, color = fg, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.width(4.dp))
                Icon(
                    if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Info,
                    contentDescription = null,
                    tint = fg,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}