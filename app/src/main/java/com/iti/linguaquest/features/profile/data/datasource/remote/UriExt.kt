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

    val orientation = try {
        java.io.ByteArrayInputStream(originalBytes).use { inputStream ->
            androidx.exifinterface.media.ExifInterface(inputStream).getAttributeInt(
                androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION,
                androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL
            )
        }
    } catch (e: Exception) {
        androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL
    }

    val rotationDegrees = when (orientation) {
        androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90 -> 90f
        androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_180 -> 180f
        androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270 -> 270f
        else -> 0f
    }

    val bitmap = BitmapFactory.decodeByteArray(originalBytes, 0, originalBytes.size)
        ?: throw IOException("Unable to decode image at $this")

    val scale = maxDimension.toFloat() / maxOf(bitmap.width, bitmap.height)
    var scaledBitmap = if (scale < 1f) {
        bitmap.scale((bitmap.width * scale).toInt(), (bitmap.height * scale).toInt())
    } else bitmap

    if (rotationDegrees != 0f) {
        val matrix = android.graphics.Matrix().apply { postRotate(rotationDegrees) }
        val rotatedBitmap = Bitmap.createBitmap(
            scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true
        )
        if (scaledBitmap != bitmap) {
            scaledBitmap.recycle()
        }
        scaledBitmap = rotatedBitmap
    }

    val outputStream = ByteArrayOutputStream()
    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    val compressedBytes = outputStream.toByteArray()

    if (scaledBitmap != bitmap) scaledBitmap.recycle()
    bitmap.recycle()

    val requestBody = compressedBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
    val fileName = "avatar_${System.currentTimeMillis()}.jpg"
    return MultipartBody.Part.createFormData(partName, fileName, requestBody)
}