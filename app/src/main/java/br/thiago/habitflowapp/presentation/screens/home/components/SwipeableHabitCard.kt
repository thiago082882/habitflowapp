package br.thiago.habitflowapp.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.thiago.habitflowapp.domain.model.Habit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableHabitCard(
    habit: Habit,
    onDelete: (Habit) -> Unit,
    content: @Composable () -> Unit
) {
    var dismissed by remember { mutableStateOf(false) }

    if (!dismissed) {
        val swipeState = rememberSwipeToDismissBoxState(
            confirmValueChange = { value ->
                if (value == SwipeToDismissBoxValue.EndToStart) {
                    onDelete(habit)
                    true
                } else {
                    false
                }
            }
        )

        SwipeToDismissBox(
            state = swipeState,
            modifier = Modifier.fillMaxWidth(),
            backgroundContent = {
                if (swipeState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(85.dp)
                            .background(Color(0xffe17b7b)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            modifier = Modifier.padding(end = 16.dp),
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remover hábito",
                            tint = Color.White
                        )
                    }
                }
            }
        ) {
            content()
        }
    }
}
