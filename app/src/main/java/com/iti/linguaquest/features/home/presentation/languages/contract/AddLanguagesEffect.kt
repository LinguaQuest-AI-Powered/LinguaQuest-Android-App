package com.iti.linguaquest.features.home.presentation.languages.contract

sealed interface AddLanguagesEffect {
    data object NavigateBack : AddLanguagesEffect
}