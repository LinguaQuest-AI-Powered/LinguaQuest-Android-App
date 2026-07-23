package com.iti.linguaquest.core.appicon.domain

 import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

 @Singleton
class AppIconService @Inject constructor(
    private val stateRepository: AppIconStateRepository,
    private val ruleEngine: AppIconRuleEngine,
    private val manager:  AppIconController
) {
    suspend fun refresh(source: AppIconRefreshSource = AppIconRefreshSource.USER) {
        withContext(Dispatchers.IO) {
            if (source == AppIconRefreshSource.USER) {
                stateRepository.markUserInteraction()
            }

            val iconType = ruleEngine.evaluate()
            manager.switchTo(iconType)
        }
    }
}
