package com.iti.linguaquest.features.gallery.presentation.contract

import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.sharedComponents.text.UiText

import com.iti.linguaquest.core.sharedComponents.state.DataStatus

data class GalleryState(
    val dataStatus: DataStatus = DataStatus.Loading,
    val words: List<WordEntity> = emptyList(),
    val filteredWords: List<WordEntity> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String = "All Items"
) {
    val hasData: Boolean get() = words.isNotEmpty()
}
