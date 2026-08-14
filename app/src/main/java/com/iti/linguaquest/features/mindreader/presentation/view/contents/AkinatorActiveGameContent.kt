package com.iti.linguaquest.features.mindreader.presentation.view.contents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderIntent
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderPhase
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderState
import com.iti.linguaquest.core.sharedComponents.MessageBubble
import com.iti.linguaquest.features.mindreader.presentation.view.components.MindReaderActionRow
import com.iti.linguaquest.features.mindreader.presentation.view.components.MindReaderAnswerButton
import com.iti.linguaquest.features.mindreader.presentation.view.components.MindReaderProgressBar

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
            onBackClicked = { onIntent(MindReaderIntent.ReturnToHomeClicked) },
            showXp = true,
            xpCount = state.xpBalance,
            showCoins = true,
            coinsCount = state.coinBalance
        )

        MindReaderProgressBar(
            currentQuestion = state.currentQuestionNumber,
            maxQuestions = state.maxQuestions,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MessageBubble(
                title = state.currentQuestion?.uppercase() ?: ""
            )

            MindReaderActionRow(
                onTranslateClick = { onIntent(MindReaderIntent.TranslateClicked) },
                onPlayAudioClick = { onIntent(MindReaderIntent.PlayAudioClicked) },
                modifier = Modifier.padding(top = 8.dp)
            )

           Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                if (state.showTranslation && state.translatedQuestion != null) {
                    Text(
                        text = state.translatedQuestion,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            AppMascotGradientBox(
                imageRes = R.drawable.lingo_mind_asking
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                val isInputEnabled = state.currentPhase == MindReaderPhase.PLAYING

                MindReaderAnswerButton(
                    text = MindReaderAnswerOption.YES.resolveLabel(state.targetLanguageCode),
                    onClick = { onIntent(MindReaderIntent.AnswerClicked(MindReaderAnswerOption.YES)) },
                    enabled = isInputEnabled
                )

                Spacer(modifier = Modifier.height(10.dp))

                MindReaderAnswerButton(
                    text = MindReaderAnswerOption.NO.resolveLabel(state.targetLanguageCode),
                    onClick = { onIntent(MindReaderIntent.AnswerClicked(MindReaderAnswerOption.NO)) },
                    enabled = isInputEnabled
                )

                Spacer(modifier = Modifier.height(10.dp))

                MindReaderAnswerButton(
                    text = MindReaderAnswerOption.SOMETIMES.resolveLabel(state.targetLanguageCode),
                    onClick = { onIntent(MindReaderIntent.AnswerClicked(MindReaderAnswerOption.SOMETIMES)) },
                    enabled = isInputEnabled
                )

                Spacer(modifier = Modifier.height(10.dp))

                MindReaderAnswerButton(
                    text = MindReaderAnswerOption.PROBABLY_NOT.resolveLabel(state.targetLanguageCode),
                    onClick = { onIntent(MindReaderIntent.AnswerClicked(MindReaderAnswerOption.PROBABLY_NOT)) },
                    enabled = isInputEnabled
                )

                Spacer(modifier = Modifier.height(10.dp))

                MindReaderAnswerButton(
                    text = MindReaderAnswerOption.PROBABLY.resolveLabel(state.targetLanguageCode),
                    onClick = { onIntent(MindReaderIntent.AnswerClicked(MindReaderAnswerOption.PROBABLY)) },
                    enabled = isInputEnabled
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

