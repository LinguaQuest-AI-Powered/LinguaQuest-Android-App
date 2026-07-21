package com.iti.linguaquest.features.editprofile.presentation.component


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
  fun ProfileInputCard(
                label: String,
                value: String,
                onValueChange: (String) -> Unit,
                modifier: Modifier = Modifier,
                placeholder: String? = null,
                singleLine: Boolean = true,
                minLines: Int = 1,
                trailingIcon: (@Composable () -> Unit)? = null
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(LinguaQuestTheme.colors.fieldCardBackground)
                        .padding(16.dp)
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.IconsColor
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(LinguaQuestTheme.colors.textFieldFill)
                            .border(
                                BorderStroke(1.dp, LinguaQuestTheme.colors.textFieldBorder),
                                RoundedCornerShape(14.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 14.dp),
                        verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Bottom
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (value.isEmpty() && placeholder != null) {
                                Text(
                                    text = placeholder,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = LinguaQuestTheme.colors.textFieldPlaceholder
                                )
                            }

                            BasicTextField(
                                value = value,
                                onValueChange = onValueChange,
                                singleLine = singleLine,
                                minLines = minLines,
                                textStyle = MaterialTheme.typography.titleMedium.copy(
                                    color = AppColors.BrownText
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        if (trailingIcon != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                            trailingIcon()
                        }
                    }
                }
            }
