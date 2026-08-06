package com.iti.linguaquest.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import com.iti.linguaquest.core.wallet.domain.usecase.GetWalletUseCase
import com.iti.linguaquest.core.wallet.domain.usecase.RefreshWalletUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getWalletUseCase: GetWalletUseCase,
    private val refreshWalletUseCase: RefreshWalletUseCase
) : ViewModel() {

    val wallet: StateFlow<Wallet> = getWalletUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Wallet(0, 0)
    )

    var lastActiveTab: NestedScreen = NestedScreen.Home

    init {
        refreshWallet()
    }

    fun refreshWallet() {
        viewModelScope.launch {
            refreshWalletUseCase()
        }
    }
}
