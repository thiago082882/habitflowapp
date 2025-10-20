package br.thiago.habitflowapp.presentation.screens.new_habit.components

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import br.thiago.habitflowapp.presentation.components.HabitTextField
import br.thiago.habitflowapp.R
import br.thiago.habitflowapp.presentation.ui.theme.outlineVariantLight

@Composable
fun ReminderTimePicker(
    reminderTime: String,
    onTimeSelected: (String) -> Unit
) {
    val context = LocalContext.current

    fun showTimePicker() {
        val (hour, minute) = try {
            reminderTime.split(":").map { it.toInt() }
        } catch (_: Exception) {
            listOf(9, 0)
        }

        val dialog = TimePickerDialog(
            context,
            android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            { _: TimePicker, h: Int, m: Int ->
                val formatted = "%02d:%02d".format(h, m)
                onTimeSelected(formatted)
            },
            hour,
            minute,
            true
        )

        dialog.setOnShowListener {
            val picker = dialog.findViewById<TimePicker>(
                context.resources.getIdentifier("timePicker", "id", "android")
            )
            picker?.setIs24HourView(true)
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.show()
    }

    HabitTextField(
        value = TextFieldValue(reminderTime.ifBlank { "" }),
        onValueChange = {},
        label = "",
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showTimePicker() },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_time),
                contentDescription = "Selecionar horário",
                tint = outlineVariantLight,
                modifier = Modifier.clickable { showTimePicker() }
            )
        }
    )
}
