package com.iti.linguaquest.features.help.presentation.contact.contract

import com.iti.linguaquest.features.help.data.ContactUsDefaults
import com.iti.linguaquest.features.help.data.ContactUsSection

data class ContactUsState(
    val email: String = ContactUsDefaults.email,
    val sections: List<ContactUsSection> = ContactUsDefaults.sections
)

sealed interface ContactUsIntent {
    data object OnBackClicked : ContactUsIntent
}

sealed interface ContactUsEffect {
    data object NavigateBack : ContactUsEffect
}
