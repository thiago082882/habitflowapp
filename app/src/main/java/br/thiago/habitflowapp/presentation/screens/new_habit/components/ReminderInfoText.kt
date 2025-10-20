package br.thiago.habitflowapp.presentation.screens.new_habit.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.padding
import br.thiago.habitflowapp.presentation.ui.theme.onSurfaceVariantLight

@Composable
fun ReminderInfoText(visible: Boolean) {
    AnimatedVisibility(visible = visible) {
        Text(
            text = "O Firebase Cloud Messaging (FCM) será usado para enviar esses lembretes.",
            fontSize = 12.sp,
            color = onSurfaceVariantLight,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
