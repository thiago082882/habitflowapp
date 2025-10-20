package br.thiago.habitflowapp.domain.use_cases.users


import br.thiago.habitflowapp.domain.model.User
import br.thiago.habitflowapp.domain.repository.UsersRepository
import javax.inject.Inject

class Create @Inject constructor( private val repository: UsersRepository) {

    suspend operator  fun invoke(user: User) = repository.create(user)
}