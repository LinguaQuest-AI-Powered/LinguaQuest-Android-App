package com.iti.linguaquest.features.gallery.presentation.contract

import com.iti.linguaquest.core.database.word.WordEntity

sealed interface GalleryIntent {
    data object LoadWords : GalleryIntent
    data object RefreshWords : GalleryIntent
    data class CategorySelected(val category: String) : GalleryIntent
    data class DeleteWordClicked(val word: WordEntity) : GalleryIntent
    data class WordItemClicked(val wordId: Int) : GalleryIntent
}
