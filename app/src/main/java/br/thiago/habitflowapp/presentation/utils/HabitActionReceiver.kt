package br.thiago.habitflowapp.presentation.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HabitActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val habitName = intent.getStringExtra("habitName") ?: return

        when (intent.action) {
            HabitReminderReceiver.ACTION_SNOOZE -> {

                NotificationManagerCompat.from(context).cancel(habitName.hashCode())


                val snoozeTimeMillis = System.currentTimeMillis() + 10 * 60 * 1000
                val snoozeTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(snoozeTimeMillis))

                NotificationScheduler.scheduleHabitNotification(
                    context = context,
                    habitName = habitName,
                    habitGoal = "",
                    reminderTime = snoozeTime,
                    frequency = "SNOOZE"
                )
            }

            HabitReminderReceiver.ACTION_COMPLETE -> {

                NotificationManagerCompat.from(context).cancel(habitName.hashCode())

                NotificationScheduler.cancelHabitNotification(context, habitName)

            }
        }

    }
}
