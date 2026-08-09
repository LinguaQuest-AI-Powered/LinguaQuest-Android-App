package com.iti.linguaquest.features.gallery.presentation.contract

import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord

data class GalleryState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val words: List<WordEntity> = emptyList(),
    val filteredWords: List<WordEntity> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String = "All Items",
    val lockScreenWords: List<LockScreenWord> = emptyList(),
    val filteredLockScreenWords: List<LockScreenWord> = emptyList(),
    val lockScreenCategories: List<String> = emptyList(),
    val selectedLockScreenCategory: String = "All Items",
    val errorMessage: UiText? = null
)
