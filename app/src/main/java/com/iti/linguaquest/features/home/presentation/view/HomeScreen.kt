package com.iti.linguaquest.features.home.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.features.home.presentation.view.components.ContinueLessonCard
import com.iti.linguaquest.features.home.presentation.view.components.ExploreWorldsSection
import com.iti.linguaquest.features.home.presentation.view.components.LanguageProgressCard
import com.iti.linguaquest.features.home.presentation.view.components.LessonPreview
import com.iti.linguaquest.features.home.presentation.view.components.WorldDifficulty
import com.iti.linguaquest.features.home.presentation.view.components.WorldItem

@Composable
fun HomeScreen(
    onNavigateToDetails: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val worlds = remember {
        listOf(
            WorldItem(
                id = "kitchen",
                title = "Kitchen World",
                imageRes = R.drawable.kitchen_icon,
                difficulty = WorldDifficulty.EASY,
                progress = 0.4f,
                isCompleted = true
            ),
            WorldItem(
                id = "city",
                title = "City World",
                imageRes = R.drawable.kitchen_icon,
                difficulty = WorldDifficulty.MEDIUM,
                progress = 0.1f,
                isCompleted = true
            ),
            WorldItem(
                id = "animals",
                title = "Animals World",
                imageRes = R.drawable.kitchen_icon,
                difficulty = WorldDifficulty.HARD,
                progress = 0.9f
            )
        )
    }

    val lesson = remember {
        LessonPreview(
            lessonId = 1,
            word = "Apple",
            partOfSpeech = "Noun",
            translation = "La Pomme",
            iconRes = R.drawable.apple_icon
        )
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.lingo_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        HomeContent(
            worlds = worlds,
            lesson = lesson,
            onNavigateToDetails = onNavigateToDetails
        )
    }
}

@Composable
fun HomeContent(
    worlds: List<WorldItem>,
    lesson: LessonPreview,
    onNavigateToDetails: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        LanguageProgressCard(
            languageName = "Spanish",
            level = 12,
            streakDays = 7,
            progress = 0.55f,
            flagRes = R.drawable.flag_spain
        )

        Spacer(modifier = Modifier.height(20.dp))

        ExploreWorldsSection(
            worlds = worlds,
            onSeeMoreClick = { },
            onWorldClick = { },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        ContinueLessonCard(
            lesson = lesson,
            onContinueClick = {
                onNavigateToDetails(lesson.lessonId)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}