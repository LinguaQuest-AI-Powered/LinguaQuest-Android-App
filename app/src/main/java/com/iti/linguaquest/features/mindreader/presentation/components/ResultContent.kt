package com.iti.linguaquest.features.mindreader.presentation.components



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
fun ResultContent(
    modifier: Modifier = Modifier,
    state: MindReaderState,
    onIntent: (MindReaderIntent) -> Unit
) {
    val resultInfo = state.resultInfo ?: return
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LinguaQuestScreenTopBar(
            title = stringResource(id = R.string.mind_reader_lobby_title),
            onBackClicked = { onIntent(MindReaderIntent.ReturnToHomeClicked) },
            coinsCount = state.coinBalance
        )

        val imageRes = if (resultInfo.isVictory) R.drawable.lingo_mind_lose else R.drawable.lingo_mind_win
        AppMascotGradientBox(
            imageRes = imageRes,
            modifier = Modifier.weight(1f)
        ) {
            val titleRes = if (resultInfo.isVictory) R.string.mind_reader_stumped else R.string.mind_reader_busted
            
            Text(
                text = stringResource(id = titleRes),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = if (resultInfo.isVictory) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
            
            if (resultInfo.isVictory) {
                Text(
                    text = stringResource(id = R.string.mind_reader_stumped_desc),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            } else if (resultInfo.reason != null) {
                Text(
                    text = resultInfo.reason,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (resultInfo.isVictory) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "+${resultInfo.xpEarned} XP",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(id = R.string.mind_reader_experience),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "+${resultInfo.coinsEarned} 🪙",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(id = R.string.mind_reader_earnings),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.weight(1f))

            AppButton(
                text = stringResource(id = R.string.mind_reader_try_again),
                onClick = { onIntent(MindReaderIntent.TryAgainClicked) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            AppButton(
                text = stringResource(id = R.string.mind_reader_return_home),
                onClick = { onIntent(MindReaderIntent.ReturnToHomeClicked) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ResultContentPreview() {
    LinguaQuestTheme {
        ResultContent(
            state = MindReaderState(),
            onIntent = {}
        )
    }
}
