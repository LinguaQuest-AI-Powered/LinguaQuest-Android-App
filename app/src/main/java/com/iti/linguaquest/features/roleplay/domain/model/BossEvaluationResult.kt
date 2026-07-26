package com.iti.linguaquest.features.roleplay.domain.model

data class BossEvaluationResult(
    val task_completed: Boolean,
    val fluency_score: Int,
    val feedback_message: String
)
