package com.iti.linguaquest.features.profile.presentation.view.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarPickerBottomSheet(
    onDismiss: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onChooseFromGalleryClick: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss ,  containerColor = MaterialTheme.colorScheme.primary) {
        Column(Modifier.padding(bottom = 24.dp)) {
            Text(
                stringResource(R.string.update_profile_photo),
                color = LinguaQuestTheme.colors.blackColor,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
            AvatarPickerOption(
                icon = Icons.Default.CameraAlt,
                label = stringResource(R.string.take_photo),
                onClick = onTakePhotoClick
            )
            AvatarPickerOption(
                icon = Icons.Default.PhotoLibrary,
                label = stringResource(R.string.choose_from_gallery),
                onClick = onChooseFromGalleryClick
            )
        }
    }
}

@Composable
private fun AvatarPickerOption(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(LinguaQuestTheme.colors.IconBoxBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = LinguaQuestTheme.colors.BrownText)
        }
        Spacer(Modifier.width(14.dp))
        Text(label, color = LinguaQuestTheme.colors.blackColor, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
    }
}