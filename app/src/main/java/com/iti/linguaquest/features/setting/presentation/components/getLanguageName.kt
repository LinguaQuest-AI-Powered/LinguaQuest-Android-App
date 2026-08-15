package com.iti.linguaquest.features.setting.presentation.components
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R

@Composable
  fun getLanguageName(languageCode: String): String {
    return when (languageCode) {
        "es" -> stringResource(id = R.string.lang_spanish)
        "fr" -> stringResource(id = R.string.lang_french)
        "ja" -> stringResource(id = R.string.lang_japanese)
        "de", "ge" -> stringResource(id = R.string.lang_german)
        "it" -> stringResource(id = R.string.lang_italian)
        "ko" -> stringResource(id = R.string.lang_korean)
        "pt" -> stringResource(id = R.string.lang_portuguese)
        "zh" -> stringResource(id = R.string.lang_chinese)
        "ru" -> stringResource(id = R.string.lang_russian)
        "ar" -> stringResource(id = R.string.lang_arabic)
        else -> stringResource(id = R.string.lang_english)
    }
}