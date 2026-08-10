package com.iti.linguaquest.features.all_worlds.presentation.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsIntent
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsState


import com.iti.linguaquest.core.sharedComponents.animations.LingoEntranceAnimations
import com.iti.linguaquest.core.sharedComponents.animations.StaggeredAnimatedItem
import com.iti.linguaquest.core.sharedComponents.animations.rememberStaggeredAnimationState

@Composable
fun AllWorldsContent(
    state: AllWorldsState,
    onIntent: (AllWorldsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val animationState = rememberStaggeredAnimationState(count = 4 + state.filteredWorlds.size)
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        StaggeredAnimatedItem(
            index = 0,
            state = animationState,
            enter = LingoEntranceAnimations.popUpVertically(offset = -60)
        ) {
            LinguaQuestScreenTopBar(
                title = stringResource(R.string.app_name),
                onBackClicked = { onIntent(AllWorldsIntent.OnBackClicked) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        StaggeredAnimatedItem(
            index = 1,
            state = animationState,
            enter = LingoEntranceAnimations.popUpVertically()
        ) {
            AllWorldsHeader(
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        StaggeredAnimatedItem(
            index = 2,
            state = animationState,
            enter = LingoEntranceAnimations.popUpVertically()
        ) {
            AllWorldsFilterRow(
                selectedFilter = state.selectedFilter,
                onFilterSelected = { onIntent(AllWorldsIntent.OnFilterSelected(it)) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        StaggeredAnimatedItem(
            index = 3,
            state = animationState,
            enter = LingoEntranceAnimations.popUpVertically()
        ) {
            Text(
                text = stringResource(R.string.worlds_count_format, state.filteredWorlds.size),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = LinguaQuestTheme.colors.iconsColor
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AllWorldsGrid(
            worlds = state.filteredWorlds,
            animationState = animationState,
            startIndex = 4,
            onWorldClick = { onIntent(AllWorldsIntent.OnWorldClicked(it)) }
        )
    }
}
