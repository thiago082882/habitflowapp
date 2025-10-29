package br.thiago.habitflowapp.presentation.screens.signUp

import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.model.User
import br.thiago.habitflowapp.domain.use_cases.auth.AuthUseCases
import br.thiago.habitflowapp.domain.use_cases.users.UsersUseCases
import com.google.firebase.auth.FirebaseUser
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
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class SignupViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var authUseCases: AuthUseCases
    private lateinit var usersUseCases: UsersUseCases
    private lateinit var viewModel: SignupViewModel

    private val mockUser: FirebaseUser = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        authUseCases = AuthUseCases(
            login = mockk(relaxed = true),
            logout = mockk(relaxed = true),
            signup = mockk(relaxed = true),
            forgotPassword = mockk(relaxed = true),
            getCurrentUser = mockk {
                coEvery { this@mockk.invoke() } returns null
            }
        )


        usersUseCases = mockk(relaxed = true)

        viewModel = SignupViewModel(authUseCases, usersUseCases)
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
        assertEquals("O email não é válido", viewModel.emailErrMsg)
    }


    @Test
    fun validatePassword_validPassword_setsIsPasswordValidTrue() {
        viewModel.onPasswordInput("Abcd1234!")
        viewModel.validatePassword()
        assertTrue(viewModel.isPasswordValid)
        assertEquals("", viewModel.passwordErrMsg)
    }

    @Test
    fun validatePassword_invalidPassword_setsIsPasswordValidFalse() {
        viewModel.onPasswordInput("abc123")
        viewModel.validatePassword()
        assertFalse(viewModel.isPasswordValid)
        assertEquals(
            "Senha deve ter pelo menos 8 caracteres, incluir maiúscula, minúscula, número e caractere especial",
            viewModel.passwordErrMsg
        )
    }


    @Test
    fun validateConfirmPassword_matchingPasswords_setsIsConfirmPasswordTrue() {
        viewModel.onPasswordInput("Abcd1234!")
        viewModel.onConfirmPasswordInput("Abcd1234!")
        viewModel.validateConfirmPassword()
        assertTrue(viewModel.isconfirmPassword)
        assertEquals("", viewModel.confirmPasswordErrMsg)
    }

    @Test
    fun validateConfirmPassword_nonMatchingPasswords_setsIsConfirmPasswordFalse() {
        viewModel.onPasswordInput("Abcd1234!")
        viewModel.onConfirmPasswordInput("Different123!")
        viewModel.validateConfirmPassword()
        assertFalse(viewModel.isconfirmPassword)
        assertEquals("As senhas não coincidem", viewModel.confirmPasswordErrMsg)
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
    fun onConfirmPasswordInput_updatesStateCorrectly() {
        viewModel.onConfirmPasswordInput("Abcd1234!")
        assertEquals("Abcd1234!", viewModel.state.confirmPassword)
    }


    @Test
    fun enabledSignupButton_true_whenAllValidationsPass() {
        viewModel.onEmailInput("test@example.com")
        viewModel.onPasswordInput("Abcd1234!")
        viewModel.onConfirmPasswordInput("Abcd1234!")

        viewModel.validateEmail()
        viewModel.validatePassword()
        viewModel.validateConfirmPassword()

        assertTrue(viewModel.isEnabledSignupButton
        )
    }

    @Test
    fun enabledSignupButton_false_whenOneValidationFails() {
        viewModel.onEmailInput("invalid-email")
        viewModel.onPasswordInput("Abcd1234!")
        viewModel.onConfirmPasswordInput("Abcd1234!")

        viewModel.validateEmail()
        viewModel.validatePassword()
        viewModel.validateConfirmPassword()

        assertFalse(viewModel.isEnabledSignupButton
        )
    }


    @Test
    fun signup_setsResponseToSuccess() = runTest {
        val user = User(email = "test@example.com", password = "Abcd1234!")
        coEvery { authUseCases.signup(any()) } returns Response.Success(mockUser)

        viewModel.signup(user)
        advanceUntilIdle()

        assertTrue(viewModel.signupResponse is Response.Success)
        assertEquals(mockUser, (viewModel.signupResponse as Response.Success).data)
    }


    @Test
    fun signup_setsResponseToFailure() = runTest {
        val user = User(email = "test@example.com", password = "Abcd1234!")
        val exception = Exception("Signup failed")
        coEvery { authUseCases.signup(any()) } returns Response.Failure(exception)

        viewModel.signup(user)
        advanceUntilIdle()

        assertTrue(viewModel.signupResponse is Response.Failure)
        assertEquals("Signup failed", (viewModel.signupResponse as Response.Failure).exception?.message)
    }
}
