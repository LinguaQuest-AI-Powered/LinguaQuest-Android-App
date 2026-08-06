package com.iti.linguaquest.features.roleplay.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class GeminiRequestDto(
    @SerializedName("contents") val contents: List<GeminiContentDto>,
    @SerializedName("generationConfig") val generationConfig: GeminiGenerationConfigDto? = null
)

data class GeminiContentDto(
    @SerializedName("parts") val parts: List<GeminiPartDto>
)

data class GeminiPartDto(
    @SerializedName("text") val text: String
)

data class GeminiGenerationConfigDto(
    @SerializedName("temperature") val temperature: Float = 0.1f,
    @SerializedName("responseMimeType") val responseMimeType: String? = null
)

data class GeminiResponseDto(
    @SerializedName("candidates") val candidates: List<GeminiCandidateDto>?
)

data class GeminiCandidateDto(
    @SerializedName("content") val content: GeminiContentResponseDto?
)

data class GeminiContentResponseDto(
    @SerializedName("parts") val parts: List<GeminiPartDto>?
)
