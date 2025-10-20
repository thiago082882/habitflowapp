package br.thiago.habitflowapp.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.thiago.habitflowapp.presentation.components.AuthTextField
import br.thiago.habitflowapp.presentation.components.PrimaryButton
import br.thiago.habitflowapp.presentation.screens.login.component.ClickableTextLink
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.thiago.habitflowapp.domain.model.Response


@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(key1 = viewModel.loginResponse) {
        when (val response = viewModel.loginResponse) {
            is Response.Success -> onLoginClick()
            is Response.Failure -> println("Erro ao logar: ${response.exception?.message}")
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FA)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "HabitFlow",
                fontSize = 34.sp,
                color = Color(0xFF1ABC9C),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Acompanhe seu progresso.",
                fontSize = 15.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp),
                textAlign = TextAlign.Center
            )

            AuthTextField(
                value = state.email,
                onValueChange = { viewModel.onEmailInput(it) },
                placeholder = "E-mail",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                errorMsg = viewModel.emailErrMsg,
                validateField = {
                    viewModel.validateEmail()
                }
            )
            AuthTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordInput,
                placeholder = "Senha",
                icon = Icons.Default.Lock,
                isPassword = true,
                errorMsg = viewModel.passwordErrMsg,
                validateField = {
                    viewModel.validatePassword()
                }
            )

            PrimaryButton(
                text = "Entrar",
                onClick = {
                    viewModel.login()
                },
                enabled = viewModel.isEnabledLoginButton
            )

            Text(
                text = "Esqueci minha senha",
                color = Color(0xFF1ABC9C),
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable(onClick = onForgotPasswordClick)
            )

            ClickableTextLink(
                normalText = "Não tem conta?",
                clickableText = "Cadastre-se",
                onClick = onRegisterClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onLoginClick = {},
        onForgotPasswordClick = {},
        onRegisterClick = {}
    )
}
