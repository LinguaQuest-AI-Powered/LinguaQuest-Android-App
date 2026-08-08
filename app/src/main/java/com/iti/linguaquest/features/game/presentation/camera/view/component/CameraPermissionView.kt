package com.iti.linguaquest.features.game.presentation.camera.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.game.presentation.camera.contract.PermissionStatus
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.AppOutlinedButton
import com.iti.linguaquest.core.sharedComponents.ButtonVariant

@Composable
fun CameraPermissionView(
    status: PermissionStatus,
    onGrantClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.lingo_camera_permission),
            contentDescription = "",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.camera_permission_title),
            style = MaterialTheme.typography.headlineMedium,
            color = LinguaQuestTheme.colors.titleAndCationsColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (status == PermissionStatus.PERMANENTLY_DENIED)
                stringResource(id = R.string.camera_permission_blocked_desc)
            else
                stringResource(id = R.string.camera_permission_desc),
            style = MaterialTheme.typography.bodyLarge,
            color = LinguaQuestTheme.colors.titleAndCationsColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        AppButton3D(
            text = if (status == PermissionStatus.PERMANENTLY_DENIED) stringResource(id = R.string.open_settings) else stringResource(id = R.string.grant_permission),
            onClick = onGrantClicked,
            variant = ButtonVariant.PRIMARY,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppOutlinedButton(
            text = stringResource(id = R.string.go_back),
            onClick = onBackClicked,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}