package com.iti.linguaquest.features.home.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.domain.model.UserLanguage
import com.iti.linguaquest.features.home.domain.repository.LanguagesRepo
import javax.inject.Inject

import kotlinx.coroutines.flow.Flow

class GetMyLanguagesUseCase @Inject constructor(
    private val languagesRepo: LanguagesRepo
) {
    operator fun invoke(): Flow<LinguaQuestResult<List<UserLanguage>, LinguaQuestDataError>> =
        languagesRepo.getMyLanguages()
}
