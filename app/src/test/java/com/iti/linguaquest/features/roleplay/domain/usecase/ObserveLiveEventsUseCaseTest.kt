package com.iti.linguaquest.features.roleplay.domain.usecase

import app.cash.turbine.test
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ObserveLiveEventsUseCaseTest {

    private lateinit var repository: RoleplayRepository
    private lateinit var useCase: ObserveLiveEventsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = ObserveLiveEventsUseCase(repository)
    }

    @Test
    fun invoke_exposesRepositoryEventsFlow() = runTest {
        // Given
        val expectedEvent = RoleplayLiveEvent.Transcription("Bonjour", isUser = true)
        every { repository.events } returns flowOf(expectedEvent)

        // When
        val flow = useCase()

        // Then
        flow.test {
            val item = awaitItem()
            assertEquals(expectedEvent, item)
            awaitComplete()
        }
    }
}
