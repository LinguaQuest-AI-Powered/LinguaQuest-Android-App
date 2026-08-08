package com.iti.linguaquest.features.mindreader.presentation.view.contents

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderIntent
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderState
import com.iti.linguaquest.core.sharedComponents.MessageBubble
import com.iti.linguaquest.features.mindreader.presentation.view.components.MindReaderAnswerButton
import com.iti.linguaquest.features.mindreader.presentation.view.components.MindReaderQuizWordCard

@Composable
fun PopQuizContent(
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            MessageBubble(
                title = stringResource(id = R.string.mind_reader_pop_quiz_speech)
            )

            Spacer(modifier = Modifier.height(8.dp))

            AppMascotGradientBox(
                imageRes = R.drawable.lingo_mind_quiz
            ) {
                Text(
                    text = stringResource(id = R.string.mind_reader_pop_quiz_title),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black,
                    color = LinguaQuestTheme.colors.BrownText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                MindReaderQuizWordCard(
                    word = state.popQuizQuestion?.correctEntity?.resolveTranslation(state.nativeLanguageCode)?.uppercase()
                        ?: state.popQuizQuestion?.correctEntity?.resolveTranslation("en")?.uppercase()
                        ?: ""
                )

                Spacer(modifier = Modifier.height(24.dp))

                state.popQuizQuestion?.choices?.forEach { choice ->
                    MindReaderAnswerButton(
                        text = choice.entity.resolveTranslation(state.targetLanguageCode),
                        onClick = { onIntent(MindReaderIntent.PopQuizAnswered(choice)) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PopQuizContentPreview() {
    LinguaQuestTheme {
        PopQuizContent(
            state = MindReaderState(),
            onIntent = {}
        )
    }
}

