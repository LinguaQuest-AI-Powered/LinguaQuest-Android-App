package com.iti.linguaquest.features.home.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.domain.repository.LanguagesRepo
import javax.inject.Inject

class RefreshMyLanguagesUseCase @Inject constructor(
    private val languagesRepo: LanguagesRepo
) {
    suspend operator fun invoke(): LinguaQuestResult<Unit, LinguaQuestDataError> =
        languagesRepo.refreshMyLanguages()
}
