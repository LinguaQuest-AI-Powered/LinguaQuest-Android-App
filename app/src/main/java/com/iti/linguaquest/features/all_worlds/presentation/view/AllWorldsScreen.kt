package com.iti.linguaquest.features.all_worlds.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.core.theme.AppColors.BrownText
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsEffect
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsIntent
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsState
import com.iti.linguaquest.features.all_worlds.presentation.view.components.AllWorldsFilterRow
import com.iti.linguaquest.features.all_worlds.presentation.viewModel.AllWorldsViewModel
import com.iti.linguaquest.features.home.presentation.view.components.WorldCard
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AllWorldsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToWorldDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AllWorldsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is AllWorldsEffect.NavigateBack -> onNavigateBack()
                is AllWorldsEffect.NavigateToWorldDetails -> onNavigateToWorldDetails(effect.worldId)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F5F0))
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            AllWorldsContent(
                state = state,
                onIntent = viewModel::onIntent
            )
        }
    }
}

@Composable
fun AllWorldsContent(
    state: AllWorldsState,
    onIntent: (AllWorldsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE6F3F7))
                    .clickable { onIntent(AllWorldsIntent.OnBackClicked) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFFF0A020)
                )
            }

            Text(
                text = "LinguaQuest",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = BrownText
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 48.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title and Subtitle
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "All Worlds",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = LinguaQuestTheme.colors.blackColor,
                    fontSize = 32.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Choose your next adventure",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = LinguaQuestTheme.colors.iconsColor
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Filters
        AllWorldsFilterRow(
            selectedFilter = state.selectedFilter,
            onFilterSelected = { onIntent(AllWorldsIntent.OnFilterSelected(it)) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Worlds Count
        Text(
            text = "${state.filteredWorlds.size} worlds",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = LinguaQuestTheme.colors.iconsColor
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Worlds Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.filteredWorlds, key = { it.id }) { world ->
                WorldCard(
                    world = world,
                    onClick = { onIntent(AllWorldsIntent.OnWorldClicked(world)) },
                    modifier = Modifier.fillMaxWidth() // Fills the grid cell width
                )
            }
        }
    }
}
