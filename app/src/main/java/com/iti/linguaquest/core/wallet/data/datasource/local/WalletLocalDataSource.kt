package com.iti.linguaquest.core.wallet.data.datasource.local

import com.iti.linguaquest.core.wallet.domain.model.Wallet
import kotlinx.coroutines.flow.Flow

interface WalletLocalDataSource {
    val wallet: Flow<Wallet>

    suspend fun saveWallet(wallet: Wallet)
}
