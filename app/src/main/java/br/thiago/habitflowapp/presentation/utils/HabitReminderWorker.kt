package br.thiago.habitflowapp.presentation.utils

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import br.thiago.habitflowapp.R
import br.thiago.habitflowapp.MainActivity

class HabitReminderReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_SNOOZE = "ACTION_SNOOZE"
        const val ACTION_COMPLETE = "ACTION_COMPLETE"
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        val habitName = intent.getStringExtra("habitName") ?: return
        val habitGoal = intent.getStringExtra("habitGoal") ?: ""
        val reminderTime = intent.getStringExtra("reminderTime") ?: ""


        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val snoozeIntent = Intent(context, HabitActionReceiver::class.java).apply {
            action = ACTION_SNOOZE
            putExtra("habitName", habitName)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            habitName.hashCode(),
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val completeIntent = Intent(context, HabitActionReceiver::class.java).apply {
            action = ACTION_COMPLETE
            putExtra("habitName", habitName)
        }
        val completePendingIntent = PendingIntent.getBroadcast(
            context,
            habitName.hashCode() + 1,
            completeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "habit_channel")
            .setSmallIcon(R.drawable.ic_time)
            .setContentTitle("✨ Lembrete de Hábito")
            .setContentText("Está na hora de $habitName! ⏰")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Meta: $habitGoal\nHorário: $reminderTime\n\nMantenha sua rotina e continue evoluindo! 💪")
            )
            .setColor(Color.parseColor("#6C63FF"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_snooze, "Adiar 10 min", snoozePendingIntent)
            .addAction(R.drawable.ic_check, "Concluir", completePendingIntent)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()

        NotificationManagerCompat.from(context).notify(habitName.hashCode(), notification)
    }
}
