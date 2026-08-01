package com.iti.linguaquest.features.mindreader.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MindReaderEntityDropdown(
    selectedEntity: MindReaderEntity?,
    entities: List<MindReaderEntity>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onEntitySelected: (MindReaderEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(LinguaQuestTheme.colors.MindReaderBeige)
                    .clickable { onExpandedChange(!expanded) }
                    .padding(16.dp)
            ) {
                Text(
                    text = selectedEntity?.let {
                        "${it.emoji} ${it.resolveTranslation("en")}"
                    } ?: stringResource(id = R.string.mind_reader_trap_dropdown_hint),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (selectedEntity != null) {
                        LinguaQuestTheme.colors.BrownText
                    } else {
                        LinguaQuestTheme.colors.BrownText.copy(alpha = 0.5f)
                    },
                    modifier = Modifier.weight(1f)
                )

                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                entities.forEach { entity ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = entity.emoji,
                                    fontSize = 20.sp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = entity.resolveTranslation("en"),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        },
                        onClick = {
                            onEntitySelected(entity)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}
