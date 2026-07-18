package com.iti.linguaquest.features.review.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import com.iti.linguaquest.features.review.domain.model.AIReviewResponse
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.sharedComponents.ErrorView
import com.iti.linguaquest.core.sharedComponents.LoadingView
import com.iti.linguaquest.core.theme.LinguaQuestTheme
 import com.iti.linguaquest.features.review.presentation.contract.ReviewIntent
import com.iti.linguaquest.features.review.presentation.contract.ReviewState
import com.iti.linguaquest.features.review.presentation.view.components.AIResponseCard
import com.iti.linguaquest.features.review.presentation.view.components.ReviewTopBar

@Composable
fun ReviewContent(
    state: ReviewState,
    onIntent: (ReviewIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

         ReviewTopBar(
            title = stringResource(R.string.review_screen_title),
            onBack = { onIntent(ReviewIntent.BackClicked) }
        )

        Spacer(modifier = Modifier.height(20.dp))

         when {
            state.isLoading -> {
                LoadingView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }

            state.errorMessage != null -> {
                ErrorView(
                    message = state.errorMessage,
                    onRetry = { onIntent(ReviewIntent.RetryClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                )
            }

            state.aiResponse != null && state.word != null -> {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically(
                        animationSpec = spring(),
                        initialOffsetY = { it / 3 }
                    ),
                    exit = fadeOut()
                ) {
                    AIResponseCard(
                        response = state.aiResponse,
                        word = state.word,
                        speakingSectionId = state.speakingSectionId,
                        onIntent = onIntent
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}


private val previewWord = WordEntity(
    id = 1,
    sourceWord = "Apple",
    translatedWord = "تفاحة",
    sourceLanguage = "English",
    targetLanguage = "Arabic",
    category = "Food",
    imagePath = ""
)

private val previewResponse = AIReviewResponse(
    exampleSentence = "She ate a red apple for breakfast.",
    sentenceTranslation = "أكلت تفاحة حمراء على الفطور.",
    memoryTip = "Think of the apple emoji 🍎 – it starts the ABC, just like learning starts with basics.",
    funFact = "There are more than 7,500 known cultivars of apples grown around the world.",
    fullText = ""
)

@Preview(showBackground = true, name = "Review – Loading")
@Composable
private fun ReviewContentLoadingPreview() {
    LinguaQuestTheme {
        ReviewContent(
            state = ReviewState(isLoading = true),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Review – Error")
@Composable
private fun ReviewContentErrorPreview() {
    LinguaQuestTheme {
        ReviewContent(
            state = ReviewState(errorMessage = "Couldn't get AI review. Please try again."),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Review – Success")
@Composable
private fun ReviewContentSuccessPreview() {
    LinguaQuestTheme {
        ReviewContent(
            state = ReviewState(
                word = previewWord,
                aiResponse = previewResponse
            ),
            onIntent = {}
        )
    }
}


