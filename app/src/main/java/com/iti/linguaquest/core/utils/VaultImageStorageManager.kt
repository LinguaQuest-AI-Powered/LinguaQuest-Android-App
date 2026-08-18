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
            "spanish", "es", "الإسبانية", "الاسبانية" -> "es"
            "french", "fr", "الفرنسية" -> "fr"
            "german", "de", "الألمانية", "الالمانية" -> "de"
            "arabic", "ar", "العربية" -> "ar"
            "english", "en", "الإنجليزية", "الانجليزية" -> "en"
            "chinese", "zh", "الصينية" -> "zh"
            "italian", "it", "الإيطالية", "الايطالية" -> "it"
            "korean", "ko", "الكورية" -> "ko"
            "japanese", "ja", "اليابانية" -> "ja"
            "portuguese", "pt", "البرتغالية" -> "pt"
            else -> language.trim().lowercase().take(2)
        }
    }

    fun saveVaultImage(word: String, languageCode: String, sourceFile: File): File? {
        if (word.isBlank()) return null
        return try {
            val sanitizedWord = word.trim().lowercase().replace(Regex("[^\\p{L}\\p{N}]"), "_")
            val langCode = if (languageCode.isBlank()) "en" else getStandardLanguageCode(languageCode)
            val destFile = File(vaultImagesDir, "${sanitizedWord}_${langCode}.jpg")
            sourceFile.copyTo(destFile, overwrite = true)
            destFile
        } catch (e: Exception) {
            null
        }
    }

    fun getVaultImageUri(word: String, languageCode: String): String? {
        if (word.isBlank()) return null
        val sanitizedWord = word.trim().lowercase().replace(Regex("[^\\p{L}\\p{N}]"), "_")
        val langCode = if (languageCode.isBlank()) "en" else getStandardLanguageCode(languageCode)
        val file = File(vaultImagesDir, "${sanitizedWord}_${langCode}.jpg")
        if (file.exists()) {
            return Uri.fromFile(file).toString()
        }
        val fallbackFile = vaultImagesDir.listFiles()?.firstOrNull {
            it.name.startsWith("${sanitizedWord}_") || it.name == "${sanitizedWord}.jpg"
        }
        return fallbackFile?.let { Uri.fromFile(it).toString() }
    }
}
