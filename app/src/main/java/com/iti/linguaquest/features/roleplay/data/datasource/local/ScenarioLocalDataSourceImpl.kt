package com.iti.linguaquest.features.roleplay.data.datasource.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import javax.inject.Inject

class ScenarioLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ScenarioLocalDataSource {

    override suspend fun getScenariosJson(languageCode: String): String = withContext(Dispatchers.IO) {
        try {
            context.assets.open("scenarios_$languageCode.json").bufferedReader().use { it.readText() }
        } catch (e: FileNotFoundException) {
            context.assets.open("scenarios_en.json").bufferedReader().use { it.readText() }
        }
    }
}
