package com.iti.linguaquest.features.help.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.iti.linguaquest.R

data class FaqItem(
    @StringRes val questionRes: Int,
    @StringRes val answerRes: Int,
    @DrawableRes val questionImageRes: Int,
    @DrawableRes val answerImageRes: Int
)

object FaqsDefaults {
    val items: List<FaqItem> = listOf(
        FaqItem(
            questionRes = R.string.faq_item_1_question,
            answerRes = R.string.faq_item_1_answer,
            questionImageRes = R.drawable.lingo_help_qw,
            answerImageRes = R.drawable.lingo_help_review
        ),
        FaqItem(
            questionRes = R.string.faq_item_2_question,
            answerRes = R.string.faq_item_2_answer,
            questionImageRes = R.drawable.lingo_help_qw,
            answerImageRes = R.drawable.lingo_help_review
        ),
        FaqItem(
            questionRes = R.string.faq_item_3_question,
            answerRes = R.string.faq_item_3_answer,
            questionImageRes = R.drawable.lingo_help_qw,
            answerImageRes = R.drawable.lingo_help_review
        ),
        FaqItem(
            questionRes = R.string.faq_item_4_question,
            answerRes = R.string.faq_item_4_answer,
            questionImageRes = R.drawable.lingo_help_qw,
            answerImageRes = R.drawable.lingo_help_review
        ),
        FaqItem(
            questionRes = R.string.faq_item_5_question,
            answerRes = R.string.faq_item_5_answer,
            questionImageRes = R.drawable.lingo_help_qw,
            answerImageRes = R.drawable.lingo_help_review
        )
    )
}
