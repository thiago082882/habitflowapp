package br.thiago.habitflowapp.presentation.screens.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.thiago.habitflowapp.domain.model.Habit
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.use_cases.auth.AuthUseCases
import br.thiago.habitflowapp.domain.use_cases.habits.HabitsUseCases
import br.thiago.habitflowapp.presentation.screens.home.components.HomeState
import br.thiago.habitflowapp.presentation.utils.NotificationScheduler.cancelHabitNotification
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HabitsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val habitsUseCases: HabitsUseCases,
    private val authUseCases: AuthUseCases,
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set


    var welcomeMessage by mutableStateOf("")


    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig


    private val currentUser = authUseCases.getCurrentUser()
    private val localHabitsState = mutableMapOf<String, Boolean>()

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        fetchWelcomeMessage()

        getHabits()
    }

    fun getHabits() = viewModelScope.launch {
        state = state.copy(habitsResponse = Response.Loading)

        habitsUseCases.getHabitsByIdUser(currentUser?.uid ?: "").collect { response ->
            val habits = (response as? Response.Success)?.data ?: emptyList()
            val completed = habits.count { it.completed }
            val total = habits.size
            val progress = if (total > 0) (completed * 100) / total else 0

            state = state.copy(
                habitsResponse = response,
                completedHabits = completed,
                totalHabits = total,
                progressPercent = progress
            )
        }
    }



    fun toggleHabit(habit: Habit) = viewModelScope.launch {
        val currentHabits = (state.habitsResponse as? Response.Success)?.data ?: emptyList()

        val updatedHabits = currentHabits.map {
            if (it.id == habit.id) {
                val newCompleted = !it.completed
                localHabitsState[it.id] = newCompleted
                it.copy(completed = newCompleted)
            } else it
        }

        val completed = updatedHabits.count { it.completed }
        val total = updatedHabits.size
        val progress = if (total > 0) (completed * 100) / total else 0

        state = state.copy(
            habitsResponse = Response.Success(updatedHabits),
            completedHabits = completed,
            totalHabits = total,
            progressPercent = progress
        )

        val newCompleted = !habit.completed

        // 🔔 Cancela notificação se o hábito foi concluído
        if (newCompleted) {
            cancelHabitNotification(context = context,habit.name)
        }

        val result = habitsUseCases.toggleHabitCompletion(habit)

        if (result is Response.Failure<*>) {

            state = state.copy(habitsResponse = Response.Success(currentHabits))
        }
    }


    private fun fetchWelcomeMessage() = viewModelScope.launch {
        try {
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val message = remoteConfig.getString("mensagem_boas_vindas")

                   if (message.isNotEmpty()) {

                        welcomeMessage = message

                        viewModelScope.launch {
                            delay(5000)
                            welcomeMessage = ""
                        }
                    }
                }
            }
        } catch (e: Exception) {
           e.printStackTrace()
        }
    }


    fun deleteHabit(habitId: String) = viewModelScope.launch {
        state = state.copy(deleteResponse = Response.Loading)
        val result = habitsUseCases.deleteHabit(habitId)
        state = state.copy(deleteResponse = result)
    }

    fun logout() {
        authUseCases.logout()
    }
}
