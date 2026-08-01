package com.iti.linguaquest.features.help.data

import com.iti.linguaquest.R
import com.iti.linguaquest.features.help.presentation.help.contract.FaqItem

object HelpFaqProvider {

    fun defaultFaqs(): List<FaqItem> = listOf(
        FaqItem(
            id = "earn_coins",
            questionRes = R.string.faq_earn_coins_question,
            answerRes = R.string.faq_earn_coins_answer
        ),
        FaqItem(
            id = "lock_screen_vocabulary",
            questionRes = R.string.faq_lock_screen_question,
            answerRes = R.string.faq_lock_screen_answer
        ),
        FaqItem(
            id = "change_target_language",
            questionRes = R.string.faq_change_language_question,
            answerRes = R.string.faq_change_language_answer
        ),
        FaqItem(
            id = "maintain_streak",
            questionRes = R.string.faq_maintain_streak_question,
            answerRes = R.string.faq_maintain_streak_answer
        )
    )
}