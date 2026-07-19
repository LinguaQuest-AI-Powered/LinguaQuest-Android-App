package com.iti.linguaquest.features.home.data.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.features.home.data.remote.dto.MyLanguagesResponseDto
import com.iti.linguaquest.features.home.data.remote.dto.AvailableLanguagesResponseDto
import com.iti.linguaquest.features.home.data.remote.dto.AddLanguagesRequestDto
import com.iti.linguaquest.features.home.data.remote.dto.SetActiveLanguageRequestDto
import com.iti.linguaquest.features.home.data.remote.dto.SetActiveLanguageResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PATCH

interface LanguagesApiService {
    @GET("languages/mine")
    suspend fun getMyLanguages(): SuccessResponseDto<MyLanguagesResponseDto>

    @GET("languages/available")
    suspend fun getAvailableLanguages(): SuccessResponseDto<AvailableLanguagesResponseDto>

    @POST("languages")
    suspend fun addLanguages(
        @Body request: AddLanguagesRequestDto
    ): SuccessResponseDto<MyLanguagesResponseDto>

    @PATCH("languages/active")
    suspend fun setActiveLanguage(
        @Body request: SetActiveLanguageRequestDto
    ): SuccessResponseDto<SetActiveLanguageResponseDto>
}
