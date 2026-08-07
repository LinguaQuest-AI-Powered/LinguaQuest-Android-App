package com.iti.linguaquest.features.setting.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.AppOutlinedButton
import com.iti.linguaquest.core.sharedComponents.IconPosition
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ImageWrapper

@Composable
fun EnableLockScreenDialog(
    coinCost: Int = 50,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(top = 100.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 100.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.enable_lock_screen_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = LinguaQuestTheme.colors.titleAndCationsColor,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(R.string.enable_lock_screen_desc, coinCost),
                        style = MaterialTheme.typography.bodyMedium,
                        color = LinguaQuestTheme.colors.titleAndCationsColor.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Surface(
                        shape = RoundedCornerShape(99.dp),
                        color = AppColors.OrangeActive.copy(alpha = 0.1f),
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_coin),
                                contentDescription = null,
                                tint = AppColors.OrangeActive,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = coinCost.toString(),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = AppColors.OrangeActive
                            )
                        }
                    }

                    AppButton3D(
                        text = stringResource(R.string.btn_confirm),
                        onClick = onConfirm,
                        icon = painterResource(id = R.drawable.ic_coin),
                        iconPosition = IconPosition.START,
                        backgroundColorOverride = AppColors.OrangeActive,
                        ledgeColorOverride = AppColors.OrangeActive.copy(alpha = 0.7f),
                        contentColorOverride = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AppOutlinedButton(
                        text = stringResource(R.string.btn_cancel),
                        onClick = onCancel,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            ImageWrapper(
                model = R.drawable.lingo_reward,
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = (-110).dp)
            )
        }
    }
}