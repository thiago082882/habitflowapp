package br.thiago.habitflowapp.presentation.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast

fun Context.initNotifications() {
    // 1️⃣ Criar canal de notificações (Android 8+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "habit_channel",
            "Lembretes de Hábito",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Canal para lembretes de hábitos"
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    // 2️⃣ Verificar e solicitar alarmes exatos (Android 12+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (!alarmManager.canScheduleExactAlarms()) {
            Toast.makeText(
                this,
                "Ative alarmes exatos nas configurações para lembretes precisos",
                Toast.LENGTH_LONG
            ).show()
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:$packageName")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
    }
}
