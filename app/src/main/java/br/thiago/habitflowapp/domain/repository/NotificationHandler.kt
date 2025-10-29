package br.thiago.habitflowapp.domain.repository

interface NotificationHandler {
    fun cancelHabitNotification(habitName: String)
    fun scheduleHabitNotification(
        habitName: String,
        habitGoal: String,
        reminderTime: String,
        frequency: String
    )
}
