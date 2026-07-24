package com.iti.linguaquest.features.home.presentation.mapper

import com.iti.linguaquest.core.utils.toFlagEmoji
import com.iti.linguaquest.features.home.domain.model.UserLanguage
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import com.iti.linguaquest.features.home.presentation.languages.contract.MyLanguageUiModel
import com.iti.linguaquest.features.home.presentation.languages.contract.LanguageUiItem

fun UserLanguage.toUiModel(): MyLanguageUiModel = MyLanguageUiModel(
    id = id,
    name = name,
    level = level,
    isCurrent = isActive,
    flagEmoji = code.toFlagEmoji()
)

fun LanguageOption.toUiItem(): LanguageUiItem = LanguageUiItem(
    id = id,
    name = name,
    flagEmoji = code.toFlagEmoji(),
    isAdded = isAdded
)


