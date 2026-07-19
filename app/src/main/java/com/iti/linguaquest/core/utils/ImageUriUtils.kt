package com.iti.linguaquest.core.utils


import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun createImageCaptureUri(context: Context): Uri {
    val imagesDir = File(context.cacheDir, "images").apply { if (!exists()) mkdirs() }
    val file = File.createTempFile("avatar_", ".jpg", imagesDir)
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}