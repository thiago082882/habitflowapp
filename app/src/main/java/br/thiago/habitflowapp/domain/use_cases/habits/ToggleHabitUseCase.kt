package br.thiago.habitflowapp.domain.use_cases.habits


import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.repository.HabitRepository
import br.thiago.habitflowapp.domain.model.Habit

class ToggleHabitUseCase(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit): Response<Boolean> {
        return try {
            val updatedHabit = habit.copy(completed = !habit.completed)
            repository.updateHabit(updatedHabit)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}
