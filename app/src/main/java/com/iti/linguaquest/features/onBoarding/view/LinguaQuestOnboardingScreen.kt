package com.iti.linguaquest.features.onBoarding.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme


@Composable
fun OnboardingScreen(
    onGetStartedClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(id = R.drawable.lingo_on_boarding_parrot),
            contentDescription = "LinguaQuest mascot",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f)
                .aspectRatio(1f),
            contentScale = ContentScale.Fit
        )


        Spacer(modifier = Modifier.weight(0.14f))

        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.onboarding_title_p1))
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.tertiary)) {
                    append(" ")
                }
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.tertiary)) {
                    append(stringResource(R.string.onboarding_title_p2))
                }
            },

            style= AppTextStyles.LessonTitle.copy(textAlign = TextAlign.Center,lineHeight = 30.sp,fontSize = 24.sp,),

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(0.2f))

        AppButton(
            text = stringResource(R.string.get_started),
            onClick = onGetStartedClick,
            variant = ButtonVariant.PRIMARY
        )

        Spacer(modifier = Modifier.height(12.dp))

        AppButton(
            text = stringResource(R.string.already_have_an_account),
            onClick = onLoginClick,
            variant = ButtonVariant.SECONDARY
        )

        Spacer(modifier = Modifier.weight(0.16f))
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun OnboardingScreenPreview() {
    LinguaQuestTheme {
        OnboardingScreen()
    }
}