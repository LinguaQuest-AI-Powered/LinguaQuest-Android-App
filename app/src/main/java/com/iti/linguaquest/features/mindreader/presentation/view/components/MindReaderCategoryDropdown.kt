package com.iti.linguaquest.features.mindreader.presentation.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MindReaderCategoryDropdown(
    selectedCategory: MindReaderCategory?,
    availableCategories: List<MindReaderCategory>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onCategorySelected: (MindReaderCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            modifier = Modifier.fillMaxWidth()
        ) {
            CategorySelectionCard(
                category = selectedCategory,
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                availableCategories.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Text(text = "${category.emoji} ${category.displayName}")
                        },
                        onClick = {
                            onCategorySelected(category)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}
