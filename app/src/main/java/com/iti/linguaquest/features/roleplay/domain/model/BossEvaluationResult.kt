package com.iti.linguaquest.features.roleplay.domain.model

data class BossEvaluationResult(
    val task_completed: Boolean,
    val fluency_score: Int = 0,
    val grammar_score: Int = 0,
    val vocabulary_score: Int = 0,
    val target_language_percentage: Int = 100,
    val feedback_message: String = "",
    val strengths: List<String> = emptyList(),
    val improvements: List<String> = emptyList(),
    val stars: Int = 0,
    val xp_earned: Int = 0,
    val coins_earned: Int = 0
)

