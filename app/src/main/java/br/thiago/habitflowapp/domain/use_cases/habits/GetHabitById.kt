package br.thiago.habitflowapp.domain.use_cases.habits

import br.thiago.habitflowapp.domain.repository.HabitRepository
import javax.inject.Inject

class GetHabitById @Inject constructor(private val repository: HabitRepository) {
    suspend operator fun invoke(habitId: String) = repository.getHabitById(habitId)
}
