package br.thiago.habitflowapp.presentation.components


import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.thiago.habitflowapp.presentation.ui.theme.onBackgroundLight
import br.thiago.habitflowapp.presentation.ui.theme.outlineLight
import br.thiago.habitflowapp.presentation.ui.theme.primaryContainerLight


@Composable
fun HabitTextField(
    value: String,
    onValueChange: (value: String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = primaryContainerLight,
            unfocusedBorderColor = outlineLight,
            focusedTextColor = onBackgroundLight,
            unfocusedTextColor = onBackgroundLight,
            focusedLabelColor = primaryContainerLight,
            unfocusedLabelColor = outlineLight
        )
    )
}


@Preview(showBackground = true)
@Composable
private fun HabitTextFieldPreview() {
    HabitTextField(
        value = "",
        onValueChange = {},
        label = "Nome do Hábito"

    )

}