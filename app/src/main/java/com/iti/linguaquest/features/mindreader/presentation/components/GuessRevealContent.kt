package com.iti.linguaquest.features.mindreader.presentation.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderIntent
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderState

@Composable
fun GuessRevealContent(
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.mind_reader_i_think_its),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = state.guessResult?.entity?.resolveTranslation(state.targetLanguageCode) ?: "",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { onIntent(MindReaderIntent.PlayGuessAudioClicked) }) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play Audio")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            AppButton(
                text = stringResource(id = R.string.mind_reader_wrong),
                onClick = { onIntent(MindReaderIntent.GuessVerifiedIncorrect) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            AppButton(
                text = stringResource(id = R.string.mind_reader_correct),
                onClick = { onIntent(MindReaderIntent.GuessVerifiedCorrect) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
