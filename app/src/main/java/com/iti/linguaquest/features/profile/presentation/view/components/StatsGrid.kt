package com.iti.linguaquest.features.profile.presentation.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.iti.linguaquest.R
import com.iti.linguaquest.features.profile.presentation.model.ProfileState

@Composable
 fun StatsGrid(state: ProfileState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(
                painter = painterResource(R.drawable.ic_coin),
                value = state.coins.toString(),
                label = stringResource(R.string.coins_label),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                painter = painterResource(R.drawable.ic_xp),
                value = state.totalXp.toString(),
                label = stringResource(R.string.total_xp_label),
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(
                painter = painterResource(R.drawable.ic_streak),
                value = "${state.streakDays} ${stringResource(R.string.days)}",
                label = stringResource(R.string.streak_label),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                painter =  painterResource(R.drawable.ic_profile_world),
                value = state.worldsCount.toString(),
                label = stringResource(R.string.worlds_label),
                modifier = Modifier.weight(1f)
            )
        }
    }
}