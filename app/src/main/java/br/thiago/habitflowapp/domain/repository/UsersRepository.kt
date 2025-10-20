package br.thiago.habitflowapp.domain.repository

import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.model.User
import kotlinx.coroutines.flow.Flow



interface UsersRepository {

    suspend fun create(user: User): Response<Boolean>
    suspend fun update(user: User): Response<Boolean>
    fun getUserById(id: String): Flow<User>
}