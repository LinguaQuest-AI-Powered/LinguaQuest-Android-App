package com.iti.linguaquest.features.gallery.presentation.contract

import com.iti.linguaquest.core.database.word.WordEntity

data class GalleryState(
    val isLoading: Boolean = false,
    val words: List<WordEntity> = emptyList(),
    val filteredWords: List<WordEntity> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String = "All Items",
    val errorRes: Int? = null
)
