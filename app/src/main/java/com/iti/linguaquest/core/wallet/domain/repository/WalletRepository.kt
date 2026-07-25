package com.iti.linguaquest.core.wallet.domain.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    val wallet: Flow<Wallet>

    suspend fun refreshWallet(): LinguaQuestResult<Wallet, LinguaQuestDataError>
    
    suspend fun adjustWallet(
        xpDelta: Int,
        coinsDelta: Int
    ): LinguaQuestResult<Wallet, LinguaQuestDataError>
}
