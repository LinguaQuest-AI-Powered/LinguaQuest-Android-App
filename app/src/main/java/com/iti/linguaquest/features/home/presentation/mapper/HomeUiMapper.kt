package com.iti.linguaquest.features.home.presentation.mapper

import com.iti.linguaquest.R
import com.iti.linguaquest.features.all_worlds.domain.model.World
import com.iti.linguaquest.features.home.domain.model.HomeSummary
import com.iti.linguaquest.features.all_worlds.domain.model.WorldDifficulty as DomainDifficulty
import com.iti.linguaquest.features.home.presentation.view.components.WorldDifficulty as UiDifficulty
import com.iti.linguaquest.features.home.presentation.view.components.WorldItem
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.utils.toFlagEmoji
import com.iti.linguaquest.features.home.presentation.contract.ContinueLevelUi

data class LanguageProgressUi(
    val languageName: UiText,
    val level: Int,
    val streakDays: Int,
    val progress: Float,
    val flagSource: Any?
)

private fun localWorldImageFor(worldName: String): Int = when (worldName) {
    "Kitchen World", "Kitchen" -> R.drawable.kitchen_icon
    "City World", "City" -> R.drawable.kitchen_icon
    else -> R.drawable.kitchen_icon
}

fun HomeSummary.toLanguageProgressUi(): LanguageProgressUi? {
    val lang = activeLanguage ?: return null
    val flag: Any = lang.code.toFlagEmoji()
    return LanguageProgressUi(
        languageName = UiText.DynamicString(lang.name),
        level = lang.level,
        streakDays = streakDays,
        progress = lang.levelProgressPercent / 100f,
        flagSource = flag
    )
}

fun World.toUiWorldItem(): WorldItem = WorldItem(
    id = id,
    title = UiText.DynamicString(name),
    imageSource = if (imageUrl.isNotBlank() && (imageUrl.startsWith("http") || imageUrl.contains("/"))) imageUrl else localWorldImageFor(
        name
    ),
    difficulty = when (difficulty) {
        DomainDifficulty.EASY -> UiDifficulty.EASY
        DomainDifficulty.MEDIUM -> UiDifficulty.MEDIUM
        DomainDifficulty.HARD -> UiDifficulty.HARD
    },
    progress = progressPercent / 100f,
    isCompleted = completedLevels >= totalLevels
)

fun HomeSummary.toContinueLevelUi(): ContinueLevelUi? {
    val level = continueLevel ?: return null
    val matchingWorld = exploreWorlds.find { it.id == level.worldId }
    val totalLevels = matchingWorld?.totalLevels ?: 10

    return ContinueLevelUi(
        worldId = level.worldId,
        levelId = level.levelId,
        worldName = UiText.DynamicString("${level.worldName} World"),
        targetWord = UiText.DynamicString(level.word),
        levelOrder = level.levelOrder,
        totalLevels = totalLevels
    )
}

