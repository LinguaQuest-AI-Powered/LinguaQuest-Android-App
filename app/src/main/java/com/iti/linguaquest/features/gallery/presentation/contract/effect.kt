package com.iti.linguaquest.features.gallery.presentation.contract

import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText

sealed interface GalleryEffect {
    data class ShowError(
        val message: UiText,
        val title: UiText? = null,
        val type: SnackbarType = SnackbarType.ERROR,
        val retryable: Boolean = false
    ) : GalleryEffect
    data class NavigateToReview(val word: WordEntity) : GalleryEffect
    data class ShowLockScreenWordDialog(val wordId: Int) : GalleryEffect
}
