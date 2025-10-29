//package br.thiago.habitflowapp.presentation.screens.forgotPassword
//
//import android.util.Patterns
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import br.thiago.habitflowapp.domain.model.Response
//import br.thiago.habitflowapp.domain.use_cases.auth.AuthUseCases
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class ForgotPasswordViewModel @Inject constructor(
//    private val authUseCases: AuthUseCases
//) : ViewModel() {
//
//    // STATE FORM
//    var state by mutableStateOf(ForgotPasswordState())
//        private set
//
//    // EMAIL VALIDATION
//    var isEmailValid by mutableStateOf(false)
//        private set
//
//    var emailErrMsg by mutableStateOf("")
//        private set
//
//    // ENABLE BUTTON
//    var isEnabledSendButton by mutableStateOf(false)
//        private set
//
//    // RESPONSE
//    var forgotPasswordResponse by mutableStateOf<Response<String>?>(null)
//        private set
//
////     var forgotPasswordMessage by mutableStateOf("")
////      private set
//
//
//    fun onEmailInput(email: String) {
//        state = state.copy(email = email)
//        validateEmail()
//    }
//
//     fun validateEmail() {
//         if (isValidEmail(state.email)) {
//             isEmailValid = true
//             emailErrMsg = ""
//         }
//         else {
//             isEmailValid = false
//             emailErrMsg = "O email não é valido"
//         }
//        enabledSendButton()
//    }
//
//    private fun enabledSendButton() {
//        isEnabledSendButton = isEmailValid
//    }
//
//    fun sendForgotPasswordEmail() = viewModelScope.launch {
//        if (state.email.isNotBlank()) {
//            forgotPasswordResponse = Response.Loading
//            forgotPasswordResponse = authUseCases.forgotPassword(state.email)
//        } else {
//            forgotPasswordResponse = Response.Failure(Exception("Digite seu e-mail"))
//        }
//        enabledSendButton()
//    }
//    fun isValidEmail(email: String): Boolean {
//        // Regex leve e segura para validar formato de email
//        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
//        return Regex(emailRegex).matches(email)
//    }
//
//}

package br.thiago.habitflowapp.presentation.screens.forgotPassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.use_cases.auth.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    // STATE FORM
    var state by mutableStateOf(ForgotPasswordState())
        private set

    // EMAIL VALIDATION
    var isEmailValid by mutableStateOf(false)
        private set

    var emailErrMsg by mutableStateOf("")
        private set

    // ENABLE BUTTON
    var isEnabledSendButton by mutableStateOf(false)
        private set

    // RESPONSE
    var forgotPasswordResponse by mutableStateOf<Response<String>?>(null)
        private set

    // INPUT
    fun onEmailInput(email: String) {
        state = state.copy(email = email)
        validateEmail()
    }

    // VALIDATION
    fun validateEmail() {
        if (state.email.isBlank()) {
            isEmailValid = false
            emailErrMsg = "Digite seu e-mail"
        } else if (isValidEmail(state.email)) {
            isEmailValid = true
            emailErrMsg = ""
        } else {
            isEmailValid = false
            emailErrMsg = "O email não é válido"
        }
        updateSendButton()
    }

    private fun updateSendButton() {
        isEnabledSendButton = isEmailValid
    }

    // SEND EMAIL
    fun sendForgotPasswordEmail() = viewModelScope.launch {
        if (state.email.isNotBlank()) {
            forgotPasswordResponse = Response.Loading
            forgotPasswordResponse = authUseCases.forgotPassword(state.email)
        } else {
            forgotPasswordResponse = Response.Failure(Exception("Digite seu e-mail"))
        }
        updateSendButton()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return Regex(emailRegex).matches(email)
    }
}
