package br.thiago.habitflowapp.presentation.screens.new_habit.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import br.thiago.habitflowapp.presentation.ui.theme.backgroundLight
import br.thiago.habitflowapp.presentation.ui.theme.primaryContainerLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    title: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {

            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = backgroundLight,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = backgroundLight
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = primaryContainerLight
        )
    )
}
