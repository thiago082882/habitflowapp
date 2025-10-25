package br.thiago.habitflowapp.domain.repository


import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {

    fun getHabits(): Flow<Response<List<Habit>>>
    fun getHabitsByUserId(idUser: String): Flow<Response<List<Habit>>>
    suspend fun create(habit: Habit): Response<Boolean>
    suspend fun update(habit: Habit): Response<Boolean>
    suspend fun delete(idHabit: String): Response<Boolean>
    suspend fun updateHabit(habit: Habit): Response<Boolean>
    suspend fun getHabitById(habitId: String): Habit?



}