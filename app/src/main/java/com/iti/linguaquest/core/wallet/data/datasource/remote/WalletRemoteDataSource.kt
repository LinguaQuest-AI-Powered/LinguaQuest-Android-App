package com.iti.linguaquest.core.wallet.data.datasource.remote

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.wallet.domain.model.Wallet

interface WalletRemoteDataSource {
    suspend fun getWallet(): LinguaQuestResult<Wallet, LinguaQuestDataError>
    
    suspend fun adjustWallet(
        xpDelta: Int, 
        coinsDelta: Int
    ): LinguaQuestResult<Wallet, LinguaQuestDataError>
}
