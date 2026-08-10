package com.iti.linguaquest.core.utils

import timber.log.Timber


import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun createImageCaptureUri(context: Context): Uri {
    val imagesDir = File(context.cacheDir, "images").apply { if (!exists()) mkdirs() }
    val file = File.createTempFile("avatar_", ".jpg", imagesDir)
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}

fun Uri.toTempFile(context: Context, prefix: String = "upload_"): File? {
    return try {
        if (scheme == "file" && !path.isNullOrEmpty()) {
            File(path!!)
        } else {
            val inputStream = context.contentResolver.openInputStream(this) ?: return null
            val tempFile = File.createTempFile(prefix, ".jpg", context.cacheDir)
            tempFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            tempFile
        }
    } catch (e: Exception) {
        Timber.e(e, "Failed to convert Uri to File")
        null
    }
}