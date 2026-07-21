package com.iti.linguaquest.features.home.presentation.mapper

import com.iti.linguaquest.R
import com.iti.linguaquest.features.home.domain.model.ContinueLesson
import com.iti.linguaquest.features.all_worlds.domain.model.World
import com.iti.linguaquest.features.home.domain.model.HomeSummary
import com.iti.linguaquest.features.all_worlds.domain.model.WorldDifficulty as DomainDifficulty
import com.iti.linguaquest.features.home.presentation.view.components.LessonPreview
import com.iti.linguaquest.features.home.presentation.view.components.WorldDifficulty as UiDifficulty
import com.iti.linguaquest.features.home.presentation.view.components.WorldItem
import com.iti.linguaquest.core.sharedComponents.text.UiText

data class LanguageProgressUi(
    val languageName: UiText,
    val level: Int,
    val streakDays: Int,
    val progress: Float,
    val flagSource: Any?
)

// TODO: temporary until backend images are live — delete this file's drawable lookups
// and switch WorldCard/ContinueLessonCard to ImageWrapper with the real imageUrl.
private fun localFlagFor(code: String): Int = when (code) {
    "es" -> R.drawable.flag_spain
    "fr" -> R.drawable.flag_france
    "ge" -> R.drawable.flag_germany
    "ja" -> R.drawable.flag_japan
    else -> R.drawable.flag_spain
}

private fun localWorldImageFor(worldName: String): Int = when (worldName) {
    "Kitchen World" -> R.drawable.kitchen_icon
    "City World" -> R.drawable.kitchen_icon
    else -> R.drawable.kitchen_icon
}

private fun localLessonImageFor(word: String): Int = when (word) {
    "Apple" -> R.drawable.apple_icon
    else -> R.drawable.apple_icon
}

fun HomeSummary.toLanguageProgressUi(): LanguageProgressUi = LanguageProgressUi(
    languageName = UiText.DynamicString(activeLanguage.name),
    level = activeLanguage.level,
    streakDays = streakDays,
    progress = activeLanguage.levelProgressPercent / 100f,
    flagSource = localFlagFor(activeLanguage.code)
)

fun World.toUiWorldItem(): WorldItem = WorldItem(
    id = id,
    title = UiText.DynamicString(name),
    imageSource = if (imageUrl.startsWith("http")) imageUrl else localWorldImageFor(name),
    difficulty = when (difficulty) {
        DomainDifficulty.EASY -> UiDifficulty.EASY
        DomainDifficulty.MEDIUM -> UiDifficulty.MEDIUM
        DomainDifficulty.HARD -> UiDifficulty.HARD
    },
    progress = progressPercent / 100f,
    isCompleted = completedLevels >= totalLevels
)

fun ContinueLesson.toUiLessonPreview(): LessonPreview = LessonPreview(
    lessonId = levelId,
    word = UiText.DynamicString(word),
    partOfSpeech = UiText.DynamicString(""),
    translation = UiText.DynamicString(translation),
    iconSource = if (imageUrl.startsWith("http")) imageUrl else localLessonImageFor(word),
    sentence = sentence
)