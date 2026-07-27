package com.iti.linguaquest.features.gallery.data.datasource.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import retrofit2.http.GET

interface GalleryApiService {
    @GET("gallery")
    suspend fun getGalleryWords(): SuccessResponseDto<GalleryResponseDto>
}
