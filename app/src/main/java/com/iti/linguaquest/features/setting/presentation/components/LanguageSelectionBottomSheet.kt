package com.iti.linguaquest.features.setting.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import com.iti.linguaquest.core.utils.toFlagEmoji
import com.iti.linguaquest.features.setting.presentation.LanguagesUiState
import com.iti.linguaquest.core.sharedComponents.LoadingView
import com.iti.linguaquest.core.sharedComponents.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionBottomSheet(
    currentLanguage: String,
    languagesState: LanguagesUiState,
    onLanguageSelected: (LanguageOption) -> Unit,
    onRetry: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
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
                IconButton(onClick = onDismissRequest) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                        tint = LinguaQuestTheme.colors.titleAndCationsColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                languagesState.isLoading -> {
                    LoadingView()
                }

                languagesState.isError -> {
                    ErrorView(
                        message = stringResource(R.string.error_loading_languages),
                        onRetry = onRetry,
                        modifier = Modifier.height(200.dp)
                    )
                }

                languagesState.languages.isEmpty() -> {
                    ErrorView(
                        message = stringResource(R.string.no_languages_available),
                        onRetry = onRetry,
                        modifier = Modifier.height(200.dp)
                    )
                }

                else -> {
                    languagesState.languages.forEach { language ->
                        val isSelected = currentLanguage == language.code
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onLanguageSelected(language)
                                    onDismissRequest()
                                }
                                .padding(vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = language.code.toFlagEmoji(),
                                style = AppTextStyles.LessonTitle,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Text(
                                text = language.name,
                                style = AppTextStyles.LessonTitle.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else LinguaQuestTheme.colors.titleAndCationsColor
                                )
                            )
                        }
                        SectionDivider(paddingHorizontal = 0.dp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}