package com.iti.linguaquest.features.gallery.presentation.contract


sealed interface GalleryIntent {
    data object LoadWords : GalleryIntent
    data object RefreshWords : GalleryIntent
    data class CategorySelected(val category: String) : GalleryIntent
    data class LockScreenCategorySelected(val category: String) : GalleryIntent
    data class DeleteWordClicked(val wordId: Int) : GalleryIntent
    data class WordItemClicked(val wordId: Int) : GalleryIntent
    data class LockScreenWordItemClicked(val wordId: Int) : GalleryIntent
}
