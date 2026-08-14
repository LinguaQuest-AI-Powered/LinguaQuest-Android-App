package com.iti.linguaquest.core.tutorial.domain.usecase

import com.iti.linguaquest.core.tutorial.domain.repository.TutorialRepository
import com.iti.linguaquest.core.tutorial.model.TourId
import javax.inject.Inject

class SetTourCompletedUseCase @Inject constructor(
    private val repository: TutorialRepository
) {
    suspend operator fun invoke(tourId: TourId, completed: Boolean) {
        repository.setTourCompleted(tourId, completed)
    }
}
