package com.iti.linguaquest.core.ai.network.model

import com.google.gson.annotations.SerializedName

data class GatewayChatRequestDto(
    @SerializedName("model_id") val modelId: String,
    @SerializedName("model") val model: String? = null,
    @SerializedName("messages") val messages: List<GatewayMessageDto>,
    @SerializedName("temperature") val temperature: Float? = null,
    @SerializedName("response_format") val responseFormat: GatewayResponseFormatDto? = null,
    @SerializedName("stream") val stream: Boolean? = null
)

data class GatewayMessageDto(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)

data class GatewayResponseFormatDto(
    @SerializedName("type") val type: String = "json_object"
)

data class GatewayChatResponseDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("output_text") val outputText: String? = null,
    @SerializedName("output") val output: String? = null,
    @SerializedName("text") val text: String? = null,
    @SerializedName("choices") val choices: List<GatewayChoiceDto>? = null,
    @SerializedName("content") val content: String? = null,
    @SerializedName("response") val response: String? = null,
    @SerializedName("message") val message: GatewayMessageDto? = null,
    @SerializedName("usage") val usage: GatewayUsageDto? = null,
    @SerializedName("error") val error: GatewayErrorDto? = null
)

data class GatewayChoiceDto(
    @SerializedName("index") val index: Int? = null,
    @SerializedName("message") val message: GatewayMessageDto? = null,
    @SerializedName("finish_reason") val finishReason: String? = null
)

data class GatewayUsageDto(
    @SerializedName("prompt_tokens") val promptTokens: Int? = null,
    @SerializedName("completion_tokens") val completionTokens: Int? = null,
    @SerializedName("total_tokens") val totalTokens: Int? = null
)

data class GatewayErrorDto(
    @SerializedName("message") val message: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("code") val code: String? = null
)
