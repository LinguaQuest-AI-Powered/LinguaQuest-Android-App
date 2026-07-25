package com.iti.linguaquest.core.wallet.data.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.wallet.data.datasource.local.WalletLocalDataSource
import com.iti.linguaquest.core.wallet.data.datasource.remote.WalletRemoteDataSource
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import com.iti.linguaquest.core.wallet.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val remoteDataSource: WalletRemoteDataSource,
    private val localDataSource: WalletLocalDataSource
) : WalletRepository {

    override val wallet: Flow<Wallet> = localDataSource.wallet

    override suspend fun refreshWallet(): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return when (val result = remoteDataSource.getWallet()) {
            is LinguaQuestResult.Success -> {
                localDataSource.saveWallet(result.data)
                LinguaQuestResult.Success(Unit)
            }
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun adjustWallet(
        xpDelta: Int,
        coinsDelta: Int
    ): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return when (val result = remoteDataSource.adjustWallet(xpDelta, coinsDelta)) {
            is LinguaQuestResult.Success -> {
                localDataSource.saveWallet(result.data)
                LinguaQuestResult.Success(Unit)
            }
            is LinguaQuestResult.Failure -> result
        }
    }
}
