package br.thiago.habitflowapp.presentation.screens.new_habit.components


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.thiago.habitflowapp.presentation.ui.theme.onBackgroundLight
import br.thiago.habitflowapp.presentation.ui.theme.onPrimaryLight
import br.thiago.habitflowapp.presentation.ui.theme.outlineVariantLight
import br.thiago.habitflowapp.presentation.ui.theme.primaryContainerLight
import br.thiago.habitflowapp.presentation.ui.theme.surfaceVariantLight

@Composable
fun ReminderSwitchCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = surfaceVariantLight),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Ativar Lembrete Diário",
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
}
