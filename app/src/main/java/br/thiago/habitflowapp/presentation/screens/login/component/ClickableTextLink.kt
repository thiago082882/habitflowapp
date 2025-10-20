package br.thiago.habitflowapp.presentation.screens.login.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ClickableTextLink(
    normalText: String,
    clickableText: String,
    onClick: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.Center) {
        Text(text = normalText, fontSize = 14.sp, color = Color.Gray)
        Text(
            text = " $clickableText",
            color = Color(0xFF1ABC9C),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable(onClick = onClick)
        )
    }
}
