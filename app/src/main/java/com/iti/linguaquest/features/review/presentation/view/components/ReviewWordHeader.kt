package com.iti.linguaquest.features.review.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.iti.linguaquest.R
import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun ReviewWordHeader(
    word: WordEntity,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {

         Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            AsyncImage(
                model = word.imagePath,
                contentDescription = word.sourceWord,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

             Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(50))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = word.category.uppercase(),
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

         Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = word.sourceWord,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = word.translatedWord,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

             Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${word.sourceLanguage}${stringResource(R.string.review_language_arrow)}${word.targetLanguage}",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


@Preview(showBackground = true, name = "ReviewWordHeader")
@Composable
private fun ReviewWordHeaderPreview() {
    LinguaQuestTheme {
        ReviewWordHeader(
            word = WordEntity(
                id = 1,
                sourceWord = "Apple",
                translatedWord = "تفاحة",
                sourceLanguage = "English",
                targetLanguage = "Arabic",
                category = "Food",
                imagePath = ""
            )
        )
    }
}
