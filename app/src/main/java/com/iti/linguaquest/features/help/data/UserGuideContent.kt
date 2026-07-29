package com.iti.linguaquest.features.help.data

import androidx.annotation.StringRes
import com.iti.linguaquest.R

data class UserGuideSection(
    val emoji: String,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int
)

object UserGuideDefaults {
    val sections: List<UserGuideSection> = listOf(
        UserGuideSection(
            emoji = "\uD83D\uDCF8",
            titleRes = R.string.user_guide_item_1_title,
            descriptionRes = R.string.user_guide_item_1_desc
        ),
        UserGuideSection(
            emoji = "\uD83E\uDD16",
            titleRes = R.string.user_guide_item_2_title,
            descriptionRes = R.string.user_guide_item_2_desc
        ),
        UserGuideSection(
            emoji = "\uD83C\uDFA4",
            titleRes = R.string.user_guide_item_3_title,
            descriptionRes = R.string.user_guide_item_3_desc
        ),
        UserGuideSection(
            emoji = "\uD83D\uDDE3\uFE0F",
            titleRes = R.string.user_guide_item_4_title,
            descriptionRes = R.string.user_guide_item_4_desc
        ),
        UserGuideSection(
            emoji = "\uD83D\uDCAC",
            titleRes = R.string.user_guide_item_5_title,
            descriptionRes = R.string.user_guide_item_5_desc
        ),
        UserGuideSection(
            emoji = "\uD83D\uDD12",
            titleRes = R.string.user_guide_item_6_title,
            descriptionRes = R.string.user_guide_item_6_desc
        ),
        UserGuideSection(
            emoji = "\uD83D\uDCDA",
            titleRes = R.string.user_guide_item_7_title,
            descriptionRes = R.string.user_guide_item_7_desc
        ),
        UserGuideSection(
            emoji = "\uD83D\uDDBC\uFE0F",
            titleRes = R.string.user_guide_item_8_title,
            descriptionRes = R.string.user_guide_item_8_desc
        ),
        UserGuideSection(
            emoji = "\uD83C\uDFC6",
            titleRes = R.string.user_guide_item_9_title,
            descriptionRes = R.string.user_guide_item_9_desc
        ),
        UserGuideSection(
            emoji = "\uD83C\uDF0D",
            titleRes = R.string.user_guide_item_10_title,
            descriptionRes = R.string.user_guide_item_10_desc
        )
    )
}
