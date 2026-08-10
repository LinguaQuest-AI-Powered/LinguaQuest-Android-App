package com.iti.linguaquest.features.gallery.presentation.contract

import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText

sealed interface GalleryEffect {
    data class NavigateToReview(val word: WordEntity) : GalleryEffect
    data class ShowLockScreenWordDialog(val wordId: Int) : GalleryEffect
}
