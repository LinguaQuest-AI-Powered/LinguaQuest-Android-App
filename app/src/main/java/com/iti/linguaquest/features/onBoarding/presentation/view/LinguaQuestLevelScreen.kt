package com.iti.linguaquest.features.onBoarding.presentation.view


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.IconPosition
import com.iti.linguaquest.features.onBoarding.presentation.viewModel.levelViewModel.LevelViewModel
import com.iti.linguaquest.features.onBoarding.presentation.contract.levelContract.LevelEffect
import com.iti.linguaquest.features.onBoarding.presentation.contract.levelContract.LevelIntent
import com.iti.linguaquest.features.onBoarding.presentation.contract.levelContract.LevelState
import com.iti.linguaquest.features.onBoarding.presentation.contract.levelContract.ProficiencyLevel
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.tooling.preview.Preview
import com.iti.linguaquest.features.onBoarding.presentation.components.LevelCard
import com.iti.linguaquest.core.theme.LinguaQuestTheme


@Composable
fun LevelScreen(
    onContinue: () -> Unit,
    viewModel: LevelViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            if (effect is LevelEffect.NavigateToHome) {
                onContinue()
            }
        }
    }

    LevelScreenContent(
        state = state,
        onLevelSelected = {
            viewModel.onIntent(LevelIntent.SelectLevel(it))
        },
        onContinueClick = {
            viewModel.onIntent(LevelIntent.ContinueClicked)
        }
    )
}

@Composable
fun LevelScreenContent(
    state: LevelState,
    onLevelSelected: (ProficiencyLevel) -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize().verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp,vertical = 32.dp)
    ) {
        Spacer(Modifier.height(20.dp))

        Image(
            painter = painterResource(R.drawable.lingo_level_language),
            contentDescription = null,
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.What_is_your_level),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(12.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = LinguaQuestTheme.colors.whiteColor,
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(
                width =  2.dp,
                color =  MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                text =stringResource(R.string.choose_where_your_journey_begins),
                style = MaterialTheme.typography.titleSmall,
                color =  LinguaQuestTheme.colors.titleAndCationsColor,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        ProficiencyLevel.entries.forEach { level ->
            LevelCard(
                level = level,
                isSelected = state.selectedLevel == level,
                onClick = {
                    onLevelSelected(level)
                }
            )

            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.weight(1f))

        AppButton(
            text = stringResource(R.string.continue_button),
            onClick = onContinueClick,
            enabled = state.isContinueEnabled,
            iconPosition = IconPosition.END,
            icon = painterResource(R.drawable.arrow_right),
            modifier = Modifier.padding(vertical = 24.dp)
        )
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LevelScreenPreview() {
    LinguaQuestTheme {
        LevelScreenContent(
            state = LevelState(
                selectedLevel = ProficiencyLevel.ADVANCED,
                isContinueEnabled = true
            ),
            onLevelSelected = {},
            onContinueClick = {}
        )
    }
}