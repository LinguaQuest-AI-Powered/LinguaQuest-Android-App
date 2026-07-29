package com.iti.linguaquest.features.help.data

import androidx.annotation.StringRes
import com.iti.linguaquest.R

data class ContactUsSection(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int
)

object ContactUsDefaults {
    val email: String = "support@languageapp.com"
    val sections: List<ContactUsSection> = listOf(
        ContactUsSection(
            titleRes = R.string.contact_section_1_title,
            descriptionRes = R.string.contact_section_1_desc
        ),
        ContactUsSection(
            titleRes = R.string.contact_section_2_title,
            descriptionRes = R.string.contact_section_2_desc
        ),
        ContactUsSection(
            titleRes = R.string.contact_section_3_title,
            descriptionRes = R.string.contact_section_3_desc
        )
    )
}
