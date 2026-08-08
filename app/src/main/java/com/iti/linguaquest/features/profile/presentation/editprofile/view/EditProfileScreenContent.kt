package com.iti.linguaquest.features.profile.presentation.editprofile.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBarBackButtonStyle
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.features.profile.presentation.editprofile.contract.EditProfileTab
import com.iti.linguaquest.features.profile.presentation.editprofile.utils.FieldError
import com.iti.linguaquest.features.profile.presentation.editprofile.view.component.ChangePasswordCard
import com.iti.linguaquest.features.profile.presentation.editprofile.view.component.ChangePhotoButton
import com.iti.linguaquest.features.profile.presentation.editprofile.view.component.DisplayNameTrailingIcon
import com.iti.linguaquest.features.profile.presentation.editprofile.view.component.EditableAvatar
import com.iti.linguaquest.features.profile.presentation.editprofile.view.component.ProfileInputCard
import com.iti.linguaquest.features.profile.presentation.editprofile.view.component.SecondaryTextButton
import com.iti.linguaquest.features.profile.presentation.view.components.AvatarPickerBottomSheet
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun EditProfileScreenContent(
    displayName: String,
    onDisplayNameChange: (String) -> Unit,
    oldPassword: String,
    onOldPasswordChange: (String) -> Unit,
    newPassword: String,
    onNewPasswordChange: (String) -> Unit,
    avatarModel: Any?,
    isLoading: Boolean,
    isSavingName: Boolean,
    isSavingPassword: Boolean,
    selectedTab: EditProfileTab,
    onTabChange: (EditProfileTab) -> Unit,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
    displayNameError: FieldError = FieldError(),
    oldPasswordError: FieldError = FieldError(),
    newPasswordError: FieldError = FieldError()
) {
    var showAvatarSheet by remember { mutableStateOf(false) }
    var showNameSavedTick by remember { mutableStateOf(false) }
    var wasSavingName by remember { mutableStateOf(false) }

    LaunchedEffect(isSavingName) {
        if (wasSavingName && !isSavingName && !displayNameError.isError) {
            showNameSavedTick = true
            delay(1500)
            showNameSavedTick = false
        }
        wasSavingName = isSavingName
    }

    var showPasswordSavedTick by remember { mutableStateOf(false) }
    var wasSavingPassword by remember { mutableStateOf(false) }

    LaunchedEffect(isSavingPassword) {
        if (wasSavingPassword && !isSavingPassword && !oldPasswordError.isError && !newPasswordError.isError) {
            showPasswordSavedTick = true
            delay(1500.milliseconds)
            showPasswordSavedTick = false
        }
        wasSavingPassword = isSavingPassword
    }

    Scaffold(
        modifier = modifier,
        containerColor = LinguaQuestTheme.colors.textFieldFill,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LinguaQuestTheme.colors.textFieldFill)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                AppButton3D(
                    text = stringResource(R.string.save_changes),
                    onClick = onSaveClick,
                    isLoading = if (selectedTab == EditProfileTab.PERSONAL_INFO) isSavingName else isSavingPassword,
                    isSuccess = if (selectedTab == EditProfileTab.PERSONAL_INFO) showNameSavedTick else showPasswordSavedTick
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    SecondaryTextButton(
                        text = stringResource(R.string.cancel),
                        onClick = onCancelClick
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 8.dp, bottom = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            LinguaQuestScreenTopBar(
                title = stringResource(id = R.string.edit_profile_title),
                onBackClicked = onBackClick,
                isTitleCentered = true,
                containerColor = Color.Transparent,
                titleColor = LocalLinguaQuestColors.current.BrownText,
                titleTextStyle = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                showDivider = true,
                dividerSpacing = 16.dp,
                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 0.dp),
                backButtonStyle = LinguaQuestScreenTopBarBackButtonStyle.Circular,
                backButtonSize = 40.dp,
                backButtonBackgroundColor = LocalLinguaQuestColors.current.whiteColor,
                backButtonContentColor = LocalLinguaQuestColors.current.OrangeActive,
                backButtonIconSize = 18.dp
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EditableAvatar(
                        avatarModel = avatarModel,
                        onEditClick = { showAvatarSheet = true },
                        isAvatarUploading = isLoading,
                        avatarContentDescription = stringResource(R.string.change_photo),
                        editButtonContentDescription = stringResource(R.string.change_photo)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    ChangePhotoButton(
                        text = stringResource(R.string.change_photo),
                        onClick = { showAvatarSheet = true }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                val tabs = listOf(
                    EditProfileTab.PERSONAL_INFO to stringResource(R.string.personal_info),
                    EditProfileTab.SECURITY to stringResource(R.string.security)
                )

                TabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    containerColor = Color.Transparent,
                    contentColor = LinguaQuestTheme.colors.BrownText,
                    indicator = { tabPositions ->
                        if (selectedTab.ordinal < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                                color = AppColors.OrangeActive,
                                height = 2.dp
                            )
                        }
                    },
                    divider = {
                        HorizontalDivider(color = LinguaQuestTheme.colors.textFieldBorder.copy(alpha = 0.4f))
                    }
                ) {
                    tabs.forEach { (tab, title) ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { onTabChange(tab) },
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (selectedTab == tab) LinguaQuestTheme.colors.BrownText else LinguaQuestTheme.colors.iconsColor
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                AnimatedContent(targetState = selectedTab, label = "Tab Content") { targetTab ->
                    when (targetTab) {
                        EditProfileTab.PERSONAL_INFO -> {
                            Column {
                                ProfileInputCard(
                                    label = stringResource(R.string.display_name_label),
                                    value = displayName,
                                    onValueChange = onDisplayNameChange,
                                    singleLine = true,
                                    trailingIcon = { DisplayNameTrailingIcon() },
                                    fieldError = displayNameError
                                )

                                Spacer(modifier = Modifier.height(32.dp))

                                Image(
                                    painter = painterResource(id = R.drawable.lingo_change_name),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentScale = ContentScale.Fit
                                )

                              }
                        }
                        EditProfileTab.SECURITY -> {
                            Column {
                                ChangePasswordCard(
                                    label = stringResource(R.string.change_password_label),
                                    oldPassword = oldPassword,
                                    onOldPasswordChange = onOldPasswordChange,
                                    oldPasswordPlaceholder = stringResource(R.string.old_password_placeholder),
                                    newPassword = newPassword,
                                    onNewPasswordChange = onNewPasswordChange,
                                    newPasswordPlaceholder = stringResource(R.string.new_password_placeholder),
                                    oldPasswordError = oldPasswordError,
                                    newPasswordError = newPasswordError,
                                    newPasswordHelperText = stringResource(R.string.new_password_min_length_hint)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        if (showAvatarSheet) {
            AvatarPickerBottomSheet(
                onDismiss = { showAvatarSheet = false },
                onChooseFromGalleryClick = {
                    showAvatarSheet = false
                    onGalleryClick()
                },
                onTakePhotoClick = {
                    showAvatarSheet = false
                    onCameraClick()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenContentErrorPreview() {
    LinguaQuestTheme {
        EditProfileScreenContent(
            displayName = "",
            onDisplayNameChange = {},
            oldPassword = "wrongpass",
            onOldPasswordChange = {},
            newPassword = "123",
            onNewPasswordChange = {},
            avatarModel = R.drawable.lingo_app_bar,
            isLoading = false,
            isSavingName = false,
            isSavingPassword = false,
            selectedTab = EditProfileTab.PERSONAL_INFO,
            onTabChange = {},
            onGalleryClick = {},
            onCameraClick = {},
            onBackClick = {},
            onSaveClick = {},
            onCancelClick = {},
            displayNameError = FieldError(
                isError = true, message = UiText.StringResource(R.string.display_name_empty),
            ),
            oldPasswordError = FieldError(
                isError = true, message = UiText.StringResource(R.string.display_name_empty),
            ),
            newPasswordError = FieldError(
                isError = true, message = UiText.StringResource(R.string.display_name_empty),
            )
        )
    }
}
