package br.thiago.habitflowapp.domain.use_cases.habits


import br.thiago.habitflowapp.domain.repository.HabitRepository
import javax.inject.Inject

class GetHabits @Inject constructor(private val repository: HabitRepository) {

operator fun invoke() = repository.getHabits()
}