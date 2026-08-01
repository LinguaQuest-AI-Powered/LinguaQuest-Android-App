package com.iti.linguaquest.features.mindreader.presentation.components



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.ui.tooling.preview.Preview
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderIntent
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderState

@OptIn(ExperimentalMaterial3Api::class)
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
            title = stringResource(id = R.string.mind_reader_lobby_title),
            onBackClicked = { onIntent(MindReaderIntent.ReturnToHomeClicked) },
            coinsCount = state.coinBalance
        )

        AppMascotGradientBox(
            imageRes = R.drawable.lingo_mind_busted,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.mind_reader_trap_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.weight(1f))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedEntity?.resolveTranslation("en") ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { 
                        Text(stringResource(
                            id = R.string.mind_reader_trap_dropdown_title, 
                            state.selectedCategory?.displayName ?: ""
                        )) 
                    },
                    placeholder = { Text(stringResource(id = R.string.mind_reader_trap_dropdown_hint)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    state.stumpCandidates?.forEach { entity ->
                        DropdownMenuItem(
                            text = { Text(entity.resolveTranslation("en") ?: "") },
                            onClick = {
                                selectedEntity = entity
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            AppButton(
                text = stringResource(id = R.string.mind_reader_trap_submit),
                onClick = { 
                    selectedEntity?.let { onIntent(MindReaderIntent.StumpWordSelected(it)) } 
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedEntity != null
            )
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
