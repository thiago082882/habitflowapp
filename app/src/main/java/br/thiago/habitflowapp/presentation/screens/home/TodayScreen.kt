
package br.thiago.habitflowapp.presentation.screens.home


import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.thiago.habitflowapp.domain.model.Habit
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.presentation.components.WelcomeDialog
import br.thiago.habitflowapp.presentation.navigation.AuthScreen.Login
import br.thiago.habitflowapp.presentation.navigation.Screen
import br.thiago.habitflowapp.presentation.screens.home.components.HabitCard
import br.thiago.habitflowapp.presentation.screens.home.components.ProgressCard
import br.thiago.habitflowapp.presentation.screens.home.components.SwipeableHabitCard
import br.thiago.habitflowapp.presentation.screens.home.components.TodayHeader
import br.thiago.habitflowapp.presentation.ui.theme.primaryContainerLight
import br.thiago.habitflowapp.presentation.ui.theme.primaryLight
import br.thiago.habitflowapp.presentation.utils.RequestNotificationPermission

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    navController: NavHostController,
    onToggleHabit: (Habit) -> Unit = {},
    onAddHabitClick: () -> Unit = {},
    viewModel: HabitsViewModel = hiltViewModel(),
) {
    val activity = LocalContext.current as? Activity

    RequestNotificationPermission()

    val state = viewModel.state

    val habits = (state.habitsResponse as? Response.Success)?.data ?: emptyList()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddHabitClick,
                containerColor = primaryContainerLight,
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Hábito", tint = Color.White)
            }
        }
    ) { paddingValues ->



        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                if (viewModel.welcomeMessage.isNotEmpty()) {
                    WelcomeDialog(
                        message = viewModel.welcomeMessage,
                        onDismiss = { viewModel.welcomeMessage = "" }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                TodayHeader(
                    onLogoutClick = {
//                        viewModel.logout()
//                        activity?.finish()
//                        activity?.startActivity(Intent(activity, MainActivity::class.java))

                        viewModel.logout()
                        navController.navigate(Login.route) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }

                    }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            item {
                ProgressCard(
                    title = "Progresso do Dia",
                    progressPercent = state.progressPercent,
                    completed = state.completedHabits,
                    total = state.totalHabits,
                    containerColor = primaryContainerLight.copy(alpha = 0.4f),
                    accentColor = primaryLight
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Meus Hábitos",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            items(habits, key = { it.id }) { habit ->
                SwipeableHabitCard(
                    habit = habit,
                    onDelete = { viewModel.deleteHabit(it.id) }
                ) {
                    HabitCard(
                        title = habit.name,
                        subtitle = habit.description,
                        completed = habit.completed,
                        borderColor = if (habit.completed) Color(0xFF4CAF50) else Color(0xFFF1C40F),
                        onClick = { navController.navigate(Screen.UpdateHabit.passHabit(habit.toJson())) },
                        onToggleClick = { viewModel.toggleHabit(habit) },
                        onLongClick = { viewModel.deleteHabit(habit.id) }
                    )
                }
            }



            item {
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Arraste para baixo para atualizar.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                }
            }
        }
    }
}

