package com.iti.linguaquest.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.notification.domain.usecase.GetUnreadNotificationCountUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import com.iti.linguaquest.core.wallet.domain.usecase.GetWalletUseCase
import com.iti.linguaquest.core.wallet.domain.usecase.RefreshWalletUseCase
import com.iti.linguaquest.features.auth.domain.usecase.CheckUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getWalletUseCase: GetWalletUseCase,
    private val refreshWalletUseCase: RefreshWalletUseCase,
    private val getUnreadNotificationCountUseCase: GetUnreadNotificationCountUseCase,
    private val checkUserLoggedInUseCase: CheckUserLoggedInUseCase
) : ViewModel() {

    val wallet: StateFlow<Wallet> = getWalletUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Wallet(0, 0)
    )




    private val _unreadNotificationCount = MutableStateFlow(0)
    val unreadNotificationCount: StateFlow<Int> = _unreadNotificationCount.asStateFlow()

    private val _showLockScreenWordDialogId = MutableStateFlow<Int?>(null)
    val showLockScreenWordDialogId: StateFlow<Int?> = _showLockScreenWordDialogId.asStateFlow()

    fun showLockScreenWordDialog(wordId: Int) {
        _showLockScreenWordDialogId.value = wordId
    }

    fun hideLockScreenWordDialog() {
        _showLockScreenWordDialogId.value = null
    }

    init {
        viewModelScope.launch {
            checkUserLoggedInUseCase()
                .distinctUntilChanged()
                .collectLatest { isLoggedIn ->
                if (isLoggedIn) {
                    refreshWallet()
                    refreshUnreadCount()
                }
            }
        }
    }

    fun refreshWallet() {
        viewModelScope.launch {
            refreshWalletUseCase()
        }
    }

    fun refreshUnreadCount() {
        viewModelScope.launch {
            when (val result = getUnreadNotificationCountUseCase()) {
                is LinguaQuestResult.Success -> {
                    _unreadNotificationCount.value = result.data
                }
                is LinguaQuestResult.Failure -> {

                }
            }
        }
    }
}

