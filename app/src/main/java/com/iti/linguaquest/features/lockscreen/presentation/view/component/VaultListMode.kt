package com.iti.linguaquest.features.lockscreen.presentation.view.component

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultListMode(
    words: List<LockScreenWord>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onNavigateToReview: (LockScreenWord) -> Unit,
    tts: TextToSpeech?
) {
    val filteredWords = words.filter {
        it.word.contains(searchQuery, ignoreCase = true) ||
                it.translation.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(52.dp),
            shape = RoundedCornerShape(100.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    text = stringResource(R.string.lockscreen_search_placeholder),
                    color = LinguaQuestTheme.colors.titleAndCationsColor.copy(alpha = 0.4f)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.cd_search),
                    tint = LinguaQuestTheme.colors.titleAndCationsColor.copy(alpha = 0.4f)
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (filteredWords.isEmpty()) {
            EmptyVaultState(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                imageRes = R.drawable.lingo_searching,
                title = stringResource(R.string.lockscreen_no_words_found_title),
                subtitle = stringResource(R.string.lockscreen_no_words_found_subtitle)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 24.dp,
                    top = 0.dp,
                    end = 24.dp,
                    bottom = 100.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredWords, key = { it.id }) { word ->
                    VaultListItem(
                        word = word,
                        onSpeakClick = {
                            tts?.speak(word.word, TextToSpeech.QUEUE_FLUSH, null, "lockscreen_tts")
                        },
                        onReviewClick = {
                            onNavigateToReview(word)
                        }
                    )
                }
            }
        }
    }
}
