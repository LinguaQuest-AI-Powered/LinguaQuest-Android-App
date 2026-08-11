package com.iti.linguaquest.features.home.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import com.iti.linguaquest.core.language.domain.usecase.GetSupportedLanguagesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAvailableLanguagesUseCase @Inject constructor(
    private val getMyLanguagesUseCase: GetMyLanguagesUseCase,
    private val getSupportedLanguagesUseCase: GetSupportedLanguagesUseCase
) {
    operator fun invoke(): Flow<LinguaQuestResult<List<LanguageOption>, LinguaQuestDataError>> {
        return getMyLanguagesUseCase().map { myLanguagesResult ->
            when (myLanguagesResult) {
                is LinguaQuestResult.Success -> {
                    val myLanguageIds = myLanguagesResult.data.map { it.id }.toSet()
                    val supportedLanguagesResult = getSupportedLanguagesUseCase()
                    
                    if (supportedLanguagesResult is LinguaQuestResult.Success) {
                        val mappedOptions = supportedLanguagesResult.data.map { supportedLang ->
                            supportedLang.copy(isAdded = myLanguageIds.contains(supportedLang.id))
                        }
                        LinguaQuestResult.Success(mappedOptions)
                    } else {
                        supportedLanguagesResult as LinguaQuestResult.Failure
                    }
                }
                is LinguaQuestResult.Failure -> {
                    myLanguagesResult as LinguaQuestResult.Failure
                }
            }
        }
    }
}
