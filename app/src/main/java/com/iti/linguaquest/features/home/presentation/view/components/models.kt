package com.iti.linguaquest.features.home.presentation.view.components


import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

enum class WorldDifficulty(val label: String, val badgeColor: Color) {
    EASY(label = "EASY", badgeColor = Color(0xFF3E8E5A)),
    MEDIUM(label = "MEDIUM", badgeColor = Color(0xFF2F7A6B)),
    HARD(label = "HARD", badgeColor = Color(0xFFC0532A))
}


data class WorldItem(
    val id: String,
    val title: String,
    @DrawableRes val imageRes: Int,
    val difficulty: WorldDifficulty,
    val progress: Float,
    val isCompleted: Boolean = false,
    val unlockLevel: Int? = null
)

data class LessonPreview(
    val lessonId: Int,
    val word: String,
    val partOfSpeech: String,
    val translation: String,
    @DrawableRes val iconRes: Int
)