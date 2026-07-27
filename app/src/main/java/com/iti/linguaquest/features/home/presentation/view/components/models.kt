package com.iti.linguaquest.features.home.presentation.view.components


import com.iti.linguaquest.core.sharedComponents.text.UiText
import androidx.compose.ui.graphics.Color

enum class WorldDifficulty(val label: String, val badgeColor: Color) {
    EASY(label = "EASY", badgeColor = Color(0xFF3E8E5A)),
    MEDIUM(label = "MEDIUM", badgeColor = Color(0xFF2F7A6B)),
    HARD(label = "HARD", badgeColor = Color(0xFFC0532A))
}


data class WorldItem(
    val id: Int,
    val title: UiText,
    val imageSource: Any?,
    val difficulty: WorldDifficulty,
    val progress: Float,
    val isCompleted: Boolean = false,
    val unlockLevel: Int? = null
)

