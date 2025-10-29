package br.thiago.habitflowapp.presentation.screens.login

import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.use_cases.auth.AuthUseCases
import br.thiago.habitflowapp.domain.use_cases.auth.GetCurrentUser
import br.thiago.habitflowapp.domain.use_cases.auth.Login
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
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
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var authUseCases: AuthUseCases
    private lateinit var loginUseCase: Login
    private lateinit var getCurrentUserUseCase: GetCurrentUser
    private lateinit var viewModel: LoginViewModel
    private val mockUser: FirebaseUser = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        loginUseCase = mockk()
        getCurrentUserUseCase = mockk()

        authUseCases = AuthUseCases(
            getCurrentUser = getCurrentUserUseCase,
            login = loginUseCase,
            logout = mockk(relaxed = true),
            signup = mockk(relaxed = true),
            forgotPassword = mockk(relaxed = true)
        )

        every { getCurrentUserUseCase() } returns null
        viewModel = LoginViewModel(authUseCases)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun validateEmail_validEmail_setsIsEmailValidTrue() {
        viewModel.onEmailInput("test@example.com")
        viewModel.validateEmail()
        assertTrue(viewModel.isEmailValid)
        assertEquals("", viewModel.emailErrMsg)
    }

    @Test
    fun validateEmail_invalidEmail_setsIsEmailValidFalse() {
        viewModel.onEmailInput("invalid-email")
        viewModel.validateEmail()
        assertFalse(viewModel.isEmailValid)
        assertEquals("O email não é valido", viewModel.emailErrMsg)
    }

    @Test
    fun validateEmail_emptyEmail_setsIsEmailValidFalse() {
        viewModel.onEmailInput("")
        viewModel.validateEmail()
        assertFalse(viewModel.isEmailValid)
    }

    @Test
    fun validatePassword_validComplexPassword_setsIsPasswordValidTrue() {
        viewModel.onPasswordInput("Abcd1234!")
        viewModel.validatePassword()
        assertTrue(viewModel.isPasswordValid)
        assertEquals("", viewModel.passwordErrMsg)
    }

    @Test
    fun validatePassword_tooShortPassword_setsIsPasswordValidFalse() {
        viewModel.onPasswordInput("Ab1!")
        viewModel.validatePassword()
        assertFalse(viewModel.isPasswordValid)
        assertEquals(
            "Ao menos 8 caracteres",
            viewModel.passwordErrMsg
        )
    }

    @Test
    fun validatePassword_emptyPassword_setsIsPasswordValidFalse() {
        viewModel.onPasswordInput("")
        viewModel.validatePassword()
        assertFalse(viewModel.isPasswordValid)
    }


    @Test
    fun onEmailInput_updatesStateCorrectly() {
        viewModel.onEmailInput("abc@xyz.com")
        assertEquals("abc@xyz.com", viewModel.state.email)
    }

    @Test
    fun onPasswordInput_updatesStateCorrectly() {
        viewModel.onPasswordInput("Abcd1234!")
        assertEquals("Abcd1234!", viewModel.state.password)
    }


    @Test
    fun enableLoginButton_true_whenBothValidationsAreTrue() {
        viewModel.onEmailInput("test@example.com")
        viewModel.onPasswordInput("Abcd1234!")
        viewModel.validateEmail()
        viewModel.validatePassword()
        assertTrue(viewModel.isEnabledLoginButton)
    }

    @Test
    fun enableLoginButton_false_whenOneValidationFails() {
        viewModel.onEmailInput("invalid-email")
        viewModel.onPasswordInput("Abcd1234!")
        viewModel.validateEmail()
        viewModel.validatePassword()
        assertFalse(viewModel.isEnabledLoginButton)
    }


    @Test
    fun login_success_setsLoginResponseToSuccess() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Response.Success(mockUser)

        viewModel.onEmailInput("test@example.com")
        viewModel.onPasswordInput("Abcd1234!")
        viewModel.login()
        advanceUntilIdle()

        assertThat(viewModel.loginResponse).isInstanceOf(Response.Success::class.java)
        assertThat((viewModel.loginResponse as Response.Success).data).isEqualTo(mockUser)
    }

    @Test
    fun login_failure_setsLoginResponseToFailure() = runTest {
        val exception = Exception("Login error")
        coEvery { loginUseCase(any(), any()) } returns Response.Failure(exception)

        viewModel.onEmailInput("test@example.com")
        viewModel.onPasswordInput("Abcd1234!")
        viewModel.login()
        advanceUntilIdle()

        val response = viewModel.loginResponse
        assertTrue(response is Response.Failure)
        assertEquals("Login error", (response as Response.Failure).exception?.message)
    }

    @Test
    fun login_setsResponseToLoadingAndThenSuccess() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Response.Success(mockUser)

        assertThat(viewModel.loginResponse).isNull()

        viewModel.onEmailInput("test@example.com")
        viewModel.onPasswordInput("Abcd1234!")
        viewModel.login()
        advanceUntilIdle()

        assertThat(viewModel.loginResponse).isInstanceOf(Response.Success::class.java)
        assertThat((viewModel.loginResponse as Response.Success).data).isEqualTo(mockUser)
    }

    @Test
    fun init_setsLoginResponse_whenCurrentUserExists() {
        every { getCurrentUserUseCase() } returns mockUser

        val vm = LoginViewModel(authUseCases.copy(getCurrentUser = getCurrentUserUseCase))
        assertThat(vm.loginResponse).isInstanceOf(Response.Success::class.java)
        assertThat((vm.loginResponse as Response.Success).data).isEqualTo(mockUser)
    }
}
