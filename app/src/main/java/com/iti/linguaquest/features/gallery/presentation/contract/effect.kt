package com.iti.linguaquest.features.gallery.presentation.contract

import com.iti.linguaquest.core.database.word.WordEntity

sealed interface GalleryEffect {
    data class ShowError(val messageRes: Int) : GalleryEffect
    data class NavigateToReview(val word: WordEntity) : GalleryEffect
}
