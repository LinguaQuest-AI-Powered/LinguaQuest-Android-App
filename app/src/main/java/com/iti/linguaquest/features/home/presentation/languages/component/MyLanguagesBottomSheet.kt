package com.iti.linguaquest.features.home.presentation.languages.component

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LingoSpinningIcon
import com.iti.linguaquest.core.sharedComponents.dialog.AppDialog
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.home.presentation.languages.contract.MyLanguageUiModel
import com.iti.linguaquest.core.sharedComponents.state.DataStatus
import androidx.compose.animation.Crossfade

@OptIn(ExperimentalMaterial3Api::class)
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

    if (languagePendingActivation != null && onConfirmSetActiveLanguage != null && onDismissSetActiveDialog != null) {
        AppDialog(
            title = stringResource(R.string.change_language_title),
            message = stringResource(R.string.change_language_message, languagePendingActivation.name),
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
                    text = stringResource(R.string.my_languages_title),
                    style = AppTextStyles.ScreenTitle.copy(
                        fontWeight = FontWeight.Bold,
                        color = LinguaQuestTheme.colors.titleAndCationsColor
                    )
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
                            items(languages) { language ->
                                MyLanguageItem(
                                    language = language,
                                    enabled = !isSettingActive,
                                    isRemoving = removingLanguageId == language.id,
                                    onClick = { onLanguageSelect(language) },
                                    onRemoveClick = if (onRemoveLanguageClick != null && !language.isCurrent) {
                                        { onRemoveLanguageClick(language) }
                                    } else null
                                )
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
                    onAddNewLanguageClick()
                },
                modifier = Modifier
                    .padding(bottom = 24.dp)
            )
        }
    }
}

@Composable
fun MyLanguageItem(
    language: MyLanguageUiModel,
    onClick: () -> Unit,
    onRemoveClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    isRemoving: Boolean = false,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (language.isCurrent) LinguaQuestTheme.colors.ChipBackground else Color.Transparent
    val borderColor = if (language.isCurrent) MaterialTheme.colorScheme.primary else Color.Transparent

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(backgroundColor)
            .border(
                width = if (language.isCurrent) 1.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(32.dp)
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(LinguaQuestTheme.colors.whiteColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = language.flagEmoji,
                style = AppTextStyles.LessonTitle.copy(fontSize = 28.sp, textAlign = TextAlign.Center)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = language.name,
                style = AppTextStyles.LessonTitle.copy(
                    fontWeight = FontWeight.Bold,
                    color = LinguaQuestTheme.colors.titleAndCationsColor
                )
            )
            Text(
                text = stringResource(R.string.my_languages_level_format, language.level),
                style = AppTextStyles.Caption.copy(color = LinguaQuestTheme.colors.textFieldPlaceholder)
            )
        }

        if (language.isCurrent) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = stringResource(R.string.cd_selected),
                tint = LinguaQuestTheme.colors.SuccessAccent,
                modifier = Modifier.size(24.dp)
            )
        } else if (isRemoving) {
            LingoSpinningIcon(size = 24.dp)
        } else if (onRemoveClick != null) {
            IconButton(onClick = onRemoveClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.remove),
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}