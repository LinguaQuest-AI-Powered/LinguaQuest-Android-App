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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.sharedComponents.animations.LingoEntranceAnimations
import com.iti.linguaquest.core.sharedComponents.animations.StaggeredAnimatedItem
import com.iti.linguaquest.core.sharedComponents.animations.rememberStaggeredAnimationState
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryIntent
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryState
import com.iti.linguaquest.features.gallery.presentation.view.comonents.CategoryChipsRow
import com.iti.linguaquest.features.gallery.presentation.view.comonents.EmptyGalleryView
import com.iti.linguaquest.features.gallery.presentation.view.comonents.WordCard

@Composable
fun GalleryContent(
    state: GalleryState,
    onIntent: (GalleryIntent) -> Unit,
    onWordClick: (Int, Rect) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalAnimations = if (state.filteredWords.isNotEmpty()) state.filteredWords.size + 1 else 1
    val animationState = rememberStaggeredAnimationState(count = totalAnimations)

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.height(16.dp))

            if (state.words.isNotEmpty()) {
                StaggeredAnimatedItem(
                    index = 0,
                    state = animationState,
                    enter = LingoEntranceAnimations.popUpVertically(offset = -60)
                ) {
                    CategoryChipsRow(
                        categories = state.categories,
                        selectedCategory = state.selectedCategory,
                        onCategorySelected = { onIntent(GalleryIntent.CategorySelected(it)) }
                    )
                }
            }

            if (state.filteredWords.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 100.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(
                        items = state.filteredWords
                    ) { index, word ->
                        StaggeredAnimatedItem(
                            index = index + 1,
                            state = animationState,
                            enter = LingoEntranceAnimations.popUpHorizontally(offset = 200)
                        ) {
                            WordCard(
                                word = word,
                                onWordClick = onWordClick
                            )
                        }
                    }
                }
            } else {
                EmptyGalleryView(modifier = Modifier.weight(1f))
            }
        }
    }
}
