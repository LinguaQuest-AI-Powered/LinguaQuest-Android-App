package com.iti.linguaquest.features.gallery.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.sharedComponents.ErrorView
import com.iti.linguaquest.core.sharedComponents.offline.OfflineStateView
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryIntent
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryState
import com.iti.linguaquest.features.gallery.presentation.view.comonents.CategoryChipsRow
import com.iti.linguaquest.features.gallery.presentation.view.comonents.EmptyGalleryView
import com.iti.linguaquest.features.gallery.presentation.view.comonents.WordCard

@Composable
fun GalleryContent(
    state: GalleryState,
    isOnline: Boolean = true,
    onIntent: (GalleryIntent) -> Unit,
    onWordClick: (Int, Rect) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.height(16.dp))

            if (state.words.isNotEmpty()) {
                CategoryChipsRow(
                    categories = state.categories,
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = { onIntent(GalleryIntent.CategorySelected(it)) }
                )
            }

            if (state.filteredWords.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(
                        items = state.filteredWords
                    ) { word ->
                        WordCard(
                            word = word,
                            onWordClick = onWordClick
                        )
                    }
                }
            } else {
                EmptyGalleryView(modifier = Modifier.weight(1f))
            }
        }

        if (!isOnline && state.words.isEmpty()) {
            OfflineStateView(
                onRetry = { onIntent(GalleryIntent.LoadWords) }
            )
        }

        if (state.errorMessage != null && state.words.isEmpty()) {
            ErrorView(
                message = state.errorMessage!!,
                onRetry = { onIntent(GalleryIntent.LoadWords) }
            )
        }
    }
}
