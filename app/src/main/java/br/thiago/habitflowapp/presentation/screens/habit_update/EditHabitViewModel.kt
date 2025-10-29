//package br.thiago.habitflowapp.presentation.screens.habit_update
//
//import android.content.Context
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import br.thiago.habitflowapp.domain.model.FrequencyType
//import br.thiago.habitflowapp.domain.model.Response
//import br.thiago.habitflowapp.domain.use_cases.habits.HabitsUseCases
//import br.thiago.habitflowapp.domain.use_cases.auth.AuthUseCases
//import br.thiago.habitflowapp.domain.model.Habit
//import br.thiago.habitflowapp.presentation.utils.NotificationScheduler
//import br.thiago.habitflowapp.presentation.utils.NotificationScheduler.cancelHabitNotification
//import dagger.hilt.android.lifecycle.HiltViewModel
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class EditHabitViewModel @Inject constructor(
//    @ApplicationContext private val context: Context,
//    private val savedStateHandle: SavedStateHandle,
//    private val habitsUseCases: HabitsUseCases,
//    private val authUseCases: AuthUseCases
//) : ViewModel() {
//
//    var state by mutableStateOf(UpdateHabitState())
//
//
//    // RESPONSE
//    var updateHabitResponse by mutableStateOf<Response<Boolean>?>(null)
//        private set
//
//
//    // ARGUMENTS
//    val data = savedStateHandle.get<String>("habitId")
//    val habit = Habit.fromJson(data!!)
//
//    // USER SESSION
//    val currentUser = authUseCases.getCurrentUser()
//
//
//   init {
//        state = state.copy(
//            name =habit.name,
//            description = habit.description,
//            goal = habit.goal,
//            streak =habit.streak,
//            frequency = habit.frequency,
//            reminderTime = habit.reminderTime,
//            active = habit.active,
//            selectedDays = habit.selectedDays,
//            createdAt = habit.createdAt
//        )
//
//    }
//
//
//
//    // --- FUNÇÕES DE INPUT ---
//    fun onNameInput(name: String) {
//        state = state.copy(name = name)
//    }
//
//    fun onDescriptionInput(description: String) {
//        state = state.copy(description = description)
//    }
//
//    fun onGoalInput(goal: String) {
//        state = state.copy(goal = goal)
//    }
//
//    fun onFrequencyInput(frequency: FrequencyType) {
//        state = state.copy(frequency = frequency)
//    }
//
//    fun toggleDay(index: Int) {
//        val newDays = state.selectedDays.toMutableList()
//        newDays[index] = !newDays[index]
//        state = state.copy(selectedDays = newDays)
//    }
//
//    fun onReminderTimeInput(time: String) {
//        state = state.copy(reminderTime = time)
//    }
//
//    fun onReminderToggle(isChecked: Boolean) {
//        state = state.copy(active = isChecked)
//    }
//
//    fun updateHabit(habit: Habit) = viewModelScope.launch {
//        updateHabitResponse = Response.Loading
//        val result = habitsUseCases.updateHabit(habit)
//        updateHabitResponse = result
//    }
//
//    fun onUpdateHabit() {
//        viewModelScope.launch {
//
//            val currentHabit = habitsUseCases.getHabitById(habit.id)
//
//            val updatedHabit = Habit(
//                id = habit.id,
//                name = state.name,
//                description = state.description,
//                goal = state.goal,
//                streak = state.streak,
//                completed = currentHabit?.completed ?: habit.completed,
//                frequency = state.frequency,
//                reminderTime = state.reminderTime,
//                active = state.active,
//                selectedDays = state.selectedDays,
//                idUser = currentUser?.uid ?: "",
//                createdAt = habit.createdAt
//            )
//
//            cancelHabitNotification(context = context, habitName = habit.name)
//            updateHabit(updatedHabit)
//            scheduleHabitNotificationIfActive(updatedHabit)
//        }
//    }
//
//    private fun scheduleHabitNotificationIfActive(habit: Habit) {
//        if (habit.active && !habit.completed) {
//            val frequencyStr = when (habit.frequency) {
//                FrequencyType.DAILY -> "DAILY"
//                FrequencyType.WEEKLY -> "WEEKLY"
//                FrequencyType.SPECIFIC -> "SPECIFIC"
//                else -> "DAILY"
//            }
//
//            NotificationScheduler.scheduleHabitNotification(
//                context = context,
//                habitName = habit.name,
//                habitGoal = habit.goal,
//                reminderTime = habit.reminderTime,
//                frequency = frequencyStr
//            )
//        }
//    }
//
//    fun clearForm() {
//
//        updateHabitResponse = null
//    }
//}

package br.thiago.habitflowapp.presentation.screens.habit_update

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.thiago.habitflowapp.domain.model.FrequencyType
import br.thiago.habitflowapp.domain.model.Habit
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.repository.NotificationHandler
import br.thiago.habitflowapp.domain.use_cases.auth.AuthUseCases
import br.thiago.habitflowapp.domain.use_cases.habits.HabitsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditHabitViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val habitsUseCases: HabitsUseCases,
    private val authUseCases: AuthUseCases,
    private val notificationHandler: NotificationHandler
) : ViewModel() {

    var state by mutableStateOf(UpdateHabitState())
    var updateHabitResponse by mutableStateOf<Response<Boolean>?>(null)


    // ARGUMENTOS
    private val data = savedStateHandle.get<String>("habitId")
    private val habit = Habit.fromJson(data!!)

    // SESSÃO DO USUÁRIO
    private val currentUser = authUseCases.getCurrentUser()

    init {
        state = state.copy(
            name = habit.name,
            description = habit.description,
            goal = habit.goal,
            streak = habit.streak,
            frequency = habit.frequency,
            reminderTime = habit.reminderTime,
            active = habit.active,
            selectedDays = habit.selectedDays,
            createdAt = habit.createdAt
        )
    }

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

    private fun updateState(block: UpdateHabitState.() -> UpdateHabitState) {
        state = state.block()
    }

    // --- ATUALIZAR HÁBITO ---
    fun onUpdateHabit() {
        viewModelScope.launch {
            val currentHabit = habitsUseCases.getHabitById(habit.id)
            val updatedHabit = Habit(
                id = habit.id,
                name = state.name,
                description = state.description,
                goal = state.goal,
                streak = state.streak,
                completed = currentHabit?.completed ?: habit.completed,
                frequency = state.frequency,
                reminderTime = state.reminderTime,
                active = state.active,
                selectedDays = state.selectedDays,
                idUser = currentUser?.uid ?: "",
                createdAt = habit.createdAt
            )

            // 🔔 Notificações via handler
            notificationHandler.cancelHabitNotification(habit.name)
            updateHabit(updatedHabit)
            scheduleHabitNotificationIfActive(updatedHabit)
        }
    }

    private fun updateHabit(habit: Habit) = viewModelScope.launch {
        updateHabitResponse = Response.Loading
        val result = habitsUseCases.updateHabit(habit)
        updateHabitResponse = result
    }

    private fun scheduleHabitNotificationIfActive(habit: Habit) {
        if (habit.active && !habit.completed) {
            notificationHandler.scheduleHabitNotification(
                habitName = habit.name,
                habitGoal = habit.goal,
                reminderTime = habit.reminderTime,
                frequency = habit.frequency.name
            )
        }
    }

    fun clearForm() {
        updateHabitResponse = null
    }
}
