package br.thiago.habitflowapp.data.repository

import br.thiago.habitflowapp.utils.mockFirebaseUserTask
import br.thiago.habitflowapp.utils.mockTask
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class AuthRepositoryImplTest {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        firebaseAuth = mockk()
        repository = AuthRepositoryImpl(firebaseAuth)
    }

    @Test
    fun `login should return success using mockFirebaseUserTask`() = runTest {
        val (firebaseUser, _, task) = mockFirebaseUserTask()

        val firebaseAuth = mockk<FirebaseAuth> {
            every { signInWithEmailAndPassword("test@example.com", "123456") } returns task
        }

        val repository = AuthRepositoryImpl(firebaseAuth)
        val result = repository.login("test@example.com", "123456")

        assertTrue(result is Response.Success)
        assertEquals(firebaseUser, (result as Response.Success).data)
    }

    @Test
    fun `signUp should return success using mockFirebaseUserTask`() = runTest {
        val domainUser = User(email = "signup@example.com", password = "123456")
        val (firebaseUser, _, task) = mockFirebaseUserTask()

        val firebaseAuth = mockk<FirebaseAuth> {
            every { createUserWithEmailAndPassword(domainUser.email, domainUser.password) } returns task
        }

        val repository = AuthRepositoryImpl(firebaseAuth)
        val result = repository.signUp(domainUser)

        assertTrue(result is Response.Success)
        assertEquals(firebaseUser, (result as Response.Success).data)
    }

    @Test
    fun `forgotPassword should return success using mockTask`() = runTest {
        val email = "reset@example.com"
        val task: Task<Void> = mockTask(null)

        val firebaseAuth = mockk<FirebaseAuth> {
            every { sendPasswordResetEmail(email) } returns task
        }

        val repository = AuthRepositoryImpl(firebaseAuth)
        val result = repository.forgotPassword(email)

        assertTrue(result is Response.Success)
        assertEquals("E-mail de redefinição enviado com sucesso.", (result as Response.Success).data)
    }

    @Test
    fun `logout should call signOut`() {
        val firebaseAuth = mockk<FirebaseAuth> {
            every { signOut() } just Runs
        }

        val repository = AuthRepositoryImpl(firebaseAuth)
        repository.logout()

        verify(exactly = 1) { firebaseAuth.signOut() }
    }
}
