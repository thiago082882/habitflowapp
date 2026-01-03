package br.thiago.habitflowapp.domain.use_cases.habits


import br.thiago.habitflowapp.domain.repository.HabitRepository
import javax.inject.Inject

class DeleteHabit @Inject constructor(private val repository : HabitRepository) {

    suspend operator fun invoke(idHabit: String) = repository.delete(idHabit)


}