package com.iti.linguaquest.core.wallet.data.datasource.remote

import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.wallet.data.datasource.remote.dto.WalletAdjustRequestDto
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import javax.inject.Inject

class WalletRemoteDataSourceImpl @Inject constructor(
    private val api: WalletApiService
) : WalletRemoteDataSource {

    override suspend fun getWallet(): LinguaQuestResult<Wallet, LinguaQuestDataError> {
        return when (val result = safeApiCall { api.getWallet() }) {
            is LinguaQuestResult.Success -> {
                val data = result.data.data
                LinguaQuestResult.Success(Wallet(xp = data.xp, coins = data.coins))
            }
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun adjustWallet(
        xpDelta: Int,
        coinsDelta: Int
    ): LinguaQuestResult<Wallet, LinguaQuestDataError> {
        return when (val result = safeApiCall(mapServerError = ::mapWalletAdjustError) {
            api.adjustWallet(WalletAdjustRequestDto(xpDelta = xpDelta, coinsDelta = coinsDelta))
        }) {
            is LinguaQuestResult.Success -> {
                val data = result.data.data
                LinguaQuestResult.Success(Wallet(xp = data.xp, coins = data.coins))
            }
            is LinguaQuestResult.Failure -> result
        }
    }

    private fun mapWalletAdjustError(errorKey: String, errorMessage: String): LinguaQuestDataError {
        return when (errorKey.uppercase()) {
            "INSUFFICIENT_BALANCE" -> LinguaQuestDataError.Remote.INSUFFICIENT_BALANCE
            else -> LinguaQuestDataError.CustomServerMessage(errorMessage)
        }
    }
}
