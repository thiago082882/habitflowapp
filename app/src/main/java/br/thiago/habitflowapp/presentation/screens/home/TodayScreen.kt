package br.thiago.habitflowapp.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.thiago.habitflowapp.domain.model.FrequencyType
import br.thiago.habitflowapp.models.Habit1
import br.thiago.habitflowapp.presentation.screens.home.components.HabitCard
import br.thiago.habitflowapp.presentation.screens.home.components.ProgressCard
import br.thiago.habitflowapp.presentation.screens.home.components.TodayHeader
import br.thiago.habitflowapp.presentation.ui.theme.primaryContainerLight
import br.thiago.habitflowapp.presentation.ui.theme.primaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    habits: List<Habit1>,
    onToggleHabit: (Habit1) -> Unit,
    onAddHabitClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddHabitClick,
                containerColor = primaryContainerLight,
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Hábito", tint = Color.White)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                TodayHeader()
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            item {
                ProgressCard(
                    title = "Progresso do Dia",
                    progressPercent = 67,
                    completed = 2,
                    total = 3,
                    containerColor = primaryContainerLight.copy(alpha = 0.4f),
                    accentColor = primaryLight
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Meus Hábitos",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            items(habits) { habit ->
                HabitCard(
                    title = habit.name,
                    subtitle = if (habit.name.contains("Estudar", ignoreCase = true))
                        "Freq: 3 dias por semana (SQS)"
                    else
                        "Streak: ${habit.streak} dias",
                    completed = habit.completed,
                    borderColor = when {
                        habit.completed -> primaryContainerLight
                        habit.name.contains("", ignoreCase = true) -> Color(0xFFF1C40F)
                        else -> MaterialTheme.colorScheme.primary
                    },
                    buttonLabel = if (habit.name.contains(
                            "Estudar",
                            ignoreCase = true
                        )
                    ) "OK" else null,
                    onClick = { onToggleHabit(habit) }
                )
            }


            item {
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Arraste para baixo para atualizar.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun TodayScreenPreview() {
    val habits = listOf(
        Habit1(
            name = "Beber Água (8 copos)",
            frequency = FrequencyType.DAILY,
            completed = true,
            streak = 12
        ),
        Habit1(
            name = "Estudar Programação (30 min)",
            frequency = FrequencyType.WEEKLY,
            completed = false,
            streak = 0
        ),
        Habit1(
            name = "Caminhada (10 mins)",
            frequency = FrequencyType.DAILY,
            completed = true,
            streak = 7
        )
    )


    TodayScreen(
        habits = habits,
        onToggleHabit = {},
        onAddHabitClick = {}
    )
}
