package com.iti.linguaquest.features.profile.domain.usecase

import android.content.Context
import coil.ImageLoader
import coil.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class PreloadImageUseCase @Inject constructor(
    private val imageLoader: ImageLoader,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(url: Any) {
        runCatching {
            val request = ImageRequest.Builder(context).data(url).build()
            imageLoader.execute(request)
        }
    }
}