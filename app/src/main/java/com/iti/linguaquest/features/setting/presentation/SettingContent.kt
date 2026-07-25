package com.iti.linguaquest.features.setting.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.core.utils.ShareTopBar
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenFeatureState
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenIntent
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenState
import com.iti.linguaquest.features.setting.presentation.components.AppButton3D
import com.iti.linguaquest.features.setting.presentation.components.DailyReminderSection
import com.iti.linguaquest.features.setting.presentation.components.EnableLockScreenDialog
import com.iti.linguaquest.features.setting.presentation.components.LanguageSelectionBottomSheet
import com.iti.linguaquest.features.setting.presentation.components.LockScreenSettingItem
import com.iti.linguaquest.features.setting.presentation.components.RepeatBottomSheet
import com.iti.linguaquest.features.setting.presentation.components.SectionDivider
import com.iti.linguaquest.features.setting.presentation.components.SettingItem
import com.iti.linguaquest.features.setting.presentation.components.SettingProfileHeader
import com.iti.linguaquest.features.setting.presentation.components.SettingSectionContainer
import com.iti.linguaquest.features.setting.presentation.components.TimePickerDialog
import com.iti.linguaquest.features.setting.presentation.components.getLanguageName
import com.iti.linguaquest.features.setting.presentation.contract.ReminderIntent
import com.iti.linguaquest.features.setting.presentation.contract.ReminderState
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import com.iti.linguaquest.features.setting.presentation.LanguagesUiState
import com.iti.linguaquest.core.sharedComponents.dialog.AppDialog

@Composable
fun SettingContent(
    onBackClick: () -> Unit,
    appLanguage: String,
    availableLanguagesState: LanguagesUiState,
    onRetryLanguages: () -> Unit,
    onChangeAppLanguage: (LanguageOption) -> Unit,
    appTheme: String,
    onChangeAppTheme: (String) -> Unit,
    soundEnabled: Boolean,
    onSoundToggle: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsToggle: (Boolean) -> Unit,
    isLoggingOut: Boolean = false,
    onLogoutClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onLockScreenVocabularyClick: () -> Unit,
    reminderState: ReminderState,
    onReminderIntent: (ReminderIntent) -> Unit,
    lockScreenState: LockScreenState,
    onLockScreenIntent: (LockScreenIntent) -> Unit
) {
    val isDark = when (appTheme) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

     if (lockScreenState.isConfirmDialogVisible) {
        EnableLockScreenDialog(
            coinCost = 50,
            onConfirm = { onLockScreenIntent(LockScreenIntent.ConfirmEnableClicked) },
            onCancel = { onLockScreenIntent(LockScreenIntent.CancelEnableClicked) }
        )
    }

    if (showLanguageDialog) {
        LanguageSelectionBottomSheet(
            currentLanguage = appLanguage,
            languagesState = availableLanguagesState,
            onLanguageSelected = onChangeAppLanguage,
            onRetry = onRetryLanguages,
            onDismissRequest = { showLanguageDialog = false }
        )
    }

     if (reminderState.showTimePicker) {
        TimePickerDialog(state = reminderState, onIntent = onReminderIntent)
    }

    if (reminderState.showRepeatSheet) {
        RepeatBottomSheet(state = reminderState, onIntent = onReminderIntent)
    }

    if (showLogoutDialog) {
        AppDialog(
            title = stringResource(id = R.string.settings_log_out),
            imageRes = R.drawable.lingo_logout,
            message = stringResource(id = R.string.logout_dialog_message),
            primaryButtonText = stringResource(id = R.string.settings_log_out),
            onPrimaryClick = {
                showLogoutDialog = false
                onLogoutClick()
            },
            secondaryButtonText = stringResource(id = R.string.cancel_button),
            onSecondaryClick = { showLogoutDialog = false },
            onDismissRequest = { showLogoutDialog = false },
            showCloseIcon = true
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(vertical = 24.dp)
            .statusBarsPadding()
    ) {
        ShareTopBar(title = R.string.settings_label, onBackClick = onBackClick)

        Spacer(modifier = Modifier.height(10.dp))

        SettingProfileHeader()

        Spacer(modifier = Modifier.height(24.dp))

         SettingSectionContainer(title = stringResource(id = R.string.settings_category_account)) {
            SettingItem(
                icon = painterResource(id = R.drawable.ic_edit_icon),
                title = stringResource(id = R.string.settings_edit_profile),
                iconTint = LocalLinguaQuestColors.current.OrangeActive,
                onClick = onEditProfileClick
            )
             LockScreenSettingItem(
                 isFeatureActive = lockScreenState.featureState == LockScreenFeatureState.ACTIVE || lockScreenState.featureState == LockScreenFeatureState.ENABLING,
                 onCheckedChange = { isChecked ->
                     onLockScreenIntent(LockScreenIntent.ToggleFeatureClicked(isChecked))
                 }
             )
        }

        Spacer(modifier = Modifier.height(24.dp))

         SettingSectionContainer(title = stringResource(id = R.string.settings_category_app_experience)) {
            SettingItem(
                icon = painterResource(id = R.drawable.ic_learning_language),
                title = stringResource(id = R.string.settings_app_language),
                value = getLanguageName(appLanguage),
                valueColor = LocalLinguaQuestColors.current.BrownText,
                iconTint = MaterialTheme.colorScheme.tertiary,
                onClick = { showLanguageDialog = true }
            )
            SectionDivider()
            SettingItem(
                icon = painterResource(id = R.drawable.ic_bell_icon),
                title = stringResource(id = R.string.settings_notifications),
                hasSwitch = true,
                switchChecked = notificationsEnabled,
                onSwitchChange = onNotificationsToggle,
                iconTint = MaterialTheme.colorScheme.tertiary
            )
            SectionDivider()
            SettingItem(
                icon = painterResource(id = R.drawable.ic_moon_icon),
                title = stringResource(id = R.string.settings_dark_mode),
                hasSwitch = true,
                switchChecked = isDark,
                onSwitchChange = { isChecked ->
                    onChangeAppTheme(if (isChecked) "dark" else "light")
                },
                iconTint = MaterialTheme.colorScheme.tertiary
            )
            SectionDivider()
            SettingItem(
                icon = painterResource(id = R.drawable.ic_speaker_icon),
                title = stringResource(id = R.string.settings_sound_effects),
                hasSwitch = true,
                switchChecked = soundEnabled,
                onSwitchChange = onSoundToggle,
                iconTint = MaterialTheme.colorScheme.tertiary
            )
            SectionDivider()
            SettingItem(
                icon = painterResource(id = R.drawable.ic_help_icon),
                title = stringResource(id = R.string.settings_help_support),
                iconTint = MaterialTheme.colorScheme.tertiary,
                onClick = { /* TODO */ }
            )
            SectionDivider()
             SettingItem(
                icon = painterResource(id = R.drawable.ic_info_icon),
                title = stringResource(id = R.string.settings_about_app),
                iconTint = MaterialTheme.colorScheme.tertiary,
                onClick = { /* TODO */ }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

         DailyReminderSection(
            state = reminderState,
            onIntent = onReminderIntent,
            enabled = notificationsEnabled,
            modifier = Modifier.padding(horizontal = 0.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

         AppButton3D(
            text = stringResource(id = R.string.settings_log_out),
            onClick = { showLogoutDialog = true },
            textColor = Color.Black,
            isLoading = isLoggingOut,
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
            appLanguage = "en",
            availableLanguagesState = LanguagesUiState(),
            onRetryLanguages = {},
            onChangeAppLanguage = {},
            appTheme = "system",
            onChangeAppTheme = {},
            soundEnabled = true,
            onSoundToggle = {},
            notificationsEnabled = true,
            onNotificationsToggle = {},
            isLoggingOut = false,
            onLogoutClick = {},
            onEditProfileClick = {},
            onLockScreenVocabularyClick = {},
            reminderState = ReminderState(enabled = true),
            onReminderIntent = {},
            lockScreenState = LockScreenState(),
            onLockScreenIntent = {}
        )
    }
}