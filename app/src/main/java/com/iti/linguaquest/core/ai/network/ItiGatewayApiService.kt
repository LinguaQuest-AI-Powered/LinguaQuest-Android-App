package com.iti.linguaquest.core.ai.network

import com.iti.linguaquest.core.ai.network.model.GatewayChatRequestDto
import com.iti.linguaquest.core.ai.network.model.GatewayChatResponseDto
import com.iti.linguaquest.core.network.NoAuth
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ItiGatewayApiService {

    @NoAuth
    @POST("chat")
    suspend fun createChatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: GatewayChatRequestDto
    ): Response<GatewayChatResponseDto>
}
