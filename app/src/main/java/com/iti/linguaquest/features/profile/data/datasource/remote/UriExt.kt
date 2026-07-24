package com.iti.linguaquest.features.profile.data.datasource.remote


import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.ByteArrayOutputStream
import androidx.core.graphics.scale


fun Uri.toMultipartBodyPart(
    context: Context,
    partName: String,
    maxDimension: Int = 1024,
    quality: Int = 80
): MultipartBody.Part {
    val contentResolver = context.contentResolver

    val originalBytes = contentResolver.openInputStream(this)?.use { it.readBytes() }
        ?: throw IOException("Unable to read image data from $this")

    val bitmap = BitmapFactory.decodeByteArray(originalBytes, 0, originalBytes.size)
        ?: throw IOException("Unable to decode image at $this")

    val scale = maxDimension.toFloat() / maxOf(bitmap.width, bitmap.height)
    val scaledBitmap = if (scale < 1f) {
        bitmap.scale((bitmap.width * scale).toInt(), (bitmap.height * scale).toInt())
    } else bitmap

    val outputStream = ByteArrayOutputStream()
    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    val compressedBytes = outputStream.toByteArray()

    if (scaledBitmap != bitmap) scaledBitmap.recycle()
    bitmap.recycle()

    val requestBody = compressedBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
    val fileName = "avatar_${System.currentTimeMillis()}.jpg"
    return MultipartBody.Part.createFormData(partName, fileName, requestBody)
}