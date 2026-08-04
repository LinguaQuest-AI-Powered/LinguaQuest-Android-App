package com.iti.linguaquest.features.voicegame.presentation.viewModel

import app.cash.turbine.test
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import com.iti.linguaquest.core.wallet.domain.usecase.GetWalletUseCase
import com.iti.linguaquest.util.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class VoiceResultViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var observeNetworkStatusUseCase: ObserveNetworkStatusUseCase
    private lateinit var getWalletUseCase: GetWalletUseCase
    private lateinit var viewModel: VoiceResultViewModel

    @Before
    fun setUp() {
        observeNetworkStatusUseCase = mockk()
        getWalletUseCase = mockk()

        every { observeNetworkStatusUseCase() } returns flowOf(true)
        every { getWalletUseCase() } returns flowOf(Wallet(xp = 250, coins = 100))

        viewModel = VoiceResultViewModel(
            observeNetworkStatusUseCase = observeNetworkStatusUseCase,
            getWalletUseCase = getWalletUseCase
        )
    }

    @Test
    fun isOnline_emitsInitialStateCorrectly() = runTest {
        viewModel.isOnline.test {
            assertTrue(awaitItem())
        }
    }

    @Test
    fun wallet_emitsInitialWalletCorrectly() = runTest {
        viewModel.wallet.test {
            val initial = awaitItem()
            val item = if (initial.xp == 0) awaitItem() else initial
            assertEquals(250, item.xp)
            assertEquals(100, item.coins)
        }
    }
}
