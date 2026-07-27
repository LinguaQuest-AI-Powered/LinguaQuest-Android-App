package com.iti.linguaquest.features.all_worlds.presentation.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.features.home.presentation.view.components.WorldCard
import com.iti.linguaquest.features.home.presentation.view.components.WorldItem

import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun AllWorldsGrid(
    worlds: List<WorldItem>,
    onWorldClick: (WorldItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(worlds.size, key = { index -> "${worlds[index].id}_$index" }) { index ->
            val world = worlds[index]
            WorldCard(
                world = world,
                onClick = { onWorldClick(world) },
                imageShape = RoundedCornerShape(percent = 50),
                modifier = Modifier.width(240.dp).height(220.dp)
            )
        }
    }
}
