package com.iti.linguaquest.features.help.data
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.iti.linguaquest.R
enum class HelpTopic(
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int,
    @StringRes val detailRes: Int,
    @DrawableRes val imageRes: Int
) {
    FAQS(
        titleRes = R.string.help_faqs_title,
        subtitleRes = R.string.help_faqs_subtitle,
        detailRes = R.string.help_faqs_detail,
        imageRes = R.drawable.lingo_help_q
    ),
    CONTACT_US(
        titleRes = R.string.help_contact_title,
        subtitleRes = R.string.help_contact_subtitle,
        detailRes = R.string.help_contact_detail,
        imageRes = R.drawable.lingo_help_c
    ),
    USER_GUIDE(
        titleRes = R.string.help_guide_title,
        subtitleRes = R.string.help_guide_subtitle,
        detailRes = R.string.help_guide_detail,
        imageRes = R.drawable.lingo_help_a
    )
}
