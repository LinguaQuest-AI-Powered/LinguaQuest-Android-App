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
import androidx.compose.ui.window.Dialog
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ImageWrapper

@Composable
fun EnableLockScreenDialog(
    coinCost: Int = 50,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Dialog(onDismissRequest = onCancel) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                 ImageWrapper(
                    model = R.drawable.lingo_reward,
                    contentDescription = null,
                    modifier = Modifier
                        .size(160.dp)
                        .padding(bottom = 16.dp)
                )

                 Text(
                    text = stringResource(id = R.string.enable_lock_screen_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = LinguaQuestTheme.colors.titleAndCationsColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                 Text(
                    text = stringResource(id = R.string.enable_lock_screen_subtitle, coinCost),
                    style = MaterialTheme.typography.bodyMedium,
                    color = LinguaQuestTheme.colors.titleAndCationsColor.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                 Surface(
                    shape = RoundedCornerShape(99.dp),
                    color = AppColors.OrangeActive.copy(alpha = 0.15f),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_coin),
                            contentDescription = stringResource(id = R.string.coins_description),
                            tint = AppColors.OrangeActive,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = coinCost.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.OrangeActive
                        )
                    }
                }

                 Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.OrangeActive),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_coin),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.confirm_button),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                 OutlinedButton(
                    onClick = onCancel,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = LinguaQuestTheme.colors.titleAndCationsColor),
                    border = androidx.compose.foundation.BorderStroke(2.dp, LinguaQuestTheme.colors.textFieldBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel_button),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}