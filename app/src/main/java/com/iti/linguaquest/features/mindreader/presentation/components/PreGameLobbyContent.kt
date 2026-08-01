package com.iti.linguaquest.features.mindreader.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.sharedComponents.dialog.AppDialog
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderIntent
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreGameLobbyContent(
    modifier: Modifier = Modifier,
    state: MindReaderState,
    onIntent: (MindReaderIntent) -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LinguaQuestScreenTopBar(
            title = stringResource(id = R.string.mind_reader_lobby_title),
            onBackClicked = { onIntent(MindReaderIntent.ReturnToHomeClicked) },
            coinsCount = state.coinBalance
        )

        AppMascotGradientBox(
            imageRes = R.drawable.lingo_mind_thinking,
            modifier = Modifier.weight(1f)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(id = R.string.mind_reader_lobby_subtitle),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(id = R.string.mind_reader_think_of_a_word),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            var expanded by remember { mutableStateOf(false) }
            val selectedName = state.selectedCategory?.displayName ?: "Select Category"

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.padding(horizontal = 32.dp).fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedName,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    state.availableCategories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(text = category.displayName) },
                            onClick = {
                                onIntent(MindReaderIntent.CategorySelected(category))
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            AppButton(
                text = stringResource(id = R.string.mind_reader_start_game),
                onClick = { showConfirmDialog = true },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.selectedCategory != null
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }

    if (showConfirmDialog) {
        AppDialog(
            title = stringResource(id = R.string.mind_reader_confirm_dialog_title),
            message = stringResource(id = R.string.mind_reader_confirm_dialog_desc, state.selectedCategory?.displayName ?: "Unknown"),
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
private fun PreGameLobbyContentPreview() {
    LinguaQuestTheme {
        PreGameLobbyContent(
            state = MindReaderState(),
            onIntent = {}
        )
    }
}
