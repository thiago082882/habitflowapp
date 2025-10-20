package br.thiago.habitflowapp.presentation.screens.new_habit.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.thiago.habitflowapp.domain.model.FrequencyType
import br.thiago.habitflowapp.presentation.ui.theme.onBackgroundLight
import br.thiago.habitflowapp.presentation.ui.theme.onPrimaryLight
import br.thiago.habitflowapp.presentation.ui.theme.outlineLight
import br.thiago.habitflowapp.presentation.ui.theme.primaryContainerLight
import br.thiago.habitflowapp.presentation.ui.theme.surfaceContainerLowestLight


@Composable
fun FrequencySelector(
    frequency: FrequencyType,
    onFrequencyChange: (FrequencyType) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        FrequencyButton(
            text = "Diário",
            selected = frequency == FrequencyType.DAILY,
            onClick = { onFrequencyChange(FrequencyType.DAILY) },
            modifier = Modifier.weight(1f)
        )
        FrequencyButton(
            text = "Semanal",
            selected = frequency == FrequencyType.WEEKLY,
            onClick = { onFrequencyChange(FrequencyType.WEEKLY) },
            modifier = Modifier.weight(1f)
        )
        FrequencyButton(
            text = "Dias Específicos",
            selected = frequency == FrequencyType.SPECIFIC,
            onClick = { onFrequencyChange(FrequencyType.SPECIFIC) },
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun FrequencyButton(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) primaryContainerLight else surfaceContainerLowestLight)
            .border(
                width = 2.dp,
                color = if (selected) primaryContainerLight else outlineLight,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) onPrimaryLight else onBackgroundLight,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FrequencySelectorPreview() {
    FrequencySelector(
        frequency = FrequencyType.DAILY,
        onFrequencyChange = {}
    )
}