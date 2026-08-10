package com.iti.linguaquest.features.profile.presentation.view.components

import androidx.compose.runtime.Composable

@Composable
fun ProfileOverlays(
    showAvatarSheet: Boolean,
    onDismissAvatarSheet: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onChooseFromGalleryClick: () -> Unit
) {
    if (showAvatarSheet) {
        AvatarPickerBottomSheet(
            onDismiss = onDismissAvatarSheet,
            onTakePhotoClick = onTakePhotoClick,
            onChooseFromGalleryClick = onChooseFromGalleryClick
        )
    }
}
