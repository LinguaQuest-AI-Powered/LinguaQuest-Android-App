package com.iti.linguaquest.features.profile.data.datasource.remote


import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

fun Uri.toMultipartBodyPart(context: Context, partName: String): MultipartBody.Part {
    val contentResolver = context.contentResolver
    val mimeType = contentResolver.getType(this) ?: "image/jpeg"
    val extension = when (mimeType) {
        "image/png" -> "png"
        "image/webp" -> "webp"
        else -> "jpg"
    }
    val bytes = contentResolver.openInputStream(this)?.use { it.readBytes() }
        ?: throw IOException("Unable to read image data from $this")
    val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
    val fileName = "avatar_${System.currentTimeMillis()}.$extension"
    return MultipartBody.Part.createFormData(partName, fileName, requestBody)
}