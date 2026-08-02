package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderCategory
import java.util.Locale
import javax.inject.Inject

class GetMindReaderCategoriesUseCase @Inject constructor(
    private val getMindReaderDatasetUseCase: GetMindReaderDatasetUseCase
) {
    suspend operator fun invoke(): List<MindReaderCategory> {
        val dataset = getMindReaderDatasetUseCase()

        val entitiesByWorld = dataset.entities.groupBy { it.worldKey }

        return entitiesByWorld.map { (key, entities) ->
            val displayName = key
                .replace("_", " ")
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

            MindReaderCategory(
                id = key,
                displayName = displayName,
                emoji = entities.firstOrNull()?.emoji.orEmpty()
            )
        }.sortedBy { it.displayName }
    }
}
