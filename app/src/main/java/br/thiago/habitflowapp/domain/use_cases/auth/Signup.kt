package br.thiago.habitflowapp.domain.use_cases.auth


import br.thiago.habitflowapp.domain.model.User
import br.thiago.habitflowapp.domain.repository.AuthRepository
import javax.inject.Inject

class Signup @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(user: User) = repository.signUp(user)

}