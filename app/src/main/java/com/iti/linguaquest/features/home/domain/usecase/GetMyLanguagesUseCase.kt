package com.iti.linguaquest.features.home.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.domain.model.UserLanguage
import com.iti.linguaquest.features.home.domain.repository.LanguagesRepo
import javax.inject.Inject

class GetMyLanguagesUseCase @Inject constructor(
    private val languagesRepo: LanguagesRepo
) {
    suspend operator fun invoke(): LinguaQuestResult<List<UserLanguage>, LinguaQuestDataError> =
        languagesRepo.getMyLanguages()
}
