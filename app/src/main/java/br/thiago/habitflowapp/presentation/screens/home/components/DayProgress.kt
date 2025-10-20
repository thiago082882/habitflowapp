package br.thiago.habitflowapp.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProgressCard(
    modifier: Modifier = Modifier,
    title: String = "Progresso",
    progressPercent: Int,
    completed: Int,
    total: Int,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
    accentColor: Color = MaterialTheme.colorScheme.primary,
    circleColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    color = textColor.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$progressPercent% Concluído",
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(circleColor)
                    .border(2.dp, accentColor.copy(alpha = 0.9f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$completed/$total",
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewProgressCard() {
    ProgressCard(
        title = "Progresso do Dia",
        progressPercent = 67,
        completed = 2,
        total = 3
    )
}
