package br.thiago.habitflowapp.presentation.screens.home.components


import br.thiago.habitflowapp.domain.model.Habit
import br.thiago.habitflowapp.domain.model.Response


data class HomeState(
    val habitsResponse: Response<List<Habit>>? = null,
    val completedHabits: Int = 0,
    val totalHabits: Int = 0,
    val progressPercent: Int = 0,
    val deleteResponse: Response<Boolean>? = null
)
