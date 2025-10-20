package br.thiago.habitflowapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.thiago.habitflowapp.presentation.navigation.RootNavGraph
import br.thiago.habitflowapp.presentation.ui.theme.HabitFlowAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitFlowAppTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    navController = rememberNavController()
                    RootNavGraph(navController = navController)

                }
        }
//            HabitFlowAppTheme {
//                val habits = listOf(
//                    Habit1(
//                        name = "Beber Água (8 copos)",
//                        frequency = FrequencyType.DAILY,
//                        completed = true,
//                        streak = 12
//                    ),
//                    Habit1(
//                        name = "Estudar Programação (30 min)",
//                        frequency = FrequencyType.WEEKLY,
//                        completed = false,
//                        streak = 0
//                    ),
//                    Habit1(
//                        name = "Caminhada (10 mins)",
//                        frequency = FrequencyType.DAILY,
//                        completed = true,
//                        streak = 7
//                    )
//                )
//
//
//                TodayScreen(
//                    habits = habits,
//                    onToggleHabit = {},
//                    onAddHabitClick = {}
//                )
//            }
        }
    }
}

