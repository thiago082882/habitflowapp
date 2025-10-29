package br.thiago.habitflowapp.data.repository

import android.app.AlarmManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import br.thiago.habitflowapp.presentation.utils.NotificationScheduler
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [34])
class NotificationHandlerImplTest {

    private lateinit var context: Context
    private lateinit var handler: NotificationHandlerImpl

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        handler = NotificationHandlerImpl(context)


        mockkObject(NotificationScheduler)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `scheduleHabitNotification should set an alarm`() {
        val habitName = "Drink Water"
        val habitGoal = "8 glasses"
        val reminderTime = "08:00"
        val frequency = "daily"


        handler.scheduleHabitNotification(habitName, habitGoal, reminderTime, frequency)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val shadowAlarmManager = Shadows.shadowOf(alarmManager)


        val pendingIntents = shadowAlarmManager.scheduledAlarms
        assertTrue(pendingIntents.isNotEmpty())


        val pendingIntent = pendingIntents.first().operation
        val shadowPendingIntent = Shadows.shadowOf(pendingIntent)
        val intent = shadowPendingIntent.savedIntent

        assertEquals(habitName, intent.getStringExtra("habitName"))
        assertEquals(habitGoal, intent.getStringExtra("habitGoal"))
    }

    @Test
    fun `cancelHabitNotification should cancel an alarm`() {
        val habitName = "Drink Water"


        handler.scheduleHabitNotification(habitName, "8 glasses", "08:00", "daily")
        handler.cancelHabitNotification(habitName)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val shadowAlarmManager = Shadows.shadowOf(alarmManager)

        val pendingIntents = shadowAlarmManager.scheduledAlarms
        val exists = pendingIntents.any {
            val shadowPendingIntent = Shadows.shadowOf(it.operation)
            val intent = shadowPendingIntent.savedIntent
            intent.getStringExtra("habitName") == habitName
        }
        assertFalse(exists)
    }

}
