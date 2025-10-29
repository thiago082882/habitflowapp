package br.thiago.habitflowapp.presentation.screens.habit_update

import androidx.lifecycle.SavedStateHandle
import br.thiago.habitflowapp.domain.model.FrequencyType
import br.thiago.habitflowapp.domain.model.Habit
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.repository.NotificationHandler
import br.thiago.habitflowapp.domain.use_cases.auth.AuthUseCases
import br.thiago.habitflowapp.domain.use_cases.habits.HabitsUseCases
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditHabitViewModelTest {

    private lateinit var viewModel: EditHabitViewModel
    private lateinit var habitsUseCases: HabitsUseCases
    private lateinit var authUseCases: AuthUseCases
    private lateinit var notificationHandler: NotificationHandler
    private lateinit var savedStateHandle: SavedStateHandle

    private val testDispatcher = StandardTestDispatcher()

    private val sampleHabit = Habit(
        id = "1",
        name = "Habit Test",
        description = "Desc",
        goal = "Goal",
        streak = 0,
        completed = false,
        frequency = FrequencyType.DAILY,
        reminderTime = "08:00",
        active = true,
        selectedDays = listOf(true, true, false, false, true, false, false),
        idUser = "user123",
        createdAt = System.currentTimeMillis()
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        habitsUseCases = mockk()
        notificationHandler = mockk(relaxed = true)
        savedStateHandle = mockk()
        authUseCases = mockk()

        val firebaseUser = mockk<FirebaseUser>(relaxed = true)
        every { firebaseUser.uid } returns "user123"
        every { authUseCases.getCurrentUser() } returns firebaseUser
        every { savedStateHandle.get<String>("habitId") } returns sampleHabit.toJson()

        viewModel = EditHabitViewModel(
            habitsUseCases = habitsUseCases,
            authUseCases = authUseCases,
            notificationHandler = notificationHandler,
            savedStateHandle = savedStateHandle
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state initialized correctly from habit`() {
        assertEquals(sampleHabit.name, viewModel.state.name)
        assertEquals(sampleHabit.description, viewModel.state.description)
        assertEquals(sampleHabit.goal, viewModel.state.goal)
        assertEquals(sampleHabit.frequency, viewModel.state.frequency)
        assertEquals(sampleHabit.reminderTime, viewModel.state.reminderTime)
        assertEquals(sampleHabit.active, viewModel.state.active)
        assertEquals(sampleHabit.selectedDays, viewModel.state.selectedDays)
    }

    @Test
    fun `onNameInput updates state`() {
        viewModel.onNameInput("New Name")
        assertEquals("New Name", viewModel.state.name)
    }

    @Test
    fun `toggleDay updates selectedDays`() {
        val initial = viewModel.state.selectedDays[0]
        viewModel.toggleDay(0)
        assertEquals(!initial, viewModel.state.selectedDays[0])
    }

    @Test
    fun `onUpdateHabit should call updateHabit and notificationHandler`() = runTest(testDispatcher) {
        coEvery { habitsUseCases.getHabitById(sampleHabit.id) } returns sampleHabit
        coEvery { habitsUseCases.updateHabit(any()) } returns Response.Success(true)

        viewModel.onUpdateHabit()
        advanceUntilIdle()

        coVerify { habitsUseCases.updateHabit(match { it.name == sampleHabit.name }) }
        coVerify { notificationHandler.cancelHabitNotification(sampleHabit.name) }
        coVerify { notificationHandler.scheduleHabitNotification(
            habitName = sampleHabit.name,
            habitGoal = sampleHabit.goal,
            reminderTime = sampleHabit.reminderTime,
            frequency = sampleHabit.frequency.name
        ) }
    }

    @Test
    fun `clearForm should reset updateHabitResponse`() {
        viewModel.updateHabitResponse = Response.Success(true)
        viewModel.clearForm()
        assertNull(viewModel.updateHabitResponse)
    }
}
