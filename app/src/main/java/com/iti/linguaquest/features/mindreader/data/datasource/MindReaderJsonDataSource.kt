package com.iti.linguaquest.features.mindreader.data.datasource

import android.content.Context
import com.google.gson.Gson
import com.iti.linguaquest.R
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAttribute
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderDataset
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MindReaderJsonDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : MindReaderDataSource {

    private val gson = Gson()

    override suspend fun loadDataset(): MindReaderDataset = withContext(Dispatchers.IO) {
        val attributes = readAttributes()
        val entities = readEntities()
        val config = readConfig()

        require(attributes.isNotEmpty()) { "Mind Reader attributes JSON is empty." }
        require(entities.isNotEmpty()) { "Mind Reader entities JSON is empty." }

        MindReaderDataset(
            attributes = attributes,
            entities = entities,
            config = config
        )
    }

    private fun readAttributes(): List<MindReaderAttribute> {
        val wrapper = gson.fromJson(
            readRawResource(R.raw.mindreader_attributes),
            MindReaderAttributesWrapper::class.java
        )
        return wrapper?.attributes.orEmpty()
    }

    private fun readEntities(): List<MindReaderEntity> {
        val wrapper = gson.fromJson(
            readRawResource(R.raw.mindreader_entities),
            MindReaderEntitiesWrapper::class.java
        )
        return wrapper?.entities.orEmpty()
    }

    private fun readConfig(): MindReaderGameConfig {
        return gson.fromJson(
            readRawResource(R.raw.game_config),
            MindReaderGameConfig::class.java
        )
    }

    private fun readRawResource(rawId: Int): String {
        return context.resources.openRawResource(rawId).bufferedReader().use { it.readText() }
    }
}

private data class MindReaderAttributesWrapper(
    val attributes: List<MindReaderAttribute> = emptyList()
)

private data class MindReaderEntitiesWrapper(
    val entities: List<MindReaderEntity> = emptyList()
)
