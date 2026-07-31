package com.iti.linguaquest.features.gallery.data.datasource.remote

data class GalleryResponseDto(
    val totalCount: Int? = null,
    val words: List<GalleryWordDto>? = null
)

data class GalleryWordDto(
    val word: String? = null,
    val nativeWord: String? = null,
    val world: GalleryWorldDto? = null
)

data class GalleryWorldDto(
    val id: Int? = null,
    val name: String? = null,
    val imageUrl: String? = null
)
