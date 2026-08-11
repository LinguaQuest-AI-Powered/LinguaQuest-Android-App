package com.iti.linguaquest.core.database.languages

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.iti.linguaquest.features.home.domain.model.UserLanguage

@Entity(tableName = "user_languages")
data class UserLanguageEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val code: String,
    val imageUrl: String,
    val level: Int,
    val isActive: Boolean,
    val progressPercent: Int
) {
    fun toDomain(): UserLanguage = UserLanguage(
        id = id,
        name = name,
        code = code,
        imageUrl = imageUrl,
        level = level,
        isActive = isActive,
        progressPercent = progressPercent
    )
}

fun UserLanguage.toEntity(): UserLanguageEntity = UserLanguageEntity(
    id = id,
    name = name,
    code = code,
    imageUrl = imageUrl,
    level = level,
    isActive = isActive,
    progressPercent = progressPercent
)
