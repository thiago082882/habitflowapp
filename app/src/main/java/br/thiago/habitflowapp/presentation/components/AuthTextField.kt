package br.thiago.habitflowapp.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (value: String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    icon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    errorMsg: String = "",
    forgotPasswordMessage : String = "",
    validateField: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                validateField()
            },
            placeholder = { Text(placeholder, color = Color(0xFFBDBDBD)) },
            singleLine = true,
            leadingIcon = {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = Color(0xFF1ABC9C)
                    )
                }
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(0xFF1ABC9C),
                unfocusedIndicatorColor = Color(0xFFE0E0E0),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMsg.isNotEmpty()) {
            Text(
                text = errorMsg,
                fontSize = 12.sp,
                color = Color(0xFFD32F2F),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

    }
}
