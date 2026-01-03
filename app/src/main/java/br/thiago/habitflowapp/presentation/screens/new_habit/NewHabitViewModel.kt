
package br.thiago.habitflowapp.presentation.screens.new_habit

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.thiago.habitflowapp.domain.model.FrequencyType
import br.thiago.habitflowapp.domain.model.Habit
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.use_cases.auth.AuthUseCases
import br.thiago.habitflowapp.domain.use_cases.habits.HabitsUseCases
import br.thiago.habitflowapp.presentation.utils.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewHabitViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val habitsUseCases: HabitsUseCases,
    private val authUseCases: AuthUseCases
) : ViewModel() {

    var state by mutableStateOf(NewHabitState())


    var createHabitResponse by mutableStateOf<Response<Boolean>?>(null)
        private set

    val currentUser = authUseCases.getCurrentUser()

    // --- INPUTS ---
    fun onNameInput(name: String) = updateState { copy(name = name) }
    fun onDescriptionInput(description: String) = updateState { copy(description = description) }
    fun onGoalInput(goal: String) = updateState { copy(goal = goal) }
    fun onFrequencyInput(frequency: FrequencyType) = updateState { copy(frequency = frequency) }

    fun toggleDay(index: Int) {
        val newDays = state.selectedDays.toMutableList()
        newDays[index] = !newDays[index]
        updateState { copy(selectedDays = newDays) }
    }

    fun onReminderTimeInput(time: String) = updateState { copy(reminderTime = time) }
    fun onReminderToggle(isChecked: Boolean) = updateState { copy(active = isChecked) }

    private fun updateState(block: NewHabitState.() -> NewHabitState) {
        state = state.block()
    }

    // --- CREATE HABIT ---
    fun createHabit() {
        viewModelScope.launch {
            if (!state.isFormValid()) {
                createHabitResponse = Response.Failure(
                    Exception("Preencha todos os campos obrigatórios antes de salvar.")
                )
                return@launch
            }

            val habit = Habit(
                name = state.name.trim(),
                description = state.description.trim(),
                goal = state.goal.trim(),
                streak = state.streak,
                completed = state.completed,
                frequency = state.frequency,
                reminderTime = state.reminderTime,
                active = state.active,
                selectedDays = state.selectedDays,
                idUser = currentUser?.uid ?: "",
                createdAt = state.createdAt
            )

            if (habit.active) scheduleHabitNotification(habit)
            else cancelHabitNotification(habit.name)

            createHabitResponse = Response.Loading
            createHabitResponse = habitsUseCases.create(habit)
        }
    }


    private fun scheduleHabitNotification(habit: Habit) {
        val frequencyStr = when (habit.frequency) {
            FrequencyType.DAILY -> "DAILY"
            FrequencyType.WEEKLY -> "WEEKLY"
            FrequencyType.SPECIFIC -> "SPECIFIC"
            else -> "DAILY"
        }

        Log.d("HabitNotification", "Agendando notificação: ${habit.name} às ${habit.reminderTime} (${frequencyStr})")

        NotificationScheduler.scheduleHabitNotification(
            context = context,
            habitName = habit.name,
            habitGoal = habit.goal,
            reminderTime = habit.reminderTime,
            frequency = frequencyStr
        )
    }

    fun cancelHabitNotification(habitName: String) {
        NotificationScheduler.cancelHabitNotification(context, habitName)
    }

    fun clearForm() {
        state = NewHabitState()
        createHabitResponse = null
    }
}

