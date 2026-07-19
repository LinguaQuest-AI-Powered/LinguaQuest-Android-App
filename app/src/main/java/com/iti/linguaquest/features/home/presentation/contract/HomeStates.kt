package com.iti.linguaquest.features.home.presentation.contract


import com.iti.linguaquest.features.home.presentation.mapper.LanguageProgressUi
import com.iti.linguaquest.features.home.presentation.view.components.LessonPreview
import com.iti.linguaquest.features.home.presentation.view.components.WorldItem

data class HomeState(
    val isLoading: Boolean = true,
    val languageProgress: LanguageProgressUi? = null,
    val worlds: List<WorldItem> = emptyList(),
    val continueLesson: LessonPreview? = null,
    val hasError: Boolean = false,
    val isLanguageBottomSheetVisible: Boolean = false
)



