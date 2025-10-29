package br.thiago.habitflowapp.presentation.screens.signUp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.model.User
import br.thiago.habitflowapp.domain.use_cases.auth.AuthUseCases
import br.thiago.habitflowapp.domain.use_cases.users.UsersUseCases
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val authUseCases: AuthUseCases, private val usersUseCases: UsersUseCases): ViewModel() {

    // STATE FORM
    var state by mutableStateOf(SignupState())
        private set


    var usernameErrMsg by mutableStateOf("")
        private set

    // EMAIL
    var isEmailValid by mutableStateOf(false)
        private set
    var emailErrMsg by mutableStateOf("")
        private set

    // PASSWORD
    var isPasswordValid by mutableStateOf(false)
        private set
    var passwordErrMsg by mutableStateOf("")
        private set

    // CONFIRMAR CONTRASENA
    var isconfirmPassword by mutableStateOf(false)
        private set
    var confirmPasswordErrMsg by mutableStateOf("")
        private set

    // ENABLE BUTTON
    var isEnabledSignupButton by mutableStateOf(false)
        private set

    var signupResponse by mutableStateOf<Response<FirebaseUser>?>(null)
        private set

    var user = User()

    fun onEmailInput(email: String) {
        state = state.copy(email = email)
    }


    fun onPasswordInput(password: String) {
        state = state.copy(password = password)
    }

    fun onConfirmPasswordInput(confirmPassword: String) {
        state = state.copy(confirmPassword = confirmPassword)
    }

    fun onSignup() {
        user.email = state.email
        user.password = state.password
        signup(user)
    }

    fun createUser() = viewModelScope.launch {
        user.id = authUseCases.getCurrentUser()!!.uid
        usersUseCases.create(user)
    }

    fun signup(user: User) = viewModelScope.launch {
        signupResponse = Response.Loading
        val result = authUseCases.signup(user)
        signupResponse = result
    }

    fun enabledLoginButton() {
        isEnabledSignupButton =
            isEmailValid &&
                    isPasswordValid &&
                    isconfirmPassword
    }


    fun validateEmail() {
        // É UM EMAIL VALIDO
        if (isValidEmail(state.email)) {
            isEmailValid = true
            emailErrMsg = ""
        }
        else {
            isEmailValid = false
            emailErrMsg = "O email não é válido"
        }

        enabledLoginButton()
    }

//    fun validatePassword() {
//        if (state.password.length >= 6) {
//            isPasswordValid = true
//            passwordErrMsg = ""
//        }
//        else {
//            isPasswordValid = false
//            passwordErrMsg = "Ao menos 6 caracteres"
//        }
//
//        enabledLoginButton()
//    }

    fun validateConfirmPassword() {
        if (state.password == state.confirmPassword) {
            isconfirmPassword = true
            confirmPasswordErrMsg = ""
        }
        else {
            isconfirmPassword = false
            confirmPasswordErrMsg = "As senhas não coincidem"
        }
        enabledLoginButton()
    }

    fun validatePassword() {
        val password = state.password

        val passwordRegex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!]).{8,}\$")

        if (passwordRegex.matches(password)) {
            isPasswordValid = true
            passwordErrMsg = ""
        } else {
            isPasswordValid = false
            passwordErrMsg = "Senha deve ter pelo menos 8 caracteres, incluir maiúscula, minúscula, número e caractere especial"
        }

        enabledLoginButton()
    }

     fun isValidEmail(email: String): Boolean {
        // Regex leve e segura para validar formato de email
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return Regex(emailRegex).matches(email)
    }



}