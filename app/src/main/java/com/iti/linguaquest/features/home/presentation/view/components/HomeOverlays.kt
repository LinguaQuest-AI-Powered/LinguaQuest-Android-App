package com.iti.linguaquest.features.home.presentation.view.components

import androidx.compose.runtime.Composable
import com.iti.linguaquest.features.home.presentation.contract.HomeIntent
import com.iti.linguaquest.features.home.presentation.contract.HomeState
import com.iti.linguaquest.features.home.presentation.languages.mylanguages.view.components.MyLanguagesBottomSheet
import com.iti.linguaquest.features.home.presentation.languages.mylanguages.contract.MyLanguagesIntent
import com.iti.linguaquest.features.home.presentation.languages.mylanguages.contract.MyLanguagesState
import com.iti.linguaquest.features.home.presentation.languages.mylanguages.contract.MyLanguageUiModel
import com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components.HomeDailyRewardDialog

@Composable
fun HomeOverlays(
    state: HomeState,
    myLanguagesState: MyLanguagesState,
    onHomeIntent: (HomeIntent) -> Unit,
    onMyLanguagesIntent: (MyLanguagesIntent) -> Unit
) {
    HomeDailyRewardDialog(
        state = state,
        onDismissRequest = { onHomeIntent(HomeIntent.DismissDailyRewardDialog) },
        onClaimClick = { onHomeIntent(HomeIntent.ClaimDailyRewardClicked) }
    )

    if (state.isLanguageBottomSheetVisible) {
        MyLanguagesBottomSheet(
            languages = myLanguagesState.languages,
            dataStatus = myLanguagesState.dataStatus,
            isSettingActive = myLanguagesState.isSettingActive,
            languagePendingRemoval = myLanguagesState.languagePendingRemoval,
            removingLanguageId = myLanguagesState.removingLanguageId,
            languagePendingActivation = myLanguagesState.languagePendingActivation,
            onDismiss = { onMyLanguagesIntent(MyLanguagesIntent.Dismiss) },
            onAddNewLanguageClick = { onMyLanguagesIntent(MyLanguagesIntent.AddNewLanguageClicked) },
            onLanguageSelect = { selectedLang ->
                onMyLanguagesIntent(MyLanguagesIntent.RequestSetActiveLanguage(selectedLang))
            },
            onConfirmSetActiveLanguage = {
                onMyLanguagesIntent(MyLanguagesIntent.ConfirmSetActiveLanguage)
            },
            onDismissSetActiveDialog = {
                onMyLanguagesIntent(MyLanguagesIntent.DismissSetActiveDialog)
            },
            onRemoveLanguageClick = { lang ->
                onMyLanguagesIntent(MyLanguagesIntent.RequestRemoveLanguage(lang))
            },
            onConfirmRemoveLanguage = {
                onMyLanguagesIntent(MyLanguagesIntent.ConfirmRemoveLanguage)
            },
            onDismissRemoveDialog = {
                onMyLanguagesIntent(MyLanguagesIntent.DismissRemoveDialog)
            }
        )
    }

    DailyMissionDialog(
        state = state.dailyMissionState,
        onDismissRequest = { onHomeIntent(HomeIntent.DismissDailyMissionDialog) },
        onStartCamera = { word -> onHomeIntent(HomeIntent.StartDailyMissionCamera(word)) }
    )
}
