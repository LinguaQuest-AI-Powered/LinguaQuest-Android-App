package com.iti.linguaquest.core.navigation

import com.iti.linguaquest.core.database.word.WordEntity

/**
 * A simple in-memory holder to pass a WordEntity between composables
 * during navigation. This avoids serializing the entity into the nav route.
 * Cleared after the ReviewScreen consumes the word.
 */
object SharedWordHolder {
    var pendingWord: WordEntity? = null
}
