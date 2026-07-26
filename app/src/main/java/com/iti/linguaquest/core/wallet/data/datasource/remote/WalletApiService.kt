package com.iti.linguaquest.core.wallet.data.datasource.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.core.wallet.data.datasource.remote.dto.WalletAdjustRequestDto
import com.iti.linguaquest.core.wallet.data.datasource.remote.dto.WalletDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WalletApiService {
    
    @GET("wallet")
    suspend fun getWallet(): SuccessResponseDto<WalletDto>

    @POST("wallet/adjust")
    suspend fun adjustWallet(
        @Body request: WalletAdjustRequestDto
    ): SuccessResponseDto<WalletDto>
}
