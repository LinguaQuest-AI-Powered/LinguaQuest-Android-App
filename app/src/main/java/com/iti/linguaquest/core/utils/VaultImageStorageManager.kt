package com.iti.linguaquest.core.utils

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VaultImageStorageManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val vaultImagesDir: File
        get() {
            val dir = File(context.filesDir, "vault_images")
            if (!dir.exists()) {
                dir.mkdirs()
            }
            return dir
        }

    private fun getStandardLanguageCode(language: String): String {
        return when (language.trim().lowercase()) {
            "spanish", "es" -> "es"
            "french", "fr" -> "fr"
            "german", "de" -> "de"
            "arabic", "ar" -> "ar"
            "english", "en" -> "en"
            "chinese", "zh" -> "zh"
            "italian", "it" -> "it"
            "korean", "ko" -> "ko"
            "japanese", "ja" -> "ja"
            "portuguese", "pt" -> "pt"
            else -> language.trim().lowercase().take(2)
        }
    }

    fun saveVaultImage(word: String, language: String, sourceFile: File): File? {
        if (word.isBlank() || language.isBlank()) return null
        return try {
            val sanitizedWord = word.trim().lowercase().replace(Regex("[^\\p{L}\\p{N}]"), "_")
            val langCode = getStandardLanguageCode(language)
            val destFile = File(vaultImagesDir, "${sanitizedWord}_${langCode}.jpg")
            sourceFile.copyTo(destFile, overwrite = true)
            destFile
        } catch (e: Exception) {
            null
        }
    }

    fun getVaultImageUri(word: String, language: String): String? {
        if (word.isBlank() || language.isBlank()) return null
        val sanitizedWord = word.trim().lowercase().replace(Regex("[^\\p{L}\\p{N}]"), "_")
        val langCode = getStandardLanguageCode(language)
        val file = File(vaultImagesDir, "${sanitizedWord}_${langCode}.jpg")
        return if (file.exists()) {
            Uri.fromFile(file).toString()
        } else {
            null
        }
    }
}
