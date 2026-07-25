package com.iti.linguaquest.features.profile.presentation.editprofile.utils

import android.content.Context
import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import com.iti.linguaquest.core.sharedComponents.text.UiText
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


data class FieldError(
    val isError: Boolean = false,
    val message: UiText? = null,
    val shakeTrigger: Int = 0
)


fun Modifier.shakeOnError(isError: Boolean, shakeTrigger: Int = 0): Modifier = composed {
    val offsetX = remember { Animatable(0f) }

    LaunchedEffect(isError, shakeTrigger) {
        if (isError) {
            offsetX.snapTo(0f)
            listOf(-12f, 12f, -10f, 10f, -6f, 6f, -3f, 3f, 0f).forEach { target ->
                offsetX.animateTo(
                    targetValue = target,
                    animationSpec = tween(durationMillis = 40, easing = LinearEasing)
                )
            }
        }
    }

    this.graphicsLayer { translationX = offsetX.value }
}

fun Uri.toMultipartBody(
    context: Context,
    partName: String = "file"
): MultipartBody.Part {
    val contentResolver = context.contentResolver
    val mimeType = contentResolver.getType(this) ?: "image/jpeg"
    val inputStream = contentResolver.openInputStream(this)
        ?: throw IllegalArgumentException("Unable to open input stream for uri: $this")
    val bytes = inputStream.use { it.readBytes() }
    val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
    val fileName = "upload_${System.currentTimeMillis()}.jpg"
    return MultipartBody.Part.createFormData(partName, fileName, requestBody)
}