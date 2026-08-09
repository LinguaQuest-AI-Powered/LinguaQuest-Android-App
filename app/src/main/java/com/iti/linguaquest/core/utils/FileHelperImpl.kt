package com.iti.linguaquest.core.utils

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class FileHelperImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FileHelper {
    override fun uriToFile(uri: Uri): File? {
        return uri.toTempFile(context, "daily_mission_")
    }
}
