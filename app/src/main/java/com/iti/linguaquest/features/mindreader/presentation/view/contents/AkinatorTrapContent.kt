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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderIntent
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderState
import com.iti.linguaquest.core.sharedComponents.MessageBubble
import com.iti.linguaquest.features.mindreader.presentation.view.components.MindReaderEntityDropdown

@Composable
fun AkinatorTrapContent(
    modifier: Modifier = Modifier,
    state: MindReaderState,
    onIntent: (MindReaderIntent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedEntity by remember { mutableStateOf<MindReaderEntity?>(null) }

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
                title = stringResource(id = R.string.mind_reader_trap_speech)
            )

            Spacer(modifier = Modifier.height(8.dp))

            AppMascotGradientBox(
                imageRes = R.drawable.lingo_mind_busted
            ) {
                Text(
                    text = stringResource(
                        id = R.string.mind_reader_trap_dropdown_title,
                        state.selectedCategory?.displayName ?: ""
                    ),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black,
                    color = LinguaQuestTheme.colors.BrownText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                MindReaderEntityDropdown(
                    selectedEntity = selectedEntity,
                    entities = state.stumpCandidates ?: emptyList(),
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    onEntitySelected = { selectedEntity = it },
                    targetLanguageCode = state.targetLanguageCode,
                    nativeLanguageCode = state.nativeLanguageCode
                )

                Spacer(modifier = Modifier.height(32.dp))

                AppButton3D(
                    text = stringResource(id = R.string.mind_reader_trap_submit),
                    onClick = {
                        selectedEntity?.let { onIntent(MindReaderIntent.StumpWordSelected(it)) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedEntity != null
                )

                Spacer(modifier = Modifier.height(12.dp))

                AppButton3D(
                    text = stringResource(id = R.string.mind_reader_return_to_home),
                    onClick = { onIntent(MindReaderIntent.ReturnToHomeClicked) },
                    modifier = Modifier.fillMaxWidth(),
                    variant = ButtonVariant.SOCIAL,
                    contentColorOverride = AppColors.Teal,
                    borderColorOverride = AppColors.Teal
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AkinatorTrapContentPreview() {
    LinguaQuestTheme {
        AkinatorTrapContent(
            state = MindReaderState(),
            onIntent = {}
        )
    }
}

