package com.iti.linguaquest.features.mindreader.presentation.components

import androidx.compose.ui.tooling.preview.Preview
import com.iti.linguaquest.core.theme.LinguaQuestTheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderIntent
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderState

@Composable
fun ActiveGameContent(
    modifier: Modifier = Modifier,
    state: MindReaderState,
    onIntent: (MindReaderIntent) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LinguaQuestScreenTopBar(
            title = stringResource(id = R.string.mind_reader_lobby_title),
            onBackClicked = { onIntent(MindReaderIntent.ReturnToHomeClicked) },
            showCoins = true,
            coinsCount = state.coinBalance
        )

        AppMascotGradientBox(
            imageRes = R.drawable.lingo_mind_asking,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(
                    id = R.string.mind_reader_question_progress,
                    state.currentQuestionNumber,
                    state.maxQuestions
                ),
                style = MaterialTheme.typography.labelLarge
            )
            
            LinearProgressIndicator(
                progress = { state.currentQuestionNumber.toFloat() / state.maxQuestions.coerceAtLeast(1) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = state.currentQuestion ?: "",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { onIntent(MindReaderIntent.PlayAudioClicked) }) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play Audio")
                }
            }
            
            if (state.showTranslation) {
                Text(
                    text = state.translatedQuestion ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                TextButton(onClick = { onIntent(MindReaderIntent.TranslateClicked) }) {
                    Text(text = stringResource(id = R.string.mind_reader_translate))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            AppButton(
                text = stringResource(id = R.string.mind_reader_answer_yes),
                onClick = { onIntent(MindReaderIntent.AnswerClicked(MindReaderAnswerOption.YES)) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            AppButton(
                text = stringResource(id = R.string.mind_reader_answer_no),
                onClick = { onIntent(MindReaderIntent.AnswerClicked(MindReaderAnswerOption.NO)) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            AppButton(
                text = stringResource(id = R.string.mind_reader_answer_sometimes),
                onClick = { onIntent(MindReaderIntent.AnswerClicked(MindReaderAnswerOption.SOMETIMES)) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            AppButton(
                text = stringResource(id = R.string.mind_reader_answer_probably_not),
                onClick = { onIntent(MindReaderIntent.AnswerClicked(MindReaderAnswerOption.PROBABLY_NOT)) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            AppButton(
                text = stringResource(id = R.string.mind_reader_answer_dont_know),
                onClick = { onIntent(MindReaderIntent.AnswerClicked(MindReaderAnswerOption.PROBABLY)) },
                modifier = Modifier.fillMaxWidth()
            )
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
