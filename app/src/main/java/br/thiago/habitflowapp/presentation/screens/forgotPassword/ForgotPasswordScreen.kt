package br.thiago.habitflowapp.presentation.screens.forgotPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.presentation.components.AuthTextField
import br.thiago.habitflowapp.presentation.components.PrimaryButton

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    onSendLinkClick: () -> Unit,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {

    val state = viewModel.state

    LaunchedEffect(viewModel.forgotPasswordResponse) {
        when (val response = viewModel.forgotPasswordResponse) {
            is Response.Success ->  onSendLinkClick()
            is Response.Failure -> println("Erro: ${response.exception?.message}")
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
                .fillMaxHeight(0.50f)
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                        .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterStart)
                        .clickable(onClick = onBackClick)
                )
            }


            Text(
                text = "Recuperar Senha",
                fontSize = 24.sp,
                color = Color(0xFF0F172A),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Informe seu e-mail para receber o link de recuperação.",
                fontSize = 15.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )


            AuthTextField(
                value = state.email,
                onValueChange = { viewModel.onEmailInput(it) },
                placeholder = "E-mail",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                errorMsg = viewModel.emailErrMsg,
                validateField = { viewModel.validateEmail() }
            )


            PrimaryButton(
                text = "Enviar Link",
                onClick = { viewModel.sendForgotPasswordEmail() },
                enabled = viewModel.isEnabledSendButton,

            )


        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(
        onBackClick = {},
        onSendLinkClick = {}
    )
}
