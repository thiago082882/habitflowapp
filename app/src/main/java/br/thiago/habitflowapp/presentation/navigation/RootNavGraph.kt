package br.thiago.habitflowapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavHostController
import br.thiago.habitflowapp.presentation.screens.habit_update.EditHabitScreen
import br.thiago.habitflowapp.presentation.screens.home.TodayScreen
import br.thiago.habitflowapp.presentation.screens.new_habit.AddHabitScreen

@Composable
fun RootNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ) {

        authNavGraph(navController = navController)

        composable(route = Graph.HOME) {
            TodayScreen(
                navController = navController,
                onToggleHabit = { habit ->

                },
                onAddHabitClick = {
                    navController.navigate(Screen.NewHabit.route)
                }
            )
        }


        composable(route = Screen.NewHabit.route) {
            AddHabitScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.UpdateHabit.route,
            arguments = listOf(navArgument("habitId") {
                type = NavType.StringType
            })
        ) {
            it.arguments?.getString("habitId")?.let {
                EditHabitScreen(
                    onBackClick = {
                        navController.popBackStack()

                    },
                )
            }
        }

    }

}


sealed class Screen(val route: String) {
    object NewHabit : Screen("habits/new")

    object UpdateHabit : Screen("habits/update/{habitId}") {
        fun passHabit(habitId: String) = "habits/update/$habitId"
    }
}
