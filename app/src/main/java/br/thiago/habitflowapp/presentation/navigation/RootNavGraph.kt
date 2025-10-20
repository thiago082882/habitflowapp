package br.thiago.habitflowapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.thiago.habitflowapp.domain.model.FrequencyType
import br.thiago.habitflowapp.models.Habit1
import br.thiago.habitflowapp.presentation.screens.home.TodayScreen

@Composable
fun RootNavGraph(navController: NavHostController) {
    val habits = listOf(
        Habit1(
            name = "Beber Água (8 copos)",
            frequency = FrequencyType.DAILY,
            completed = true,
            streak = 12
        ),
        Habit1(
            name = "Estudar Programação (30 min)",
            frequency = FrequencyType.WEEKLY,
            completed = false,
            streak = 0
        ),
        Habit1(
            name = "Caminhada (10 mins)",
            frequency = FrequencyType.DAILY,
            completed = true,
            streak = 7
        )
    )


    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ) {

        authNavGraph(navController = navController)
        composable(route = Graph.HOME) {
            TodayScreen(
                habits = habits,
                onToggleHabit = {},
                onAddHabitClick = {}
            )

        }

    }

}
