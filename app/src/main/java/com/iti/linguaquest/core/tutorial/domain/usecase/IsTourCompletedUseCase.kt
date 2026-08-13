package com.iti.linguaquest.core.tutorial.domain.usecase

import com.iti.linguaquest.core.tutorial.domain.repository.TutorialRepository
import com.iti.linguaquest.core.tutorial.model.TourId
import javax.inject.Inject

class IsTourCompletedUseCase @Inject constructor(
    private val repository: TutorialRepository
) {
    suspend operator fun invoke(tourId: TourId): Boolean {
        return repository.isTourCompleted(tourId)
    }
}
