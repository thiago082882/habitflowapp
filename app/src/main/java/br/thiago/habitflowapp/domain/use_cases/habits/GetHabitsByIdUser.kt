package br.thiago.habitflowapp.domain.use_cases.habits

import br.thiago.habitflowapp.domain.repository.HabitRepository

import javax.inject.Inject

class GetHabitsByIdUser @Inject constructor(private val repository: HabitRepository) {

operator fun invoke(idUser : String) = repository.getHabitsByUserId(idUser)
}