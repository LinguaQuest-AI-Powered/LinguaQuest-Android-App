package com.iti.linguaquest.features.game.presentation.level.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HintsBottomSheet(
    coinCount: Int,
    onDismiss: () -> Unit,
    onRevealFirstLetter: () -> Unit,
    onShowCategoryClue: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        dragHandle = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .background(Color(0xFFFDF7F2), RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = rememberVectorPainter(Icons.Default.MonetizationOn),
                        contentDescription = stringResource(id = R.string.coins),
                        tint = Color(0xFFE5A822),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "%,d".format(coinCount),
                        color = AppColors.BrownText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFFE8F1F8), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            painter = rememberVectorPainter(Icons.Default.Close),
                            contentDescription = stringResource(id = R.string.close),
                            tint = AppColors.PrimaryColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.lingo_hint),
                contentDescription = stringResource(id = R.string.thinking_mascot_content_desc),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.need_a_hint),
                color = AppColors.BrownText,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(24.dp))

            HintItem(
                icon = Icons.Default.TextFields,
                title = stringResource(id = R.string.reveal_first_letter),
                cost = 25,
                onClick = onRevealFirstLetter
            )

            Spacer(modifier = Modifier.height(12.dp))

            HintItem(
                icon = Icons.Default.Info,
                title = stringResource(id = R.string.show_category_clue),
                cost = 50,
                onClick = onShowCategoryClue
            )
        }
    }
}

@Composable
private fun HintItem(
    icon: ImageVector,
    title: String,
    cost: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = rememberVectorPainter(icon),
            contentDescription = null,
            tint = Color(0xFF5CB85C),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = AppColors.BrownText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = rememberVectorPainter(Icons.Default.MonetizationOn),
                    contentDescription = stringResource(id = R.string.coins),
                    tint = Color(0xFFE5A822),
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = cost.toString(),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .background(Color(0xFFFF9800), RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.use_hint),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}
