package com.iti.linguaquest.features.home.presentation.languages.mylanguages.contract

import com.iti.linguaquest.core.sharedComponents.state.DataStatus

data class MyLanguagesState(
    val dataStatus: DataStatus = DataStatus.Loading,
    val languages: List<MyLanguageUiModel> = emptyList(),
    val isSettingActive: Boolean = false,
    val isRemoving: Boolean = false,
    val removingLanguageId: Int? = null,
    val languagePendingRemoval: MyLanguageUiModel? = null,
    val languagePendingActivation: MyLanguageUiModel? = null
) {
    val hasData: Boolean get() = languages.isNotEmpty()
}
