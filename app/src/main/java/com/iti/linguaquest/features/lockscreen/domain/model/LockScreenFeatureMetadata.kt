package com.iti.linguaquest.features.lockscreen.domain.model

data class LockScreenFeatureMetadata(
    val enabled: Boolean,
    val pendingGeneration: Boolean,
    val operationId: String? = null,
    val batchSize: Int? = null,
    val lastGenerationTime: Long? = null,
    val lastNativeLanguage: String? = null,
    val lastTargetLanguage: String? = null,
    val lastProficiencyLevel: String? = null
)
