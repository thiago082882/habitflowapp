package br.thiago.habitflowapp.presentation.screens.login


import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.use_cases.auth.AuthUseCases
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

    private val authUseCases: AuthUseCases
): ViewModel() {

    // STATE FORM
    var state by mutableStateOf(LoginState())
        private set

    // EMAIL VALIDATIONS
    var isEmailValid by mutableStateOf(false)
        private set

    var emailErrMsg by mutableStateOf("")
        private set

    // PASSWORD VALIDATIONS
    var isPasswordValid by mutableStateOf(false)
        private set
    var passwordErrMsg by mutableStateOf("")
        private set

    // ENABLE BUTTON
    var isEnabledLoginButton by mutableStateOf(false)
        private set

    // LOGIN RESPONSE
    var loginResponse by mutableStateOf<Response<FirebaseUser>?>(null)


    val currentUser = authUseCases.getCurrentUser()

    init {
        if (currentUser != null) {
            loginResponse = Response.Success(currentUser)
        }
    }

    fun onEmailInput(email: String) {
        state = state.copy(email = email)
    }

    fun onPasswordInput(password: String) {
        state = state.copy(password = password)
    }

    fun login() = viewModelScope.launch {
        loginResponse = Response.Loading
        val result = authUseCases.login(state.email, state.password)
        loginResponse = result
    }

    fun enabledLoginButton() {
        isEnabledLoginButton = isEmailValid && isPasswordValid
    }

    fun validateEmail() {
        // É UM EMAIL VÁLIDO
        if (isValidEmail(state.email)) {
            isEmailValid = true
            emailErrMsg = ""
        }
        else {
            isEmailValid = false
            emailErrMsg = "O email não é valido"
        }

        enabledLoginButton()
    }

    fun validatePassword() {
        val password = state.password

        if (password.length >= 8) {
            isPasswordValid = true
            passwordErrMsg = ""
        } else {
            isPasswordValid = false
            passwordErrMsg = "Ao menos 8 caracteres"
        }

        enabledLoginButton()
    }




    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return Regex(emailRegex).matches(email)
    }


}