package br.thiago.habitflowapp.utils

import com.google.android.gms.tasks.Task
import io.mockk.every
import io.mockk.mockk
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

inline fun <reified T> mockTask(result: T?, exception: Exception? = null): Task<T> {
    val task: Task<T> = mockk(relaxed = true)
    every { task.isComplete } returns true
    every { task.exception } returns exception
    every { task.isCanceled } returns false
    every { task.result } returns result
    return task
}

/**
 * Cria um FirebaseUser mockado junto com AuthResult e Task<AuthResult> para testes de login/signUp.
 */
fun mockFirebaseUserTask(): Triple<FirebaseUser, AuthResult, Task<AuthResult>> {
    val firebaseUser = mockk<FirebaseUser>()
    val authResult = mockk<AuthResult> { every { user } returns firebaseUser }
    val task = mockTask(authResult)
    return Triple(firebaseUser, authResult, task)
}

/**
 * Cria um Task<Void> mockado, útil para métodos que retornam Task<Void> (ex: Firestore set()).
 */
fun mockVoidTask(): Task<Void> = mockTask(null)
