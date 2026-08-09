package com.iti.linguaquest.core.utils

import android.net.Uri
import java.io.File

interface FileHelper {
    fun uriToFile(uri: Uri): File?
}
