package com.iti.linguaquest.core.language.domain.usecase

import com.iti.linguaquest.core.language.domain.repository.SupportedLanguagesRepository
import javax.inject.Inject

class PrefetchSupportedLanguagesUseCase @Inject constructor(
    private val repository: SupportedLanguagesRepository
) {
    suspend operator fun invoke() {
        repository.getSupportedLanguages()
    }
}
