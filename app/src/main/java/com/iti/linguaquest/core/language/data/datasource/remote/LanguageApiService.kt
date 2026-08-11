package com.iti.linguaquest.core.language.data.datasource.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.core.network.NoAuth
import com.iti.linguaquest.core.language.data.datasource.remote.dto.SupportedLanguagesResponseDataDto
import retrofit2.http.GET

interface LanguageApiService {
    @NoAuth
    @GET("auth/languages")
    suspend fun getSupportedLanguages(): SuccessResponseDto<SupportedLanguagesResponseDataDto>
}
