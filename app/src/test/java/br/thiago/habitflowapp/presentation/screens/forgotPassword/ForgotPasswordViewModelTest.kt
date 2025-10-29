package br.thiago.habitflowapp.presentation.screens.forgotPassword

import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.use_cases.auth.AuthUseCases
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class ForgotPasswordViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var authUseCases: AuthUseCases
    private lateinit var viewModel: ForgotPasswordViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        authUseCases = mockk {
            coEvery { forgotPassword(any()) } returns Response.Success("Email enviado")
        }

        viewModel = ForgotPasswordViewModel(authUseCases)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun validateEmail_validEmail_setsIsEmailValidTrue() {
        viewModel.onEmailInput("teste@exemplo.com")
        assertTrue(viewModel.isEmailValid)
        assertEquals("", viewModel.emailErrMsg)
        assertTrue(viewModel.isEnabledSendButton)
    }

    @Test
    fun validateEmail_invalidEmail_setsIsEmailValidFalse() {
        viewModel.onEmailInput("emailinvalido")
        assertFalse(viewModel.isEmailValid)
        assertEquals("O email não é válido", viewModel.emailErrMsg)
        assertFalse(viewModel.isEnabledSendButton)
    }

    @Test
    fun validateEmail_emptyEmail_setsIsEmailValidFalse() {
        viewModel.onEmailInput("")
        assertFalse(viewModel.isEmailValid)
        assertEquals("Digite seu e-mail", viewModel.emailErrMsg)
        assertFalse(viewModel.isEnabledSendButton)
    }

    @Test
    fun sendForgotPasswordEmail_success_setsResponseToSuccess() = runTest {
        viewModel.onEmailInput("teste@exemplo.com")
        viewModel.sendForgotPasswordEmail()
        advanceUntilIdle()

        val response = viewModel.forgotPasswordResponse
        assertTrue(response is Response.Success)
        assertEquals("Email enviado", (response as Response.Success).data)
    }

    @Test
    fun sendForgotPasswordEmail_emptyEmail_setsResponseToFailure() = runTest {
        viewModel.onEmailInput("")
        viewModel.sendForgotPasswordEmail()
        advanceUntilIdle()

        val response = viewModel.forgotPasswordResponse
        assertTrue(response is Response.Failure)
        assertEquals("Digite seu e-mail", (response as Response.Failure).exception?.message)
    }
}
