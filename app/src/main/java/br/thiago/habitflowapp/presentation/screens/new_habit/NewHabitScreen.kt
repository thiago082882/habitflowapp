package br.thiago.habitflowapp.presentation.screens.new_habit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.thiago.habitflowapp.presentation.components.HabitTextField
import br.thiago.habitflowapp.presentation.components.PrimaryButton
import br.thiago.habitflowapp.domain.model.FrequencyType
import br.thiago.habitflowapp.presentation.screens.new_habit.components.CustomTopBar
import br.thiago.habitflowapp.presentation.screens.new_habit.components.FrequencySelector
import br.thiago.habitflowapp.presentation.screens.new_habit.components.InfoBoxWithHighlight
import br.thiago.habitflowapp.presentation.screens.new_habit.components.ReminderInfoText
import br.thiago.habitflowapp.presentation.screens.new_habit.components.ReminderSwitchCard
import br.thiago.habitflowapp.presentation.screens.new_habit.components.ReminderTimePicker
import br.thiago.habitflowapp.presentation.screens.new_habit.components.SectionTitle
import br.thiago.habitflowapp.presentation.screens.new_habit.components.SpecificDaysSelector

import br.thiago.habitflowapp.presentation.ui.theme.backgroundLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var goal by remember { mutableStateOf(TextFieldValue("")) }
    var frequency by remember { mutableStateOf(FrequencyType.DAILY) }
    var reminderTime by remember { mutableStateOf("") }
    var reminderEnabled by remember { mutableStateOf(true) }
    var selectedDays by remember { mutableStateOf(List(7) { true }) }

    Scaffold(
        topBar = { CustomTopBar("Novo Hábito", onBackClick) },
        containerColor = backgroundLight
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // --- NOME ---
            SectionTitle("Nome do Hábito")
            HabitTextField(
                value = name,
                onValueChange = { name = it },
                label = "Ex: Meditar, Ler 10 páginas, Fazer 30 flex",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            // --- DESCRIÇÃO ---
            SectionTitle("Descrição (Opcional)")
            HabitTextField(
                value = description,
                onValueChange = { description = it },
                label = "Qual o objetivo desse hábito?",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Spacer(Modifier.height(20.dp))

            // --- FREQUÊNCIA ---
            SectionTitle("Frequência")
            FrequencySelector(frequency) { frequency = it }

            Spacer(Modifier.height(14.dp))
            when (frequency) {
                FrequencyType.DAILY -> InfoBoxWithHighlight(
                    text = "Este hábito será repetido todos os dias.",
                    highlight = "todos os dias."
                )
                FrequencyType.WEEKLY -> InfoBoxWithHighlight(
                    text = "Este hábito será repetido 1 vez por semana.",
                    highlight = "1 vez por semana."
                )
                FrequencyType.SPECIFIC -> SpecificDaysSelector(selectedDays) { i ->
                    selectedDays = selectedDays.toMutableList().apply { this[i] = !this[i] }
                }
            }


            Spacer(Modifier.height(20.dp))

            // --- META DIÁRIA ---
            SectionTitle("Meta Diária (Ex: 8 copos, 30 minutos)")
            HabitTextField(
                value = goal,
                onValueChange = { goal = it },
                label = "Ex: 8 copos, 30 minutos, 1 sessão",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // --- LEMBRETE ---
            SectionTitle("Horário do Lembrete")
            ReminderTimePicker(reminderTime = reminderTime, onTimeSelected = { reminderTime = it })

            Spacer(Modifier.height(16.dp))
            SectionTitle("Ativar Notificações")
            ReminderSwitchCard(
                checked = reminderEnabled,
                onCheckedChange = { reminderEnabled = it }
            )

            ReminderInfoText(visible = reminderEnabled)

            Spacer(Modifier.height(28.dp))
            //SaveButton(onSaveClick)
            PrimaryButton(
                text = "Salvar Hábito",
                onClick = onSaveClick
            )
            Spacer(Modifier.height(40.dp))
        }
    }
}

@Preview
@Composable
private fun AddHabitScreenPreview() {
    AddHabitScreen()
}
