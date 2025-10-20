package br.thiago.habitflowapp.presentation.screens.home.components

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun  HabitCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    completed: Boolean = false,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = Color(0xFF1C1C1C),
    showCheckIcon: Boolean = true,
    buttonLabel: String? = null,
    onClick: (() -> Unit)? = null,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(borderColor)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = TextStyle(
                        color = textColor,
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = if (completed) TextDecoration.LineThrough else TextDecoration.None
                    )
                )

                subtitle?.let {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = it,
                        fontSize = 13.sp,
                        color = Color.Gray,
                        lineHeight = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))


            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .border(2.dp, borderColor, CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                when {
                    buttonLabel != null -> Text(
                        text = buttonLabel,
                        color = borderColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )

                    showCheckIcon -> Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completo",
                        tint = borderColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomCardPreview() {
    Column(
        modifier = Modifier
            .background(Color(0xFFF8F8F8))
            .padding(vertical = 8.dp)
    ) {
        HabitCard(
            title = "Beber Água (8 copos)",
            subtitle = "Streak: 12 dias",
            completed = true,
            borderColor = Color(0xFF2196F3),
            onClick = {}
        )

        HabitCard(
            title = "Estudar Programação (30 min)",
            subtitle = "Freq: 3 dias por semana (SQS)",
            borderColor = Color(0xFFF1C40F),
            buttonLabel = "OK",
            onClick = {}
        )
    }
}
