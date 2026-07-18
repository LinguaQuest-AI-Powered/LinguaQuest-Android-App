package com.iti.linguaquest.features.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.features.setting.components.AppButton3D
import com.iti.linguaquest.features.setting.components.SettingItem
import com.iti.linguaquest.features.setting.components.SettingProfileHeader
import com.iti.linguaquest.features.setting.components.SettingSectionContainer
import com.iti.linguaquest.core.utils.ShareTopBar

@Composable
fun SettingContent(
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }
    var soundEffectsEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(vertical = 24.dp)
            .statusBarsPadding()
    ) {
        ShareTopBar(title = R.string.settings_label,onBackClick = onBackClick)

        Spacer(modifier = Modifier.height(10.dp))

        SettingProfileHeader()

        Spacer(modifier = Modifier.height(24.dp))

        SettingSectionContainer(title = stringResource(id = R.string.settings_category_account)) {
            SettingItem(
                icon = painterResource(id = R.drawable.ic_edit_icon),
                title = stringResource(id = R.string.settings_edit_profile),
                iconTint = LocalLinguaQuestColors.current.OrangeActive,
                onClick = { /* TODO */ }
            )
            Divider(color = MaterialTheme.colorScheme.background, thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
            SettingItem(
                icon = painterResource(id = R.drawable.ic_learning_language),
                title = stringResource(id = R.string.settings_learning_language),
                value = "English",
                valueColor =LocalLinguaQuestColors.current.BrownText,
                iconTint = LocalLinguaQuestColors.current.OrangeActive,
                onClick = { /* TODO */ }
            )

        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingSectionContainer(title = stringResource(id = R.string.settings_category_app_experience)) {
            SettingItem(
                icon = painterResource(id = R.drawable.ic_bell_icon),
                title = stringResource(id = R.string.settings_notifications),
                hasSwitch = true,
                switchChecked = notificationsEnabled,
                onSwitchChange = { notificationsEnabled = it },
                iconTint = MaterialTheme.colorScheme.tertiary
            )
            Divider(color = MaterialTheme.colorScheme.background, thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
            SettingItem(
                icon = painterResource(id = R.drawable.ic_moon_icon),
                title = stringResource(id = R.string.settings_dark_mode),
                hasSwitch = true,
                switchChecked = darkModeEnabled,
                onSwitchChange = { darkModeEnabled = it },
                iconTint = MaterialTheme.colorScheme.tertiary
            )
            Divider(color = MaterialTheme.colorScheme.background, thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
            SettingItem(
                icon = painterResource(id = R.drawable.ic_speaker_icon),
                title = stringResource(id = R.string.settings_sound_effects),
                hasSwitch = true,
                switchChecked = soundEffectsEnabled,
                onSwitchChange = { soundEffectsEnabled = it },
                iconTint = MaterialTheme.colorScheme.tertiary
            )
            Divider(color = MaterialTheme.colorScheme.background, thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
            SettingItem(
                icon = painterResource(id = R.drawable.ic_help_icon),
                title = stringResource(id = R.string.settings_help_support),
                iconTint = MaterialTheme.colorScheme.tertiary,
                onClick = { /* TODO */ }
            )
            Divider(color = MaterialTheme.colorScheme.background, thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
            SettingItem(
                icon = painterResource(id = R.drawable.ic_info_icon),
                title = stringResource(id = R.string.settings_about_app),
                iconTint = MaterialTheme.colorScheme.tertiary,
                onClick = { /* TODO */ }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        AppButton3D(
            text = stringResource(id = R.string.settings_log_out),
            onClick = onLogoutClick,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun SettingContentPreview() {
    LinguaQuestTheme {
        SettingContent(
            onBackClick = {},
            onLogoutClick = {},
            onDeleteAccountClick = {}
        )
    }
}

