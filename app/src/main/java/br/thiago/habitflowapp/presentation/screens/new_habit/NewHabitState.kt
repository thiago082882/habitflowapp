package br.thiago.habitflowapp.presentation.screens.new_habit

import br.thiago.habitflowapp.domain.model.FrequencyType


data class NewHabitState(
 var name: String = "",
 var description: String = "",
 var goal: String = "",
 var streak: Int = 0,
 var completed: Boolean = false,
 var frequency: FrequencyType = FrequencyType.DAILY,
 var reminderTime: String = "",
 var active: Boolean = false,
 var selectedDays: List<Boolean> = listOf(true, true, true, true, true, true, true),
 var createdAt: Long = System.currentTimeMillis()
)


fun NewHabitState.isFormValid(): Boolean {
 val hasName = name.isNotBlank()
 val hasGoal = goal.isNotBlank()
 val hasTime = reminderTime.isNotBlank()
 val hasSelectedDays = if (frequency == FrequencyType.SPECIFIC) {
  selectedDays.any { it }
 } else true

 return hasName && hasGoal && hasTime && hasSelectedDays
}