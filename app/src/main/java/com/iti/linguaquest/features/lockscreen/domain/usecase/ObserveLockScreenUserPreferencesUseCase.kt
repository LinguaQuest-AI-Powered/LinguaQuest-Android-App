package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class LockScreenUserPreferences(
    val appLanguage: String?,
    val targetLanguageName: String?,
    val proficiencyLevel: String?
)

class ObserveLockScreenUserPreferencesUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<LockScreenUserPreferences> {
        return combine(
            repository.appLanguage,
            repository.targetLanguageName,
            repository.proficiencyLevel
        ) { appLanguage, targetLanguageName, proficiencyLevel ->
            LockScreenUserPreferences(
                appLanguage = appLanguage,
                targetLanguageName = targetLanguageName,
                proficiencyLevel = proficiencyLevel
            )
        }
    }
}
