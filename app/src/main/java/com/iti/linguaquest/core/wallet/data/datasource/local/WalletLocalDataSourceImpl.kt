package com.iti.linguaquest.core.wallet.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.iti.linguaquest.core.di.WalletDataStore
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WalletLocalDataSourceImpl @Inject constructor(
    @WalletDataStore private val dataStore: DataStore<Preferences>
) : WalletLocalDataSource {

    override val wallet: Flow<Wallet> = dataStore.data.map { preferences ->
        Wallet(
            xp = preferences[WalletKeys.XP] ?: 0,
            coins = preferences[WalletKeys.COINS] ?: 0
        )
    }

    override suspend fun saveWallet(wallet: Wallet) {
        dataStore.edit { preferences ->
            preferences[WalletKeys.XP] = wallet.xp
            preferences[WalletKeys.COINS] = wallet.coins
        }
    }
}
