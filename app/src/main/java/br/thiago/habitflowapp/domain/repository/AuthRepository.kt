package br.thiago.habitflowapp.domain.repository

import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.model.User
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser : FirebaseUser?
    suspend fun login (email:String , senha : String) : Response<FirebaseUser>
    suspend fun signUp(user: User): Response<FirebaseUser>
    suspend fun forgotPassword(email: String): Response<String>
    fun logout()


}