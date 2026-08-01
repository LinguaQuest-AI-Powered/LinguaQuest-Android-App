package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderCategory
import java.util.Locale
import javax.inject.Inject

class GetMindReaderCategoriesUseCase @Inject constructor(
    private val getMindReaderDatasetUseCase: GetMindReaderDatasetUseCase
) {
    suspend operator fun invoke(): List<MindReaderCategory> {
        val dataset = getMindReaderDatasetUseCase()
        
        // Extract unique world keys and map to proper display names
        return dataset.entities
            .map { it.worldKey }
            .distinct()
            .map { key ->
                val displayName = key
                    .replace("_", " ")
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                
                MindReaderCategory(
                    id = key,
                    displayName = displayName
                )
            }
            .sortedBy { it.displayName }
    }
}
