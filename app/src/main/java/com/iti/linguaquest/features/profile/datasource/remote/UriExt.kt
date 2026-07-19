package com.iti.linguaquest.features.profile.datasource.remote


import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

fun Uri.toMultipartBodyPart(context: Context, partName: String): MultipartBody.Part {
    val contentResolver = context.contentResolver
    val mimeType = contentResolver.getType(this) ?: "image/jpeg"
    val bytes = contentResolver.openInputStream(this)?.use { it.readBytes() }
        ?: throw IOException("Unable to read image data from $this")
    val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
    val fileName = "avatar_${System.currentTimeMillis()}.jpg"
    return MultipartBody.Part.createFormData(partName, fileName, requestBody)
}