package br.thiago.habitflowapp.presentation.screens.new_habit.components

import androidx.compose.foundation.shape.RoundedCornerShape



import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.thiago.habitflowapp.presentation.ui.theme.onBackgroundLight
import br.thiago.habitflowapp.presentation.ui.theme.onPrimaryLight
import br.thiago.habitflowapp.presentation.ui.theme.onSurfaceVariantLight
import br.thiago.habitflowapp.presentation.ui.theme.outlineVariantLight
import br.thiago.habitflowapp.presentation.ui.theme.primaryContainerLight
import br.thiago.habitflowapp.presentation.ui.theme.surfaceVariantLight

@Composable
fun NotificationCard(
    title: String = "Ativar Lembrete Diário",
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Ativar Notificações",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = onBackgroundLight
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = surfaceVariantLight),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    color = onBackgroundLight,
                    modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = onPrimaryLight,
                        checkedTrackColor = primaryContainerLight,
                        uncheckedThumbColor = outlineVariantLight,
                        uncheckedTrackColor = surfaceVariantLight
                    )
                )
            }
        }

        AnimatedVisibility(visible = checked) {
            Text(
                text = "O Firebase Cloud Messaging (FCM) será usado para enviar esses lembretes.",
                fontSize = 12.sp,
                color = onSurfaceVariantLight,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun NotificationCardPreview() {
    NotificationCard(
        checked = true,
        onCheckedChange = {}

    )
}