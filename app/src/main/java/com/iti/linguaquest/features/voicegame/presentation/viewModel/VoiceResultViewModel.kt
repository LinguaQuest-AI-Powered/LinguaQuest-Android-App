package com.iti.linguaquest.features.voicegame.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import com.iti.linguaquest.core.wallet.domain.usecase.GetWalletUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VoiceResultViewModel @Inject constructor(
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
    getWalletUseCase: GetWalletUseCase
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()

        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )
    val wallet: StateFlow<Wallet> = getWalletUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Wallet(xp = 0, coins = 0)
    )
}
