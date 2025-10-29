package br.thiago.habitflowapp.data.repository

import android.content.Context
import br.thiago.habitflowapp.domain.repository.NotificationHandler
import br.thiago.habitflowapp.presentation.utils.NotificationScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NotificationHandler {

    override fun cancelHabitNotification(habitName: String) {

        NotificationScheduler.cancelHabitNotification(
            context, habitName
        )
    }

    override fun scheduleHabitNotification(
        habitName: String,
        habitGoal: String,
        reminderTime: String,
        frequency: String
    ) {
        NotificationScheduler.scheduleHabitNotification(
            context = context,
            habitName = habitName,
            habitGoal = habitGoal,
            reminderTime = reminderTime,
            frequency = frequency
        )
    }
}
