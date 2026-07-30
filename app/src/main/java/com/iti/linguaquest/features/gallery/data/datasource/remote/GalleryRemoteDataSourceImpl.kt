package com.iti.linguaquest.features.gallery.data.datasource.remote

import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import javax.inject.Inject

class GalleryRemoteDataSourceImpl @Inject constructor(
    private val api: GalleryApiService
) : WordRemoteDataSource {

    override suspend fun getGalleryWords(): LinguaQuestResult<GalleryResponseDto, LinguaQuestDataError> {
        val result = safeApiCall { api.getGalleryWords() }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data)
            is LinguaQuestResult.Failure -> result
        }
    }
}
