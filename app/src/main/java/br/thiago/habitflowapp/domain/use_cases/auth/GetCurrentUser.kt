package br.thiago.habitflowapp.domain.use_cases.auth


import br.thiago.habitflowapp.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUser  @Inject constructor(private val repository : AuthRepository ){

operator  fun invoke() = repository.currentUser

}