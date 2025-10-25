package br.thiago.habitflowapp.domain.use_cases.users

import br.thiago.habitflowapp.domain.repository.UsersRepository
import javax.inject.Inject

class GetUserById @Inject constructor(private val repository: UsersRepository) {

    operator  fun invoke(id:String)= repository.getUserById(id)
}