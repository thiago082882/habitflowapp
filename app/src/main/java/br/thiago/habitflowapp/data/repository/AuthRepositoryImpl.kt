package br.thiago.habitflowapp.data.repository

import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.model.User
import br.thiago.habitflowapp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

open class AuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthRepository {

    override val currentUser: FirebaseUser? get() = firebaseAuth.currentUser

    override suspend fun login(
        email: String,
        senha: String
    ): Response<FirebaseUser> {

        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(
                email, senha
            ).await()
            Response.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }

    override suspend fun signUp(user: User): Response<FirebaseUser> {

        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()
            Response.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }

    }

    override suspend fun forgotPassword(email: String): Response<String> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Response.Success("E-mail de redefinição enviado com sucesso.")
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }


    override fun logout() {
        firebaseAuth.signOut()
    }


}