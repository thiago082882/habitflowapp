package br.thiago.habitflowapp.presentation.screens.habit_update


import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.thiago.habitflowapp.domain.model.FrequencyType
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.presentation.components.HabitTextField
import br.thiago.habitflowapp.presentation.components.PrimaryButton
import br.thiago.habitflowapp.presentation.screens.new_habit.components.CustomTopBar
import br.thiago.habitflowapp.presentation.screens.new_habit.components.FrequencySelector
import br.thiago.habitflowapp.presentation.screens.new_habit.components.InfoBoxWithHighlight
import br.thiago.habitflowapp.presentation.screens.new_habit.components.ReminderInfoText
import br.thiago.habitflowapp.presentation.screens.new_habit.components.ReminderSwitchCard
import br.thiago.habitflowapp.presentation.screens.new_habit.components.ReminderTimePicker
import br.thiago.habitflowapp.presentation.screens.new_habit.components.SectionTitle
import br.thiago.habitflowapp.presentation.screens.new_habit.components.SpecificDaysSelector
import br.thiago.habitflowapp.presentation.ui.theme.backgroundLight


@Composable
fun EditHabitScreen(
    viewModel: EditHabitViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val state = viewModel.state

    LaunchedEffect(key1 = viewModel.updateHabitResponse) {
        when (val response = viewModel.updateHabitResponse) {
            is Response.Success -> {
                Toast.makeText(context, "Hábito atualizado com sucesso", Toast.LENGTH_LONG).show()
                viewModel.clearForm()
                onBackClick()
            }
            is Response.Failure -> println("Erro ao atualizar hábito: ${response.exception?.message}")
            else -> Unit
        }
    }

    Scaffold(
        topBar = { CustomTopBar("Editar Hábito", onBackClick) },
        containerColor = backgroundLight
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SectionTitle("Nome do Hábito")
            HabitTextField(
                value = state.name,
                onValueChange = { viewModel.onNameInput(it) },
                label = "Ex: Meditar, Ler 10 páginas, Fazer 30 flex",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            SectionTitle("Descrição (Opcional)")
            HabitTextField(
                value = state.description,
                onValueChange = { viewModel.onDescriptionInput(it) },
                label = "Qual o objetivo desse hábito?",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Spacer(Modifier.height(20.dp))

            SectionTitle("Frequência")
            FrequencySelector(state.frequency) { viewModel.onFrequencyInput(it) }

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
                FrequencyType.SPECIFIC -> SpecificDaysSelector(state.selectedDays) { i ->
                    viewModel.toggleDay(i)
                }
            }

            Spacer(Modifier.height(20.dp))

            SectionTitle("Meta Diária (Ex: 8 copos, 30 minutos)")
            HabitTextField(
                value = state.goal,
                onValueChange = { viewModel.onGoalInput(it) },
                label = "Ex: 8 copos, 30 minutos, 1 sessão",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            SectionTitle("Horário do Lembrete")
            ReminderTimePicker(
                reminderTime = state.reminderTime,
                onTimeSelected = { viewModel.onReminderTimeInput(it) }
            )

            Spacer(Modifier.height(16.dp))
            SectionTitle("Ativar Notificações")
            ReminderSwitchCard(
                checked = state.active,
                onCheckedChange = { checked ->
                    viewModel.onReminderToggle(checked)
                }
            )

            ReminderInfoText(visible = state.active)

            Spacer(Modifier.height(28.dp))

            PrimaryButton(
                text = "Atualizar Hábito",
                onClick = { viewModel.onUpdateHabit() }
            )

            Spacer(Modifier.height(40.dp))
        }
    }
}
