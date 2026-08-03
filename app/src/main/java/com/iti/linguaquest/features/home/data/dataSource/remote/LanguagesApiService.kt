package com.iti.linguaquest.features.home.data.dataSource.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.MyLanguagesResponseDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.AvailableLanguagesResponseDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.AddLanguagesRequestDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.SetActiveLanguageRequestDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.SetActiveLanguageResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.HTTP
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.RemoveLanguagesRequestDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.SetNativeLanguageRequestDto
import com.iti.linguaquest.features.home.data.dataSource.remote.dto.UserLanguageDto

interface LanguagesApiService {
    @GET("languages/mine")
    suspend fun getMyLanguages(): SuccessResponseDto<MyLanguagesResponseDto>

    @GET("languages/available")
    suspend fun getAvailableLanguages(): SuccessResponseDto<AvailableLanguagesResponseDto>

    @POST("languages")
    suspend fun addLanguages(
        @Body request: AddLanguagesRequestDto
    ): SuccessResponseDto<MyLanguagesResponseDto>

    @HTTP(method = "DELETE", path = "languages", hasBody = true)
    suspend fun removeLanguages(
        @Body request: RemoveLanguagesRequestDto
    ): SuccessResponseDto<MyLanguagesResponseDto>

    @PATCH("languages/active")
    suspend fun setActiveLanguage(
        @Body request: SetActiveLanguageRequestDto
    ): SuccessResponseDto<SetActiveLanguageResponseDto>

    @PATCH("languages/native")
    suspend fun setNativeLanguage(
        @Body request: SetNativeLanguageRequestDto
    ): SuccessResponseDto<UserLanguageDto>
}
