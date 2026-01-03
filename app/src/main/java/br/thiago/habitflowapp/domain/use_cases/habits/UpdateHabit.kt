package br.thiago.habitflowapp.domain.use_cases.habits


import br.thiago.habitflowapp.domain.repository.HabitRepository
import br.thiago.habitflowapp.domain.model.Habit

import javax.inject.Inject

class UpdateHabit @Inject constructor(private val repository: HabitRepository){

    suspend operator fun invoke(habit: Habit) = repository.update(habit)

}