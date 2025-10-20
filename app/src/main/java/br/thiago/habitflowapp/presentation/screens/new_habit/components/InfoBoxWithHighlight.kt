package br.thiago.habitflowapp.presentation.screens.new_habit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.thiago.habitflowapp.presentation.ui.theme.outlineLight
import br.thiago.habitflowapp.presentation.ui.theme.outlineVariantLight
import br.thiago.habitflowapp.presentation.ui.theme.primaryContainerLight
import br.thiago.habitflowapp.presentation.ui.theme.surfaceContainerLowestLight

@Composable
fun InfoBoxWithHighlight(text: String, highlight: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(surfaceContainerLowestLight, RoundedCornerShape(8.dp))
            .border(1.dp, outlineVariantLight, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            buildAnnotatedString {
                val parts = text.split(highlight)
                append(parts[0])
                withStyle(style = SpanStyle(color = primaryContainerLight , fontWeight = FontWeight.Bold)) {
                    append(highlight)
                }
                if (parts.size > 1) append(parts[1])
            },
            fontSize = 14.sp,
            style = MaterialTheme.typography.bodyMedium,
            color = outlineLight
        )
    }
}
