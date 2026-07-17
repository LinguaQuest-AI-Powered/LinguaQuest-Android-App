package com.iti.linguaquest.features.gallery.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.sharedComponents.ErrorView
import com.iti.linguaquest.core.sharedComponents.LoadingView
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryIntent
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryState
import com.iti.linguaquest.features.gallery.presentation.view.comonents.CategoryChipsRow
import com.iti.linguaquest.features.gallery.presentation.view.comonents.EmptyGalleryView
import com.iti.linguaquest.features.gallery.presentation.view.comonents.WordCard

@Composable
fun GalleryContent(
    state: GalleryState,
    onIntent: (GalleryIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        
        Spacer(modifier = Modifier.height(16.dp))

        if (state.words.isNotEmpty()) {
            CategoryChipsRow(
                categories = state.categories,
                selectedCategory = state.selectedCategory,
                onCategorySelected = { onIntent(GalleryIntent.CategorySelected(it)) }
            )
        }

        when {
            state.isLoading -> {
                LoadingView(modifier = Modifier.weight(1f))
            }
            state.errorRes != null -> {
                ErrorView(
                    message = androidx.compose.ui.res.stringResource(state.errorRes),
                    onRetry = { onIntent(GalleryIntent.LoadWords) },
                    modifier = Modifier.weight(1f)
                )
            }
            state.filteredWords.isEmpty() -> {
                EmptyGalleryView(modifier = Modifier.weight(1f))
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(
                        items = state.filteredWords,
                        key = { it.id }
                    ) { word ->
                        WordCard(
                            word = word,
                            onWordClick = { onIntent(GalleryIntent.WordItemClicked(it)) }
                        )
                    }
                }
            }
        }
    }
}
