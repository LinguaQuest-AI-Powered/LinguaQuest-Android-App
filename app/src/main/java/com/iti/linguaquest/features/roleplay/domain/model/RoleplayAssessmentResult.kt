package com.iti.linguaquest.features.roleplay.domain.model

data class RoleplayAssessmentResult(
    val isTaskCompleted: Boolean,
    val fluencyScore: Int,
    val feedbackMessage: String
)
