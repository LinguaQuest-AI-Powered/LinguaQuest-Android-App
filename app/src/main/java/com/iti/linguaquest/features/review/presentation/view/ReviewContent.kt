package com.iti.linguaquest.features.review.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.ErrorView
import com.iti.linguaquest.core.sharedComponents.LoadingView
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.features.review.presentation.contract.ReviewIntent
import com.iti.linguaquest.features.review.presentation.contract.ReviewState
import com.iti.linguaquest.features.review.presentation.view.components.ReviewStoryScene
import com.iti.linguaquest.features.review.presentation.view.model.ReviewSectionIds
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ReviewStoryContent(
    state: ReviewState,
    onIntent: (ReviewIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    var revealedScenes by remember(state.word, state.aiResponse, state.isLoading, state.errorMessage) {
        mutableIntStateOf(0)
    }

    LaunchedEffect(state.word, state.aiResponse, state.isLoading, state.errorMessage) {
        revealedScenes = 0
        if (!state.isLoading && state.errorMessage == null && state.aiResponse != null && state.word != null) {
            repeat(5) { index ->
                delay(360L.milliseconds)
                revealedScenes = index + 1
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.22f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                LinguaQuestScreenTopBar(
                    title = stringResource(R.string.review_screen_title),
                    onBackClicked = { onIntent(ReviewIntent.BackClicked) }
                )
            }

            when {
                state.isLoading -> {
                    item {
                        LoadingView()
                    }
                }

                state.errorMessage != null -> {
                    item {
                        ErrorView(
                            message = state.errorMessage,
                            onRetry = { onIntent(ReviewIntent.RetryClicked) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp)
                        )
                    }
                }

                state.aiResponse != null && state.word != null -> {
                    val word = state.word
                    val response = state.aiResponse

                    item {
                        ReviewStoryScene(
                            mascotRes = R.drawable.lingo_hello_review,
                            title = stringResource(R.string.review_section_welcome_label),
                            message = stringResource(
                                R.string.review_section_welcome_message,
                                word.sourceWord
                            ),
                            accentColor = MaterialTheme.colorScheme.tertiary,
                            flipped = false,
                            visible = revealedScenes >= 1,
                            showSpeakButton = false
                        )
                    }

                    item {
                        ReviewStoryScene(
                            mascotRes = R.drawable.lingo_sentenc_review,
                            title = stringResource(R.string.review_section_sentence_label),
                            message = response.exampleSentence,
                            accentColor = MaterialTheme.colorScheme.primary,
                            flipped = false,
                            visible = revealedScenes >= 2,
                            onSpeak = {
                                onIntent(
                                    ReviewIntent.SpeakSection(
                                        text = response.exampleSentence,
                                        language = word.sourceLanguage,
                                        sectionId = ReviewSectionIds.SENTENCE
                                    )
                                )
                            }
                        )
                    }

                    item {
                        ReviewStoryScene(
                            mascotRes = R.drawable.lingo_translation_review,
                            title = stringResource(R.string.review_section_translation_label),
                            message = response.sentenceTranslation,
                            accentColor = MaterialTheme.colorScheme.primary,
                            flipped = true,
                            visible = revealedScenes >= 3,
                            contentStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            onSpeak = {
                                onIntent(
                                    ReviewIntent.SpeakSection(
                                        text = response.sentenceTranslation,
                                        language = word.targetLanguage,
                                        sectionId = ReviewSectionIds.TRANSLATION
                                    )
                                )
                            }
                        )
                    }

                    item {
                        ReviewStoryScene(
                            mascotRes = R.drawable.lingo_memory_review,
                            title = stringResource(R.string.review_section_memory_label),
                            message = response.memoryTip,
                            accentColor = MaterialTheme.colorScheme.tertiary,
                            flipped = false,
                            visible = revealedScenes >= 4,
                            onSpeak = {
                                onIntent(
                                    ReviewIntent.SpeakSection(
                                        text = response.memoryTip,
                                        language = word.sourceLanguage,
                                        sectionId = ReviewSectionIds.MEMORY
                                    )
                                )
                            }
                        )
                    }

                    item {
                        ReviewStoryScene(
                            mascotRes = R.drawable.lingo_did_you_review,
                            title = stringResource(R.string.review_section_fun_label),
                            message = response.funFact,
                            accentColor = MaterialTheme.colorScheme.primary,
                            flipped = true,
                            visible = revealedScenes >= 5,
                            onSpeak = {
                                onIntent(
                                    ReviewIntent.SpeakSection(
                                        text = response.funFact,
                                        language = word.sourceLanguage,
                                        sectionId = ReviewSectionIds.FUN_FACT
                                    )
                                )
                            }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}