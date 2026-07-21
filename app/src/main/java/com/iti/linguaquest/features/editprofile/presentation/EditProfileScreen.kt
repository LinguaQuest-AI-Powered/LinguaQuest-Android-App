package com.iti.linguaquest.features.editprofile.presentation



import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.iti.linguaquest.R

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    initialDisplayName: String = "",
    initialTagline: String = "",
    avatarModel: Any? = null,
    onBackClick: () -> Unit = {},
    onChangePhotoClick: () -> Unit = {},
    onSave: (displayName: String, tagline: String) -> Unit = { _, _ -> }
) {
    var displayName by remember { mutableStateOf(initialDisplayName) }
    var tagline by remember { mutableStateOf(initialTagline) }

    EditProfileScreenContent(
        modifier = modifier,
        displayName = displayName,
        onDisplayNameChange = { displayName = it },
        tagline = tagline,
        onTaglineChange = { tagline = it },
        avatarModel =  R.drawable.lingo_app_bar,
        onChangePhotoClick = onChangePhotoClick,
        onBackClick = onBackClick,
        onSaveClick = { onSave(displayName, tagline) },
        onCancelClick = onBackClick
    )
}