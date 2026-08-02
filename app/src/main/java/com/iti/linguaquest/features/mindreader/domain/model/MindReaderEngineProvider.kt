package com.iti.linguaquest.features.mindreader.domain.model

import com.iti.linguaquest.features.mindreader.domain.algorithm.InformationGainMindReaderEngine

internal fun MindReaderDataset.engine(): InformationGainMindReaderEngine {
    return InformationGainMindReaderEngine(
        attributes = attributes,
        config = config
    )
}
