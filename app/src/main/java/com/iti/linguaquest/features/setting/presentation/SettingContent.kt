package com.iti.linguaquest.features.setting.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.core.utils.ShareTopBar
import com.iti.linguaquest.features.setting.presentation.components.AppButton3D
import com.iti.linguaquest.features.setting.presentation.components.DailyReminderSection
import com.iti.linguaquest.features.setting.presentation.components.RepeatBottomSheet
import com.iti.linguaquest.features.setting.presentation.components.SettingItem
import com.iti.linguaquest.features.setting.presentation.components.SettingProfileHeader
import com.iti.linguaquest.features.setting.presentation.components.SettingSectionContainer
import com.iti.linguaquest.features.setting.presentation.components.TimePickerDialog
import com.iti.linguaquest.features.setting.presentation.contract.ReminderIntent
import com.iti.linguaquest.features.setting.presentation.contract.ReminderState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingContent(
    onBackClick: () -> Unit,
    appLanguage: String,
    onChangeAppLanguage: (String) -> Unit,
    appTheme: String,
    onChangeAppTheme: (String) -> Unit,
    soundEnabled: Boolean,
    onSoundToggle: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsToggle: (Boolean) -> Unit,
    onLogoutClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onLockScreenVocabularyClick: () -> Unit,
    reminderState: ReminderState,
    onReminderIntent: (ReminderIntent) -> Unit
) {
    val isDark = when (appTheme) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }

    var showLanguageDialog by remember { mutableStateOf(false) }

    if (showLanguageDialog) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val languages = listOf(
            "en" to stringResource(R.string.lang_english),
            "es" to stringResource(R.string.lang_spanish),
            "ja" to stringResource(R.string.lang_japanese),
            "ge" to stringResource(R.string.lang_german),
            "ar" to stringResource(R.string.lang_arabic)
        )

        ModalBottomSheet(
            onDismissRequest = { showLanguageDialog = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 8.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(LinguaQuestTheme.colors.textFieldBorder)
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.settings_app_language),
                        style = AppTextStyles.ScreenTitle.copy(
                            fontWeight = FontWeight.Bold,
                            color = LinguaQuestTheme.colors.titleAndCationsColor
                        )
                    )
                    IconButton(onClick = { showLanguageDialog = false }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close),
                            tint = LinguaQuestTheme.colors.titleAndCationsColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                languages.forEach { (code, name) ->
                    Text(
                        text = name,
                        style = AppTextStyles.LessonTitle.copy(
                            fontWeight = if (appLanguage == code) FontWeight.Bold else FontWeight.Normal,
                            color = if (appLanguage == code) MaterialTheme.colorScheme.primary else LinguaQuestTheme.colors.titleAndCationsColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onChangeAppLanguage(code)
                                showLanguageDialog = false
                            }
                            .padding(vertical = 16.dp)
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.background,
                        thickness = 1.dp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    if (reminderState.showTimePicker) {
        TimePickerDialog(state = reminderState, onIntent = onReminderIntent)
    }

    if (reminderState.showRepeatSheet) {
        RepeatBottomSheet(state = reminderState, onIntent = onReminderIntent)
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
            HorizontalDivider(
                color = MaterialTheme.colorScheme.background,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            SettingItem(
                icon = painterResource(id = R.drawable.ic_learning_language),
                title = stringResource(id = R.string.settings_learning_language),
                value = stringResource(id = R.string.lang_english),
                valueColor = LocalLinguaQuestColors.current.BrownText,
                iconTint = LocalLinguaQuestColors.current.OrangeActive,
                onClick = { /* TODO */ }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingSectionContainer(title = stringResource(id = R.string.settings_category_app_experience)) {
            SettingItem(
                icon = painterResource(id = R.drawable.ic_learning_language),
                title = stringResource(id = R.string.settings_app_language),
                value = when (appLanguage) {
                    "es" -> stringResource(id = R.string.lang_spanish)
                    "ja" -> stringResource(id = R.string.lang_japanese)
                    "ge" -> stringResource(id = R.string.lang_german)
                    "ar" -> stringResource(id = R.string.lang_arabic)
                    else -> stringResource(id = R.string.lang_english)
                },
                valueColor = LocalLinguaQuestColors.current.BrownText,
                iconTint = MaterialTheme.colorScheme.tertiary,
                onClick = { showLanguageDialog = true }
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.background,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            SettingItem(
                icon = painterResource(id = R.drawable.ic_bell_icon),
                title = stringResource(id = R.string.settings_notifications),
                hasSwitch = true,
                switchChecked = notificationsEnabled,
                onSwitchChange = onNotificationsToggle,
                iconTint = MaterialTheme.colorScheme.tertiary
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.background,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
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
            HorizontalDivider(
                color = MaterialTheme.colorScheme.background,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            SettingItem(
                icon = painterResource(id = R.drawable.ic_speaker_icon),
                title = stringResource(id = R.string.settings_sound_effects),
                hasSwitch = true,
                switchChecked = soundEnabled,
                onSwitchChange = onSoundToggle,
                iconTint = MaterialTheme.colorScheme.tertiary
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.background,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            SettingItem(
                icon = painterResource(id = R.drawable.ic_help_icon),
                title = stringResource(id = R.string.settings_help_support),
                iconTint = MaterialTheme.colorScheme.tertiary,
                onClick = { /* TODO */ }
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.background,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            SettingItem(
                icon = painterResource(id = R.drawable.ic_lock_icon),
                title = stringResource(id = R.string.settings_lock_screen_vocabulary),
                iconTint = MaterialTheme.colorScheme.tertiary,
                onClick = onLockScreenVocabularyClick
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.background,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
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
            onClick = onLogoutClick,
            textColor = Color.Black,
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
            onChangeAppLanguage = {},
            appTheme = "system",
            onChangeAppTheme = {},
            soundEnabled = true,
            onSoundToggle = {},
            notificationsEnabled = true,
            onNotificationsToggle = {},
            onLogoutClick = {},
            onEditProfileClick = {},
            onLockScreenVocabularyClick = {},
            reminderState = ReminderState(enabled = true),
            onReminderIntent = {}
        )
    }
}