package com.iti.linguaquest.features.map.data.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.features.map.data.remote.dto.WorldMapDetailDto
import retrofit2.http.GET
import retrofit2.http.Path

interface MapApiService {
    @GET("worlds/{worldId}/levels")
    suspend fun getWorldMapDetail(
        @Path("worldId") worldId: Int
    ): SuccessResponseDto<WorldMapDetailDto>

}
