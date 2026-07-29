package com.iti.linguaquest.features.setting.presentation.about_app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R

@Composable
fun AboutAppScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val playStoreUrl = stringResource(id = R.string.about_app_play_store_url)
    val websiteUrl = stringResource(id = R.string.about_app_website_url)
    val privacyUrl = stringResource(id = R.string.about_app_privacy_url)
    val termsUrl = stringResource(id = R.string.about_app_terms_url)

    AboutAppContent(
        onBackClick = onBackClick,
        onRateAppClick = {
            runCatching { uriHandler.openUri(playStoreUrl) }
        },
        onWebsiteClick = {
            runCatching { uriHandler.openUri(websiteUrl) }
        },
        onPrivacyClick = {
            runCatching { uriHandler.openUri(privacyUrl) }
        },
        onTermsClick = {
            runCatching { uriHandler.openUri(termsUrl) }
        },
        modifier = modifier
    )
}
