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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.database.word.WordEntity

@Composable
fun WordCard(
    word: WordEntity,
    onWordClick: (Int, Rect) -> Unit,
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LinguaQuestTheme.colors.ProfileCardColor),
        border = BorderStroke(1.dp, LinguaQuestTheme.colors.ProfileCardBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = word.imagePath.takeIf { it.isNotBlank() },
                    contentDescription = word.sourceWord,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.lingo_gallery_defualt),
                    error = painterResource(id = R.drawable.lingo_gallery_defualt),
                    fallback = painterResource(id = R.drawable.lingo_gallery_defualt),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = word.category.uppercase(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                ) {
                    Text(
                        text = word.sourceWord,
                        color = LinguaQuestTheme.colors.blackColor,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = word.translatedWord,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .background(
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.14f),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text(

                        text = compactLanguageLabel(word.targetLanguage),
                        color = LinguaQuestTheme.colors.iconsColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = LinguaQuestTheme.colors.iconsColor,
                        modifier = Modifier.height(10.dp)
                    )
                    Text(
                        text = compactLanguageLabel(word.sourceLanguage),
                        color = LinguaQuestTheme.colors.iconsColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
private fun compactLanguageLabel(language: String): String {
    val cleaned = language.trim().filter { it.isLetterOrDigit() }
    if (cleaned.isBlank()) return "--"
    return cleaned.take(2).uppercase()
}
