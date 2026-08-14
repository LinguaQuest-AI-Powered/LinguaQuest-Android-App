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

    fun saveVaultImage(word: String, language: String, sourceFile: File): File? {
        if (word.isBlank() || language.isBlank()) return null
        return try {
            val sanitizedWord = word.trim().lowercase().replace(Regex("[^a-z0-9]"), "_")
            val sanitizedLanguage = language.trim().lowercase().replace(Regex("[^a-z0-9]"), "_")
            val destFile = File(vaultImagesDir, "${sanitizedWord}_${sanitizedLanguage}.jpg")
            sourceFile.copyTo(destFile, overwrite = true)
            destFile
        } catch (e: Exception) {
            null
        }
    }

    fun getVaultImageUri(word: String, language: String): String? {
        if (word.isBlank() || language.isBlank()) return null
        val sanitizedWord = word.trim().lowercase().replace(Regex("[^a-z0-9]"), "_")
        val sanitizedLanguage = language.trim().lowercase().replace(Regex("[^a-z0-9]"), "_")
        val file = File(vaultImagesDir, "${sanitizedWord}_${sanitizedLanguage}.jpg")
        return Uri.fromFile(file).toString()
    }
}
