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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.runtime.DisposableEffect
import com.iti.linguaquest.core.utils.SpeechManager
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryIntent
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryState
import com.iti.linguaquest.features.gallery.presentation.view.comonents.CategoryChipsRow
import com.iti.linguaquest.features.gallery.presentation.view.comonents.EmptyLockScreenWordsView
import com.iti.linguaquest.features.gallery.presentation.view.comonents.LockScreenWordCard

@Composable
fun LockScreenWordsContent(
    state: GalleryState,
    onIntent: (GalleryIntent) -> Unit,
    onWordClick: (Int, Rect) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val speechManager = remember { SpeechManager(context) }
    
    DisposableEffect(Unit) {
        onDispose { speechManager.shutdown() }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(16.dp))

            if (state.lockScreenWords.isNotEmpty()) {
                CategoryChipsRow(
                    categories = state.lockScreenCategories,
                    selectedCategory = state.selectedLockScreenCategory,
                    onCategorySelected = {
                        onIntent(GalleryIntent.LockScreenCategorySelected(it))
                    }
                )
            }

            if (state.filteredLockScreenWords.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 100.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(
                        items = state.filteredLockScreenWords
                    ) { word ->
                        LockScreenWordCard(
                            word = word,
                            onWordClick = onWordClick,
                            onSpeakClick = {
                                speechManager.speak(word.word, languageCode = word.targetLanguage)
                            }
                        )
                    }
                }
            } else {
                EmptyLockScreenWordsView(modifier = Modifier.weight(1f))
            }
        }
    }
}
