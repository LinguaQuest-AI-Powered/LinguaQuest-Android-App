package com.iti.linguaquest.core.ai.network.model

import com.google.gson.annotations.SerializedName

data class GeminiRequestDto(
    @SerializedName("contents") val contents: List<GeminiContentDto>,
    @SerializedName("generationConfig") val generationConfig: GeminiGenerationConfigDto? = null,
    @SerializedName("systemInstruction") val systemInstruction: GeminiContentDto? = null
)

data class GeminiContentDto(
    @SerializedName("parts") val parts: List<GeminiPartDto>
)

data class GeminiPartDto(
    @SerializedName("text") val text: String? = null,
    @SerializedName("inlineData") val inlineData: GeminiInlineDataDto? = null
)

data class GeminiInlineDataDto(
    @SerializedName("mimeType") val mimeType: String,
    @SerializedName("data") val data: String
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
