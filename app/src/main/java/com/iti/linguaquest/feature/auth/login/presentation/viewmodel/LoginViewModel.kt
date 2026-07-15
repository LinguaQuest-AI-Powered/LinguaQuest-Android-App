package com.iti.linguaquest.feature.auth.login.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.iti.linguaquest.feature.auth.login.domain.usecase.LoginWithGoogleUseCase
import com.iti.linguaquest.feature.auth.login.domain.usecase.ContinueAsGuestUseCase
import com.iti.linguaquest.feature.auth.login.domain.usecase.LoginWithEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val continueAsGuestUseCase: ContinueAsGuestUseCase,

    ) : ViewModel() {

}
