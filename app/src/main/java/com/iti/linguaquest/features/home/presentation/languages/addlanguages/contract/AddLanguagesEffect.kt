package com.iti.linguaquest.features.home.presentation.languages.addlanguages.contract

sealed interface AddLanguagesEffect {
    data object NavigateBack : AddLanguagesEffect
}
