package com.iti.linguaquest.features.mindreader.presentation.view.contents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderIntent
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderState
import com.iti.linguaquest.features.mindreader.presentation.view.components.MindReaderAnswerButton
import com.iti.linguaquest.features.mindreader.presentation.view.components.MindReaderSpeechBubble

@Composable
fun ActiveGameContent(
    modifier: Modifier = Modifier,
    state: MindReaderState,
    onIntent: (MindReaderIntent) -> Unit
) {
    val progress = state.currentQuestionNumber.toFloat() / state.maxQuestions.coerceAtLeast(1)
    val percentComplete = (progress * 100).toInt()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LinguaQuestScreenTopBar(
            onBackClicked = { onIntent(MindReaderIntent.ReturnToHomeClicked) },
            showXp = true,
            xpCount = state.xpBalance,
            showCoins = true,
            coinsCount = state.coinBalance
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        id = R.string.mind_reader_question_progress,
                        state.currentQuestionNumber,
                        state.maxQuestions
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = LinguaQuestTheme.colors.BrownText
                )

                Text(
                    text = stringResource(id = R.string.mind_reader_progress_complete, percentComplete),
                    style = MaterialTheme.typography.labelMedium,
                    color = LinguaQuestTheme.colors.BrownText.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = AppColors.Teal,
                trackColor = AppColors.progressTrackColor,
                strokeCap = StrokeCap.Round
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MindReaderSpeechBubble(
                text = state.currentQuestion?.uppercase() ?: ""
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFFFF3E6))
                        .clickable { onIntent(MindReaderIntent.TranslateClicked) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = "🌐", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(id = R.string.mind_reader_translate),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = LinguaQuestTheme.colors.BrownText
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    painter = painterResource(id = R.drawable.ic_speaker_icon),
                    contentDescription = null,
                    tint = LinguaQuestTheme.colors.BrownText,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFFFF3E6))
                        .clickable { onIntent(MindReaderIntent.PlayAudioClicked) }
                        .padding(6.dp)
                )
            }

            if (state.showTranslation && state.translatedQuestion != null) {
                Text(
                    text = state.translatedQuestion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            AppMascotGradientBox(
                imageRes = R.drawable.lingo_mind_asking
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                MindReaderAnswerButton(
                    text = stringResource(id = R.string.mind_reader_answer_yes),
                    onClick = { onIntent(MindReaderIntent.AnswerClicked(MindReaderAnswerOption.YES)) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                MindReaderAnswerButton(
                    text = stringResource(id = R.string.mind_reader_answer_no),
                    onClick = { onIntent(MindReaderIntent.AnswerClicked(MindReaderAnswerOption.NO)) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                MindReaderAnswerButton(
                    text = stringResource(id = R.string.mind_reader_answer_sometimes),
                    onClick = { onIntent(MindReaderIntent.AnswerClicked(MindReaderAnswerOption.SOMETIMES)) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                MindReaderAnswerButton(
                    text = stringResource(id = R.string.mind_reader_answer_probably_not),
                    onClick = { onIntent(MindReaderIntent.AnswerClicked(MindReaderAnswerOption.PROBABLY_NOT)) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                MindReaderAnswerButton(
                    text = stringResource(id = R.string.mind_reader_answer_dont_know),
                    onClick = { onIntent(MindReaderIntent.AnswerClicked(MindReaderAnswerOption.PROBABLY)) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActiveGameContentPreview() {
    LinguaQuestTheme {
        ActiveGameContent(
            state = MindReaderState(),
            onIntent = {}
        )
    }
}
