package br.thiago.habitflowapp.presentation.screens.habit_update

import br.thiago.habitflowapp.domain.model.FrequencyType


data class UpdateHabitState(
 var name: String = "",
 var description: String = "",
 var goal: String = "",
 var streak: Int = 0,
 var frequency: FrequencyType = FrequencyType.DAILY,
 var reminderTime: String = "",
 var active: Boolean = false,
 var selectedDays: List<Boolean> = listOf(true, true, true, true, true, true, true),
 var createdAt: Long = System.currentTimeMillis()
)
