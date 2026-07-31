package com.iti.linguaquest.features.review.presentation.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.iti.linguaquest.R
import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.review.domain.model.AIReviewResponse
import com.iti.linguaquest.features.review.presentation.contract.ReviewIntent
import com.iti.linguaquest.features.review.presentation.view.model.ReviewSectionIds


@Composable
fun AIResponseCard(
    response: AIReviewResponse,
    word: WordEntity,
    speakingSectionId: String?,
    onIntent: (ReviewIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "speak_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "speak_scale"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        ReviewWordHeader(word = word)

        Spacer(modifier = Modifier.height(18.dp))

        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(300)) + slideInVertically(spring(), { it / 2 })
        ) {
            ReviewSectionCard(
                leadingIconRes = R.drawable.lingo_sentence,
                label = stringResource(R.string.review_section_sentence_label),
                content = response.exampleSentence,
                accentColor = MaterialTheme.colorScheme.primary,
                background = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.42f),
                isSpeaking = speakingSectionId == ReviewSectionIds.SENTENCE,
                pulseScale = pulseScale,
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

        Spacer(modifier = Modifier.height(10.dp))

        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(400)) + slideInVertically(spring(), { it / 2 })
        ) {
            ReviewSectionCard(
                leadingIconRes = R.drawable.lingo_translation,
                label = stringResource(R.string.review_section_translation_label),
                content = response.sentenceTranslation,
                accentColor = MaterialTheme.colorScheme.primary,
                background = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.48f),
                contentStyle = FontStyle.Italic,
                isSpeaking = speakingSectionId == ReviewSectionIds.TRANSLATION,
                pulseScale = pulseScale,
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

        Spacer(modifier = Modifier.height(10.dp))

        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(500)) + slideInVertically(spring(), { it / 2 })
        ) {
            ReviewSectionCard(
                leadingIconRes = R.drawable.lingo_memory_track,
                label = stringResource(R.string.review_section_memory_label),
                content = response.memoryTip,
                accentColor = MaterialTheme.colorScheme.secondary,
                background = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                isSpeaking = speakingSectionId == ReviewSectionIds.MEMORY,
                pulseScale = pulseScale,
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

        Spacer(modifier = Modifier.height(10.dp))

        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(600)) + slideInVertically(spring(), { it / 2 })
        ) {
            ReviewSectionCard(
                leadingIconRes = R.drawable.lingo_did_you_know,
                label = stringResource(R.string.review_section_fun_label),
                content = response.funFact,
                accentColor = LinguaQuestTheme.colors.splashTopLeftColor,
                background = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                isSpeaking = speakingSectionId == ReviewSectionIds.FUN_FACT,
                pulseScale = pulseScale,
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

@Preview(showBackground = true, name = "AIResponseCard – Idle")
@Composable
private fun AIResponseCardIdlePreview() {
    LinguaQuestTheme {
        AIResponseCard(
            response = previewResponse,
            word = previewWord,
            speakingSectionId = null,
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "AIResponseCard – Translation Speaking (Arabic)")
@Composable
private fun AIResponseCardTranslationSpeakingPreview() {
    LinguaQuestTheme {
        AIResponseCard(
            response = previewResponse,
            word = previewWord,
            speakingSectionId = ReviewSectionIds.TRANSLATION,
            onIntent = {}
        )
    }
}


