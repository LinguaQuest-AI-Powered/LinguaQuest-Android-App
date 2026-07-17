package com.iti.linguaquest.features.gallery.presentation.contract

sealed interface GalleryEffect {
    data class ShowError(val messageRes: Int) : GalleryEffect
    data class NavigateToWordDetails(val wordId: Int) : GalleryEffect
}
