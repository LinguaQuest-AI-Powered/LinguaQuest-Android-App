package com.iti.linguaquest.features.mindreader.data.datasource.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iti.linguaquest.R
import com.iti.linguaquest.features.mindreader.data.dto.CategoryDto
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MindReaderLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : MindReaderLocalDataSource {

    override suspend fun getCategories(): List<CategoryDto> = withContext(Dispatchers.IO) {
        val jsonString = readRawResource(R.raw.mindreader_categories)
        val type = object : TypeToken<List<CategoryDto>>() {}.type
        gson.fromJson(jsonString, type) ?: emptyList()
    }

    override suspend fun getGameConfig(): MindReaderGameConfig = MindReaderGameConfig()

    private fun readRawResource(rawId: Int): String {
        return context.resources.openRawResource(rawId).bufferedReader().use { it.readText() }
    }
}
