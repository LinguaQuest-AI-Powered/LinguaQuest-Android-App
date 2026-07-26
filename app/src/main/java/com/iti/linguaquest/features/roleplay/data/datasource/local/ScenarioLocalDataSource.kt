package com.iti.linguaquest.features.roleplay.data.datasource.local

interface ScenarioLocalDataSource {
    suspend fun getScenariosJson(languageCode: String): String
}
