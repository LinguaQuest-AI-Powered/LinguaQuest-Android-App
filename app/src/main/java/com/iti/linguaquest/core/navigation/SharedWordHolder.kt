package com.iti.linguaquest.core.navigation

import com.iti.linguaquest.core.database.word.WordEntity


object SharedWordHolder {
    var pendingWord: WordEntity? = null
}
