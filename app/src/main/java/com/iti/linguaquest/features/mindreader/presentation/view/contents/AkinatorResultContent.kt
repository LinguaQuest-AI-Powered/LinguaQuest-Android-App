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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.AppConfettiView
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.AppRewardsRow
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.sharedComponents.IconPosition
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.sharedComponents.MessageBubble
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderIntent
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderState

@Composable
fun ResultContent(
    modifier: Modifier = Modifier,
    state: MindReaderState,
    onIntent: (MindReaderIntent) -> Unit
) {
    val resultInfo = state.resultInfo ?: return
    val soundPlayer = LocalSoundPlayer.current

    LaunchedEffect(resultInfo.isVictory) {
        if (resultInfo.isVictory) {
            soundPlayer.play(AppSound.SUCCESS)
        } else {
            soundPlayer.play(AppSound.FAIL)
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (resultInfo.isVictory) {
            AppConfettiView()
        }

        Column(
            modifier = Modifier.fillMaxSize()
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

                val speechText = if (resultInfo.isVictory) {
                    stringResource(id = R.string.mind_reader_victory_speech)
                } else {
                    resultInfo.reason ?: stringResource(id = R.string.mind_reader_busted_title)
                }

                MessageBubble(title = speechText)

                Spacer(modifier = Modifier.height(8.dp))

                val imageRes = if (resultInfo.isVictory) {
                    R.drawable.lingo_mind_win
                } else {
                    R.drawable.lingo_mind_lose
                }

                AppMascotGradientBox(
                    imageRes = imageRes
                ) {
                    val titleText = if (resultInfo.isVictory) {
                        stringResource(id = R.string.mind_reader_stumped_title)
                    } else {
                        stringResource(id = R.string.mind_reader_busted_title)
                    }

                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        color = if (resultInfo.isVictory) {
                            LinguaQuestTheme.colors.BrownText
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    AppRewardsRow(
                        xpAmount = resultInfo.xpEarned,
                        coinsAmount = resultInfo.coinsEarned
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    val primaryText = if (resultInfo.isVictory) {
                        stringResource(id = R.string.mind_reader_play_again)
                    } else {
                        stringResource(id = R.string.mind_reader_try_again)
                    }

                    AppButton3D(
                        text = primaryText,
                        onClick = { onIntent(MindReaderIntent.TryAgainClicked) },
                        modifier = Modifier.fillMaxWidth(),
                        icon = painterResource(id = R.drawable.skip),
                        iconPosition = IconPosition.START
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AppButton3D(
                        text = stringResource(id = R.string.mind_reader_return_to_home),
                        onClick = { onIntent(MindReaderIntent.ReturnToHomeClicked) },
                        modifier = Modifier.fillMaxWidth(),
                        variant = ButtonVariant.SOCIAL,
                        contentColorOverride = AppColors.Teal,
                        borderColorOverride = AppColors.Teal,
                        icon = painterResource(id = R.drawable.world_home_icon),
                        iconPosition = IconPosition.START
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
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

