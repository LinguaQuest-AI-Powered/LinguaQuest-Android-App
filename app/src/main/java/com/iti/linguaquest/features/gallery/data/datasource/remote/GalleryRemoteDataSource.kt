package com.iti.linguaquest.features.gallery.data.datasource.remote

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult

interface WordRemoteDataSource {
    suspend fun getGalleryWords(): LinguaQuestResult<GalleryResponseDto, LinguaQuestDataError>
}
