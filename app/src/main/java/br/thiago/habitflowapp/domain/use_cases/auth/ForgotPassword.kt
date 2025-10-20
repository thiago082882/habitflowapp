package br.thiago.habitflowapp.domain.use_cases.auth


import br.thiago.habitflowapp.domain.repository.AuthRepository
import javax.inject.Inject


class ForgotPassword @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String) = repository.forgotPassword(email)

}