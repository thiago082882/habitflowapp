
package br.thiago.habitflowapp.presentation.screens.new_habit

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.thiago.habitflowapp.domain.model.FrequencyType
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.presentation.components.HabitTextField
import br.thiago.habitflowapp.presentation.components.PrimaryButton
import br.thiago.habitflowapp.presentation.screens.new_habit.components.*
import br.thiago.habitflowapp.presentation.ui.theme.backgroundLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    viewModel: NewHabitViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val state = viewModel.state
    val context = LocalContext.current
    val isFormValid = state.isFormValid()

    LaunchedEffect(viewModel.createHabitResponse) {
        when (val response = viewModel.createHabitResponse) {
            is Response.Success -> {
                Toast.makeText(context, "Hábito criado com sucesso!", Toast.LENGTH_SHORT).show()
                viewModel.clearForm()
                onBackClick()
            }
            is Response.Failure -> {
                Toast.makeText(context, "Erro: ${response.exception?.message}", Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }


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
                value = state.name,
                onValueChange = viewModel::onNameInput,
                label = "Ex: Meditar, Ler 10 páginas, Fazer 30 flexões",
                modifier = Modifier.fillMaxWidth()
            )
            if (state.name.isBlank()) {
                Text(
                    text = "O nome é obrigatório",
                    color = Color.Red,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(Modifier.height(16.dp))

            // --- DESCRIÇÃO (opcional) ---
            SectionTitle("Descrição (Opcional)")
            HabitTextField(
                value = state.description,
                onValueChange = viewModel::onDescriptionInput,
                label = "Qual o objetivo desse hábito?",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            Spacer(Modifier.height(20.dp))

            // --- FREQUÊNCIA ---
            SectionTitle("Frequência")
            FrequencySelector(
                frequency = state.frequency,
                onFrequencyChange = viewModel::onFrequencyInput
            )

            Spacer(Modifier.height(14.dp))

            when (state.frequency) {
                FrequencyType.DAILY -> InfoBoxWithHighlight(
                    text = "Este hábito será repetido todos os dias.",
                    highlight = "todos os dias."
                )
                FrequencyType.WEEKLY -> InfoBoxWithHighlight(
                    text = "Este hábito será repetido 1 vez por semana.",
                    highlight = "1 vez por semana."
                )
                FrequencyType.SPECIFIC -> SpecificDaysSelector(
                    selectedDays = state.selectedDays,
                    onDayToggle = viewModel::toggleDay
                )
            }

            if (state.frequency == FrequencyType.SPECIFIC &&
                state.selectedDays.none { it }
            ) {
                Text(
                    text = "Selecione pelo menos um dia",
                    color = Color.Red,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(Modifier.height(20.dp))

            // --- META ---
            SectionTitle("Meta Diária (Ex: 8 copos, 30 minutos)")
            HabitTextField(
                value = state.goal,
                onValueChange = viewModel::onGoalInput,
                label = "Ex: 8 copos, 30 minutos, 1 sessão",
                modifier = Modifier.fillMaxWidth()
            )
            if (state.goal.isBlank()) {
                Text(
                    text = "A meta é obrigatória",
                    color = Color.Red,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(Modifier.height(16.dp))

            // --- HORÁRIO ---
            SectionTitle("Horário do Lembrete")
            ReminderTimePicker(
                reminderTime = state.reminderTime,
                onTimeSelected = viewModel::onReminderTimeInput
            )
            if (state.reminderTime.isBlank()) {
                Text(
                    text = "O horário é obrigatório",
                    color = Color.Red,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(Modifier.height(16.dp))

            // --- NOTIFICAÇÃO ---
            SectionTitle("Ativar Notificações")
            ReminderSwitchCard(
                checked = state.active,
                onCheckedChange = viewModel::onReminderToggle
            )

            ReminderInfoText(visible = state.active)

            Spacer(Modifier.height(28.dp))

            // --- BOTÃO ---
            PrimaryButton(
                text = "Salvar Hábito",
                onClick = { viewModel.createHabit() },
                enabled = isFormValid
            )

            Spacer(Modifier.height(40.dp))
        }
    }
}
