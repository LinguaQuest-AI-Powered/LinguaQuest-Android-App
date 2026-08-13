package com.iti.linguaquest.core.tutorial.domain

import com.iti.linguaquest.R
import com.iti.linguaquest.core.tutorial.model.TourId
import com.iti.linguaquest.core.tutorial.model.TutorialStep
import com.iti.linguaquest.core.tutorial.model.TutorialTour
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TourRegistry @Inject constructor() {

    fun appTour(): TutorialTour = TutorialTour(
        tourId = TourId.APP_TOUR,
        steps = listOf(
            TutorialStep(
                stepId = "tutorial_top_bar_coins",
                titleRes = R.string.tutorial_top_bar_coins_title,
                descriptionRes = R.string.tutorial_top_bar_coins_desc,
                lingoImageRes = R.drawable.lingo_on_coins
            ),
            TutorialStep(
                stepId = "tutorial_top_bar_xp",
                titleRes = R.string.tutorial_top_bar_xp_title,
                descriptionRes = R.string.tutorial_top_bar_xp_desc,
                lingoImageRes = R.drawable.lingo_change_name
            ),
            TutorialStep(
                stepId = "tutorial_top_bar_notifications",
                titleRes = R.string.tutorial_top_bar_notifications_title,
                descriptionRes = R.string.tutorial_top_bar_notifications_desc,
                lingoImageRes = R.drawable.lingo_help_qw
            ),
            TutorialStep(
                stepId = "tutorial_language_progress",
                titleRes = R.string.tutorial_language_progress_title,
                descriptionRes = R.string.tutorial_language_progress_desc,
                lingoImageRes = R.drawable.lingo_level_language
            ),
            TutorialStep(
                stepId = "tutorial_word_capture",
                titleRes = R.string.tutorial_word_capture_title,
                descriptionRes = R.string.tutorial_word_capture_desc,
                lingoImageRes = R.drawable.lingo_gallery_defualt
            ),
            TutorialStep(
                stepId = "tutorial_world_list",
                titleRes = R.string.tutorial_world_list_title,
                descriptionRes = R.string.tutorial_world_list_desc,
                lingoImageRes = R.drawable.lingo_map_1
            ),
            TutorialStep(
                stepId = "tutorial_language_button",
                titleRes = R.string.tutorial_language_button_title,
                descriptionRes = R.string.tutorial_language_button_desc,
                lingoImageRes = R.drawable.lingo_onboarding_6
            ),
            TutorialStep(
                stepId = "tutorial_daily_mission",
                titleRes = R.string.tutorial_daily_mission_title,
                descriptionRes = R.string.tutorial_daily_mission_desc,
                lingoImageRes = R.drawable.lingo_reward
            )
        )
    )

    fun galleryTour(): TutorialTour = TutorialTour(
        tourId = TourId.GALLERY_TOUR,
        steps = listOf(
            TutorialStep(
                stepId = "gallery_tab_game_captures",
                titleRes = R.string.tutorial_gallery_captures_title,
                descriptionRes = R.string.tutorial_gallery_captures_desc,
                lingoImageRes = R.drawable.lingo_gellary_icon
            ),
            TutorialStep(
                stepId = "gallery_tab_my_journal",
                titleRes = R.string.tutorial_gallery_journal_title,
                descriptionRes = R.string.tutorial_gallery_journal_desc,
                lingoImageRes = R.drawable.lingo_writing
            )
        )
    )

    fun lingosTour(): TutorialTour = TutorialTour(
        tourId = TourId.LINGOS_TOUR,
        steps = listOf(
            TutorialStep(
                stepId = "lingos_card_voice",
                titleRes = R.string.tutorial_lingos_voice_title,
                descriptionRes = R.string.tutorial_lingos_voice_desc,
                lingoImageRes = R.drawable.lingo_initial_state_voice
            ),
            TutorialStep(
                stepId = "lingos_card_roleplay",
                titleRes = R.string.tutorial_lingos_roleplay_title,
                descriptionRes = R.string.tutorial_lingos_roleplay_desc,
                lingoImageRes = R.drawable.lingo_hello_review
            ),
            TutorialStep(
                stepId = "lingos_card_mindreader",
                titleRes = R.string.tutorial_lingos_mindreader_title,
                descriptionRes = R.string.tutorial_lingos_mindreader_desc,
                lingoImageRes = R.drawable.lingo_mind_thinking
            )
        )
    )

    fun profileTour(
        hasAchievements: Boolean = true,
        hasLeaderboard: Boolean = true
    ): TutorialTour {
        val steps = mutableListOf(
            TutorialStep(
                stepId = "profile_header_target",
                titleRes = R.string.tutorial_profile_header_title,
                descriptionRes = R.string.tutorial_profile_header_desc,
                lingoImageRes = R.drawable.lingo_change_name
            ),
            TutorialStep(
                stepId = "profile_stats_target",
                titleRes = R.string.tutorial_profile_stats_title,
                descriptionRes = R.string.tutorial_profile_stats_desc,
                lingoImageRes = R.drawable.lingo_acheviment
            ),
            TutorialStep(
                stepId = "profile_settings_target",
                titleRes = R.string.tutorial_profile_settings_title,
                descriptionRes = R.string.tutorial_profile_settings_desc,
                lingoImageRes = R.drawable.lingo_stting
            )
        )

        if (hasAchievements) {
            steps.add(
                TutorialStep(
                    stepId = "profile_achievements_target",
                    titleRes = R.string.tutorial_profile_achievements_title,
                    descriptionRes = R.string.tutorial_profile_achievements_desc,
                    lingoImageRes = R.drawable.lingo_acheviment
                )
            )
        }

        if (hasLeaderboard) {
            steps.add(
                TutorialStep(
                    stepId = "profile_leaderboard_target",
                    titleRes = R.string.tutorial_profile_leaderboard_title,
                    descriptionRes = R.string.tutorial_profile_leaderboard_desc,
                    lingoImageRes = R.drawable.lingo_leaderboard
                )
            )
        }

        return TutorialTour(tourId = TourId.PROFILE_TOUR, steps = steps)
    }
}
