package br.thiago.habitflowapp.presentation.screens.new_habit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.thiago.habitflowapp.domain.model.WeekDay
import br.thiago.habitflowapp.presentation.ui.theme.onPrimaryLight
import br.thiago.habitflowapp.presentation.ui.theme.onSurfaceVariantLight
import br.thiago.habitflowapp.presentation.ui.theme.outlineLight
import br.thiago.habitflowapp.presentation.ui.theme.outlineVariantLight
import br.thiago.habitflowapp.presentation.ui.theme.primaryContainerLight
import br.thiago.habitflowapp.presentation.ui.theme.surfaceContainerLowestLight
import br.thiago.habitflowapp.presentation.ui.theme.surfaceVariantLight


@Composable
fun SpecificDaysSelector(
    selectedDays: List<Boolean>,
    onDayToggle: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(surfaceContainerLowestLight, RoundedCornerShape(12.dp))
            .border(1.dp, outlineVariantLight, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        // Cabeçalho com contagem de dias selecionados
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = outlineLight)) {
                    append("Dias selecionados: ")
                }
                withStyle(style = SpanStyle(color = primaryContainerLight, fontWeight = FontWeight.Bold)) {
                    append("${selectedDays.count { it }}")
                }
            },
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        // Círculos dos dias
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            WeekDay.allDays.forEachIndexed { i, day ->
                val isSelected = selectedDays.getOrNull(i) ?: false
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) primaryContainerLight else surfaceVariantLight)
                        .clickable { onDayToggle(i) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.shortName,
                        color = if (isSelected) onPrimaryLight else onSurfaceVariantLight,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Texto com os nomes completos dos dias selecionados
        Text(
            text = WeekDay.allDays
                .filterIndexed { i, _ -> selectedDays.getOrNull(i) == true }
                .joinToString(", ") { it.fullName }
                .let { if (it.length > 40) it.take(40) + "..." else it },
            fontSize = 13.sp,
            color = onSurfaceVariantLight,
            lineHeight = 18.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SpecificDaysSelectorPreview() {
    SpecificDaysSelector(
        selectedDays = listOf(true, false, true, false, true, false, true),
        onDayToggle = {}
    )
}
