package com.iti.linguaquest.features.home.presentation.languages.mylanguages.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.LingoSpinningIcon
import com.iti.linguaquest.core.sharedComponents.dialog.AppDialog
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.home.presentation.languages.mylanguages.contract.MyLanguageUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLanguagesBottomSheet(
    modifier: Modifier = Modifier,
    languages: List<MyLanguageUiModel>,
    isLoading: Boolean = false,
    isSettingActive: Boolean = false,
    languagePendingRemoval: MyLanguageUiModel? = null,
    removingLanguageId: Int? = null,
    onDismiss: () -> Unit,
    onAddNewLanguageClick: () -> Unit,
    onLanguageSelect: (Int) -> Unit,
    onRemoveLanguageClick: ((MyLanguageUiModel) -> Unit)? = null,
    onConfirmRemoveLanguage: (() -> Unit)? = null,
    onDismissRemoveDialog: (() -> Unit)? = null
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LingoSpinningIcon(size = 36.dp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(languages, key = { it.id }) { language ->
                        if (!language.isCurrent && onRemoveLanguageClick != null) {
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { dismissValue ->
                                    if (dismissValue == SwipeToDismissBoxValue.EndToStart || dismissValue == SwipeToDismissBoxValue.StartToEnd) {
                                        onRemoveLanguageClick(language)
                                        true
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
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(32.dp))
                                            .background(color)
                                            .padding(horizontal = 24.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = stringResource(R.string.remove),
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                },
                                content = {
                                    MyLanguageItem(
                                        language = language,
                                        enabled = !isSettingActive,
                                        isRemoving = removingLanguageId == language.id,
                                        onClick = { onLanguageSelect(language.id) }
                                    )
                                }
                            )
                        } else {
                            MyLanguageItem(
                                language = language,
                                enabled = !isSettingActive,
                                isRemoving = removingLanguageId == language.id,
                                onClick = { onLanguageSelect(language.id) },
                                modifier = Modifier.animateItem()
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
