package com.iti.linguaquest.features.mindreader.presentation.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
    modifier: Modifier = Modifier,
    selectedCategory: MindReaderCategory?,
    availableCategories: List<MindReaderCategory>,
    showBottomSheet: Boolean,
    onRequestShowBottomSheet: () -> Unit,
    onDismissRequest: () -> Unit,
    onCategorySelected: (MindReaderCategory) -> Unit,
    languageCode: String? = null
) {
    CategorySelectionCard(
        category = selectedCategory,
        modifier = modifier.clickable { onRequestShowBottomSheet() },
        languageCode = languageCode
    )

    if (showBottomSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
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
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        items = availableCategories,
                        key = { it.id }
                    ) { category ->
                        CategorySelectionCard(
                            category = category,
                            modifier = Modifier.clickable {
                                onCategorySelected(category)
                                onDismissRequest()
                            },
                            languageCode = languageCode
                        )
                    }
                }
            }
        }
    }
}
