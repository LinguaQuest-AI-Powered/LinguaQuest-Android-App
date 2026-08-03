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
import com.iti.linguaquest.core.sharedComponents.dialog.AppDialog
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderIntent
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderState
import com.iti.linguaquest.features.mindreader.presentation.view.components.MindReaderCategoryDropdown
import com.iti.linguaquest.features.mindreader.presentation.view.components.MindReaderSpeechBubble

@Composable
fun AkinatorLobbyContent(
    modifier: Modifier = Modifier,
    state: MindReaderState,
    onIntent: (MindReaderIntent) -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showCategoryDropdown by remember { mutableStateOf(false) }

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

            MindReaderSpeechBubble(
                text = stringResource(id = R.string.mind_reader_think_of_a_word)
            )

            Spacer(modifier = Modifier.height(8.dp))

            AppMascotGradientBox(
                imageRes = R.drawable.lingo_mind_thinking
            ) {
                Text(
                    text = stringResource(id = R.string.mind_reader_lobby_title),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black,
                    color = LinguaQuestTheme.colors.BrownText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.mind_reader_lobby_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = LinguaQuestTheme.colors.BrownText.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                MindReaderCategoryDropdown(
                    selectedCategory = state.selectedCategory,
                    availableCategories = state.availableCategories,
                    expanded = showCategoryDropdown,
                    onExpandedChange = { showCategoryDropdown = it },
                    onCategorySelected = { onIntent(MindReaderIntent.CategorySelected(it)) }
                )

                Spacer(modifier = Modifier.height(32.dp))

                AppButton3D(
                    text = stringResource(id = R.string.mind_reader_start_game),
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.selectedCategory != null
                )

                Spacer(modifier = Modifier.height(12.dp))

                AppButton3D(
                    text = stringResource(id = R.string.mind_reader_change_category),
                    onClick = { showCategoryDropdown = true },
                    modifier = Modifier.fillMaxWidth(),
                    variant = ButtonVariant.SOCIAL,
                    contentColorOverride = AppColors.Teal,
                    borderColorOverride = AppColors.Teal
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showConfirmDialog) {
        AppDialog(
            title = stringResource(id = R.string.mind_reader_confirm_dialog_title),
            message = stringResource(
                id = R.string.mind_reader_confirm_dialog_desc,
                state.selectedCategory?.displayName ?: ""
            ),
            imageRes = R.drawable.lingo_mind_thinking,
            primaryButtonText = stringResource(id = R.string.mind_reader_yes_lets_go),
            onPrimaryClick = {
                showConfirmDialog = false
                onIntent(MindReaderIntent.StartGameClicked)
            },
            secondaryButtonText = stringResource(id = R.string.mind_reader_not_yet),
            onSecondaryClick = { showConfirmDialog = false },
            onDismissRequest = { showConfirmDialog = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AkinatorLobbyContentPreview() {
    LinguaQuestTheme {
        AkinatorLobbyContent(
            state = MindReaderState(),
            onIntent = {}
        )
    }
}

