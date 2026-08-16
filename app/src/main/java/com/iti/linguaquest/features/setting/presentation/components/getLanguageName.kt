package com.iti.linguaquest.features.setting.presentation.components
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R

@Composable
  fun getLanguageName(languageCode: String): String {
    return when (languageCode) {
        "es" -> stringResource(id = R.string.lang_spanish)
        "fr" -> stringResource(id = R.string.lang_french)
        "de", "ge" -> stringResource(id = R.string.lang_german)
        "ar" -> stringResource(id = R.string.lang_arabic)
        else -> stringResource(id = R.string.lang_english)
    }
}