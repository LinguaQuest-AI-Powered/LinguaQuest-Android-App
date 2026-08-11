package com.iti.linguaquest.features.home.presentation.languages.mylanguages.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LingoSpinningIcon
import com.iti.linguaquest.core.sharedComponents.dialog.AppDialog
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.home.presentation.languages.mylanguages.contract.MyLanguageUiModel
import com.iti.linguaquest.core.sharedComponents.state.DataStatus
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MyLanguagesBottomSheet(
    modifier: Modifier = Modifier,
    languages: List<MyLanguageUiModel>,
    dataStatus: DataStatus = DataStatus.Loading,
    isSettingActive: Boolean = false,
    languagePendingRemoval: MyLanguageUiModel? = null,
    removingLanguageId: Int? = null,
    languagePendingActivation: MyLanguageUiModel? = null,
    onDismiss: () -> Unit,
    onAddNewLanguageClick: () -> Unit,
    onLanguageSelect: (MyLanguageUiModel) -> Unit,
    onConfirmSetActiveLanguage: (() -> Unit)? = null,
    onDismissSetActiveDialog: (() -> Unit)? = null,
    onRemoveLanguageClick: ((MyLanguageUiModel) -> Unit)? = null,
    onConfirmRemoveLanguage: (() -> Unit)? = null,
    onDismissRemoveDialog: (() -> Unit)? = null
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val soundPlayer = LocalSoundPlayer.current

    LaunchedEffect(Unit) {
        soundPlayer.play(AppSound.NotificationDisappear)
    }

    DisposableEffect(Unit) {
        onDispose {
            soundPlayer.play(AppSound.NotificationDisappear)
        }
    }

    if (languagePendingActivation != null && onConfirmSetActiveLanguage != null && onDismissSetActiveDialog != null) {
        AppDialog(
            title = stringResource(R.string.change_language_title),
            message = stringResource(R.string.change_language_message, languagePendingActivation.name),
            imageRes = R.drawable.lingo_hint,
            onDismissRequest = onDismissSetActiveDialog,
            primaryButtonText = stringResource(R.string.confirm),
            onPrimaryClick = onConfirmSetActiveLanguage,
            secondaryButtonText = stringResource(R.string.cancel),
            onSecondaryClick = onDismissSetActiveDialog
        )
    }

    if (languagePendingRemoval != null && onConfirmRemoveLanguage != null && onDismissRemoveDialog != null) {
        AppDialog(
            title = stringResource(R.string.remove_language_title),
            message = stringResource(R.string.remove_language_message, languagePendingRemoval.name),
            imageRes = R.drawable.lingo_delete_notification,
            onDismissRequest = onDismissRemoveDialog,
            primaryButtonText = stringResource(R.string.remove),
            onPrimaryClick = onConfirmRemoveLanguage,
            secondaryButtonText = stringResource(R.string.cancel),
            onSecondaryClick = onDismissRemoveDialog
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
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
        },
        modifier = modifier
    ) {
        val configuration = LocalConfiguration.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = (configuration.screenHeightDp * 0.75f).dp)
                .padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.my_languages_title),
                    style = AppTextStyles.ScreenTitle.copy(
                        fontWeight = FontWeight.Bold,
                        color = LinguaQuestTheme.colors.titleAndCationsColor
                    ),
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                        tint = LinguaQuestTheme.colors.titleAndCationsColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Crossfade(
                targetState = dataStatus,
                label = "MyLanguagesBottomSheetCrossfade",
                modifier = Modifier.weight(1f, fill = false)
            ) { targetStatus ->
                when (targetStatus) {
                    is DataStatus.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LingoSpinningIcon(size = 36.dp)
                        }
                    }
                    is DataStatus.Loaded, is DataStatus.Refreshing -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = languages,
                                key = { it.id }
                            ) { language ->
                                if (!language.isCurrent) {
                                    val dismissState = rememberSwipeToDismissBoxState(
                                        confirmValueChange = { dismissValue ->
                                            if (dismissValue == SwipeToDismissBoxValue.EndToStart || dismissValue == SwipeToDismissBoxValue.StartToEnd) {
                                                if (onRemoveLanguageClick != null) {
                                                    onRemoveLanguageClick(language)
                                                    true
                                                } else {
                                                    false
                                                }
                                            } else {
                                                false
                                            }
                                        }
                                    )

                                    LaunchedEffect(languagePendingRemoval) {
                                        if (languagePendingRemoval == null && dismissState.currentValue != SwipeToDismissBoxValue.Settled) {
                                            dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                                        }
                                    }

                                    SwipeToDismissBox(
                                        state = dismissState,
                                        modifier = Modifier.animateItem(),
                                        backgroundContent = {
                                            val color = MaterialTheme.colorScheme.error
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(72.dp)
                                                    .clip(RoundedCornerShape(32.dp))
                                                    .background(color),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = stringResource(R.string.remove),
                                                    tint = Color.White,
                                                    modifier = Modifier.size(32.dp)
                                                )
                                            }
                                        },
                                        content = {
                                            MyLanguageItem(
                                                language = language,
                                                enabled = !isSettingActive,
                                                isRemoving = removingLanguageId == language.id,
                                                onClick = { 
                                                    soundPlayer.play(AppSound.SWITCH)
                                                    onLanguageSelect(language) 
                                                }
                                            )
                                        }
                                    )
                                } else {
                                    MyLanguageItem(
                                        modifier = Modifier.animateItem(),
                                        language = language,
                                        enabled = !isSettingActive,
                                        isRemoving = removingLanguageId == language.id,
                                        onClick = { 
                                            soundPlayer.play(AppSound.SWITCH)
                                            onLanguageSelect(language) 
                                        }
                                    )
                                }
                            }
                        }
                    }
                    is DataStatus.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = targetStatus.message.asString(),
                                style = AppTextStyles.Translation,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AppButton3D(
                text = stringResource(R.string.add_new_language),
                onClick = {
                    coroutineScope.launch {
                        sheetState.hide()
                        onAddNewLanguageClick()
                    }
                },
                modifier = Modifier
                    .padding(bottom = 24.dp)
            )
        }
    }
}

