package com.iti.linguaquest.features.onBoarding.presentation.viewModel.splashViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.navigation.RootScreen
import com.iti.linguaquest.features.auth.domain.usecase.CheckUserLoggedInUseCase
import com.iti.linguaquest.features.onBoarding.domain.usecase.CheckIsFirstTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkIsFirstTimeUseCase: CheckIsFirstTimeUseCase,
    private val checkUserLoggedInUseCase: CheckUserLoggedInUseCase
) : ViewModel() {

    val destination: StateFlow<RootScreen?> = combine(
        checkIsFirstTimeUseCase(),
        checkUserLoggedInUseCase()
    ) { isFirstTime, isLoggedIn ->
        when {
            isFirstTime -> RootScreen.Onboarding
            !isLoggedIn -> RootScreen.Login
            else -> RootScreen.Main
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
