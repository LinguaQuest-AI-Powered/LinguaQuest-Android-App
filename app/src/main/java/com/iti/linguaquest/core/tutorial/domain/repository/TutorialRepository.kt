package com.iti.linguaquest.core.tutorial.domain.repository

import com.iti.linguaquest.core.tutorial.model.TourId

interface TutorialRepository {
    suspend fun isTourCompleted(tourId: TourId): Boolean
    suspend fun setTourCompleted(tourId: TourId, completed: Boolean)
    suspend fun resetAllTours()
}
