package com.iti.linguaquest.core.database.word

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey
    val id: Int,
    val sourceWord: String,
    val translatedWord: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val category: String,
    val imagePath: String,
    val isCorrect: Boolean = false
)