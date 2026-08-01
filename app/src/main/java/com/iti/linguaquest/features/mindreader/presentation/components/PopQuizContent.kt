package com.iti.linguaquest.features.mindreader.presentation.components



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.material3.Text
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
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderIntent
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderState

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
            title = stringResource(id = R.string.mind_reader_lobby_title),
            onBackClicked = { onIntent(MindReaderIntent.ReturnToHomeClicked) },
            coinsCount = state.coinBalance
        )

        AppMascotGradientBox(
            imageRes = R.drawable.lingo_mind_quiz,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.mind_reader_pop_quiz_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = state.popQuizQuestion?.prompt?.resolve(state.targetLanguageCode) ?: "",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = state.guessResult?.entity?.resolveTranslation("en") ?: "",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.weight(1f))

            state.popQuizQuestion?.choices?.forEach { choice ->
                AppButton(
                    text = choice.entity.resolveTranslation(state.targetLanguageCode) ?: "",
                    onClick = { onIntent(MindReaderIntent.PopQuizAnswered(choice)) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }
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
