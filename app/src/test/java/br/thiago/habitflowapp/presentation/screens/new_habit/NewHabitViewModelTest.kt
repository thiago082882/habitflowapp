package br.thiago.habitflowapp.presentation.screens.new_habit

import br.thiago.habitflowapp.domain.model.FrequencyType
import br.thiago.habitflowapp.domain.model.Habit
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.repository.NotificationHandler
import br.thiago.habitflowapp.domain.use_cases.auth.AuthUseCases
import br.thiago.habitflowapp.domain.use_cases.habits.HabitsUseCases
import com.google.firebase.auth.FirebaseUser
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class NewHabitViewModelTest {

    private lateinit var viewModel: NewHabitViewModel
    private lateinit var habitsUseCases: HabitsUseCases
    private lateinit var authUseCases: AuthUseCases
    private lateinit var notificationHandler: NotificationHandler

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        habitsUseCases = mockk()
        notificationHandler = mockk(relaxed = true)

        val firebaseUser = mockk<FirebaseUser>(relaxed = true)
        every { firebaseUser.uid } returns "user123"

        authUseCases = mockk()
        every { authUseCases.getCurrentUser() } returns firebaseUser

        viewModel = NewHabitViewModel(habitsUseCases, authUseCases, notificationHandler)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `input functions should update state`() {
        viewModel.onNameInput("Habit Name")
        viewModel.onDescriptionInput("Description")
        viewModel.onGoalInput("Goal")
        viewModel.onFrequencyInput(FrequencyType.WEEKLY)

        assertEquals("Habit Name", viewModel.state.name)
        assertEquals("Description", viewModel.state.description)
        assertEquals("Goal", viewModel.state.goal)
        assertEquals(FrequencyType.WEEKLY, viewModel.state.frequency)
    }

    @Test
    fun `toggleDay should change selectedDays`() {
        val index = 0
        val initial = viewModel.state.selectedDays[index]
        viewModel.toggleDay(index)
        assertEquals(!initial, viewModel.state.selectedDays[index])
    }

    @Test
    fun `createHabit should fail if form is invalid`() = runTest(testDispatcher) {
        // Form vazio
        viewModel.createHabit()
        advanceUntilIdle()

        val response = viewModel.createHabitResponse
        assertTrue(response is Response.Failure)
    }

    @Test
    fun `createHabit should call habitsUseCases and schedule notification when active`() = runTest(testDispatcher) {

        viewModel.onNameInput("New Habit")
        viewModel.onDescriptionInput("Desc")
        viewModel.onGoalInput("Goal")
        viewModel.onFrequencyInput(FrequencyType.DAILY)
        viewModel.onReminderTimeInput("08:00")
        viewModel.onReminderToggle(true)

        // Mock do create
        coEvery { habitsUseCases.create(any()) } returns Response.Success(true)

        viewModel.createHabit()
        advanceUntilIdle()

        coVerify { habitsUseCases.create(match { it.name == "New Habit" }) }
        coVerify { notificationHandler.scheduleHabitNotification(any(), any(), any(), any()) }

        val response = viewModel.createHabitResponse
        assertTrue(response is Response.Success)
        assertEquals(true, (response as Response.Success).data)
    }

    @Test
    fun `createHabit should cancel notification if not active`() = runTest(testDispatcher) {
        // Preenche o formulário
        viewModel.onNameInput("Inactive Habit")
        viewModel.onDescriptionInput("Desc")
        viewModel.onGoalInput("Goal")
        viewModel.onFrequencyInput(FrequencyType.DAILY)
        viewModel.onReminderTimeInput("08:00")
        viewModel.onReminderToggle(false)

        coEvery { habitsUseCases.create(any()) } returns Response.Success(true)

        viewModel.createHabit()
        advanceUntilIdle()

        coVerify { notificationHandler.cancelHabitNotification("Inactive Habit") }
        coVerify { habitsUseCases.create(match { it.name == "Inactive Habit" }) }
    }

    @Test
    fun `clearForm should reset state and response`() {
        viewModel.onNameInput("Habit")
        viewModel.createHabitResponse = Response.Loading

        viewModel.clearForm()

        assertEquals("", viewModel.state.name)
        assertNull(viewModel.createHabitResponse)
    }
}
