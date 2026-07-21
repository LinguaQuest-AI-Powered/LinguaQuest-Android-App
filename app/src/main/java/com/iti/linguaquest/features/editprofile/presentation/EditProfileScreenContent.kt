package com.iti.linguaquest.features.editprofile.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ShareTopBar
import com.iti.linguaquest.features.editprofile.presentation.component.ChangePhotoButton
import com.iti.linguaquest.features.editprofile.presentation.component.DisplayNameTrailingIcon
import com.iti.linguaquest.features.editprofile.presentation.component.EditableAvatar
import com.iti.linguaquest.features.editprofile.presentation.component.InfoNote
import com.iti.linguaquest.features.editprofile.presentation.component.PrimaryActionButton
import com.iti.linguaquest.features.editprofile.presentation.component.ProfileInputCard
import com.iti.linguaquest.features.editprofile.presentation.component.SecondaryTextButton
import com.iti.linguaquest.features.editprofile.presentation.component.TaglineTrailingIcon


@Composable
fun EditProfileScreenContent(
    displayName: String,
    onDisplayNameChange: (String) -> Unit,
    tagline: String,
    onTaglineChange: (String) -> Unit,
    avatarModel: Any?,
    onChangePhotoClick: () -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = AppColors.Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
                 ShareTopBar(title =  R.string.edit_profile_title, onBackClick = onBackClick)

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EditableAvatar(
                    avatarModel = avatarModel,
                    onEditClick = onChangePhotoClick
                )

                Spacer(modifier = Modifier.height(8.dp))

                ChangePhotoButton(
                    text = stringResource(R.string.change_photo),
                    onClick = onChangePhotoClick
                )
            }

            Spacer(modifier = Modifier.height(16.dp))



            ProfileInputCard(
                label = stringResource(R.string.display_name_label),
                value = tagline,
                onValueChange = onTaglineChange,
                singleLine = false,
                minLines = 1,
                trailingIcon = { DisplayNameTrailingIcon() }
            )
            Spacer(modifier = Modifier.height(16.dp))

            ProfileInputCard(
                label = stringResource(R.string.explorer_tagline_label),
                value = tagline,
                onValueChange = onTaglineChange,
                singleLine = false,
                minLines = 3,
                trailingIcon = { TaglineTrailingIcon() }
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoNote(text = stringResource(R.string.profile_visibility_note))

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryActionButton(
                text = stringResource(R.string.save_changes),
                onClick = onSaveClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                SecondaryTextButton(
                    text = stringResource(R.string.cancel),
                    onClick = onCancelClick
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenContentPreview() {
    LinguaQuestTheme {
        EditProfileScreenContent(
            displayName = "Explorer Alex",
            onDisplayNameChange = {},
            tagline = "Mapping the wild frontiers of the French language, one word at a time!",
            onTaglineChange = {},
            avatarModel = R.drawable.lingo_app_bar,
            onChangePhotoClick = {},
            onBackClick = {},
            onSaveClick = {},
            onCancelClick = {}
        )
    }
}