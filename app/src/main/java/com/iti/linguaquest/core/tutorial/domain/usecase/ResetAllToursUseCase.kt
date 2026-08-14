package com.iti.linguaquest.core.tutorial.domain.usecase

import com.iti.linguaquest.core.tutorial.domain.repository.TutorialRepository
import javax.inject.Inject

class ResetAllToursUseCase @Inject constructor(
    private val repository: TutorialRepository
) {
    suspend operator fun invoke() {
        repository.resetAllTours()
    }
}
