package br.thiago.habitflowapp.presentation.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.*

object NotificationScheduler {

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleHabitNotification(
        context: Context,
        habitName: String,
        habitGoal: String,
        reminderTime: String,
        frequency: String // "DAILY", "WEEKLY", "SPECIFIC"
    ) {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val parsedTime = sdf.parse(reminderTime) ?: return

        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            time = parsedTime
            set(Calendar.YEAR, now.get(Calendar.YEAR))
            set(Calendar.MONTH, now.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH))
            if (before(now)) add(Calendar.DAY_OF_MONTH, 1)
        }

        val intent = Intent(context, HabitReminderReceiver::class.java).apply {
            putExtra("habitName", habitName)
            putExtra("habitGoal", habitGoal)
            putExtra("reminderTime", reminderTime)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            habitName.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        when (frequency.uppercase(Locale.ROOT)) {
            "DAILY" -> alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                target.timeInMillis,
                pendingIntent
            )
            "WEEKLY" -> alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                target.timeInMillis,
                AlarmManager.INTERVAL_DAY * 7,
                pendingIntent
            )
            else -> alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                target.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelHabitNotification(context: Context, habitName: String) {
        val intent = Intent(context, HabitReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            habitName.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}
