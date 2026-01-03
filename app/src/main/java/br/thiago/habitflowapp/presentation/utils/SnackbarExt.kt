package br.thiago.habitflowapp.presentation.utils


import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CoroutineScope.showSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String
) {
    launch {
        snackbarHostState.showSnackbar(message)
    }
}
