package com.iti.linguaquest.features.mindreader.presentation.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MindReaderCategoryBottomSheet(
    selectedCategory: MindReaderCategory?,
    availableCategories: List<MindReaderCategory>,
    showBottomSheet: Boolean,
    onRequestShowBottomSheet: () -> Unit,
    onDismissRequest: () -> Unit,
    onCategorySelected: (MindReaderCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    // Render the currently selected category in the main UI
    CategorySelectionCard(
        category = selectedCategory,
        modifier = modifier.clickable { onRequestShowBottomSheet() }
    )

    // And conditionally show the bottom sheet
    if (showBottomSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.mind_reader_choose_category),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = LinguaQuestTheme.colors.BrownText,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(availableCategories) { category ->
                        CategorySelectionCard(
                            category = category,
                            modifier = Modifier.clickable {
                                onCategorySelected(category)
                                onDismissRequest()
                            }
                        )
                    }
                }
            }
        }
    }
}
