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
import com.iti.linguaquest.core.theme.AppColors
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(24.dp))
        ) {
            AsyncImage(
                model = word.imagePath,
                contentDescription = word.sourceWord,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.55f)
                            ),
                            startY = 80f
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(14.dp)
                    .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(50))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = word.category.uppercase(),
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = word.sourceWord,
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = word.translatedWord,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(14.dp)
                    .background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "${word.sourceLanguage}${stringResource(R.string.review_language_arrow)}${word.targetLanguage}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(300)) + slideInVertically(spring(), { it / 2 })
        ) {
            ReviewSectionCard(
                emoji = stringResource(R.string.review_section_sentence_emoji),
                label = stringResource(R.string.review_section_sentence_label),
                content = response.exampleSentence,
                accentColor = MaterialTheme.colorScheme.tertiary,
                background = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
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
                emoji = stringResource(R.string.review_section_translation_emoji),
                label = stringResource(R.string.review_section_translation_label),
                content = response.sentenceTranslation,
                accentColor = MaterialTheme.colorScheme.primary,
                background = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
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
                emoji = stringResource(R.string.review_section_memory_emoji),
                label = stringResource(R.string.review_section_memory_label),
                content = response.memoryTip,
                accentColor = MaterialTheme.colorScheme.onSurface,
                background = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
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
                emoji = stringResource(R.string.review_section_fun_emoji),
                label = stringResource(R.string.review_section_fun_label),
                content = response.funFact,
                accentColor = AppColors.SplashTopLeftColor,
                background = AppColors.SplashTopLeftColor.copy(alpha = 0.08f),
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


