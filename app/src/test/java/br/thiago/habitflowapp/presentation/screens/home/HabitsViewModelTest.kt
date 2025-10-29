package br.thiago.habitflowapp.presentation.screens.home

import android.content.Context
import br.thiago.habitflowapp.domain.model.Habit
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.repository.NotificationHandler
import br.thiago.habitflowapp.domain.use_cases.auth.AuthUseCases
import br.thiago.habitflowapp.domain.use_cases.habits.GetHabitsByIdUser
import br.thiago.habitflowapp.domain.use_cases.habits.HabitsUseCases
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class HabitsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var habitsUseCases: HabitsUseCases
    private lateinit var authUseCases: AuthUseCases
    private lateinit var context: Context
    private lateinit var viewModel: HabitsViewModel
    private lateinit var notificationHandler: NotificationHandler

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)


        val mockGetHabitsByIdUser = mockk<GetHabitsByIdUser>()
        coEvery { mockGetHabitsByIdUser.invoke("user123") } returns flowOf(
            Response.Success(emptyList())
        )


        habitsUseCases = mockk {
            every { getHabitsByIdUser } returns mockGetHabitsByIdUser
            coEvery { toggleHabitCompletion(any()) } returns Response.Success(true)
            coEvery { deleteHabit(any()) } returns Response.Success(true)
        }

        authUseCases = mockk()

        context = mockk(relaxed = true)

        val mockRemoteConfig = mockk<FirebaseRemoteConfig>(relaxed = true)

        notificationHandler = mockk(relaxed = true)


        every { authUseCases.getCurrentUser() } returns mockk {
            every { uid } returns "user123"
        }

        viewModel = HabitsViewModel(
            context = context,
            habitsUseCases = habitsUseCases,
            authUseCases = authUseCases,
            remoteConfig = mockRemoteConfig,
            notificationHandler = notificationHandler
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getHabits should update state with habits`() = runTest {
        val habitList = listOf(
            Habit(id = "1", name = "Test Habit 1", completed = false),
            Habit(id = "2", name = "Test Habit 2", completed = true)
        )


        coEvery { habitsUseCases.getHabitsByIdUser.invoke("user123") } returns flow {
            emit(Response.Success(habitList))
        }

        viewModel.getHabits()
        advanceUntilIdle()

        val state = viewModel.state
        assertEquals(2, state.totalHabits)
        assertEquals(1, state.completedHabits)
        assertEquals(50, state.progressPercent)
        assert(state.habitsResponse is Response.Success)
    }

    @Test
    fun `toggleHabit should toggle completion and update state`() = runTest {
        val habit = Habit(id = "1", name = "Test Habit", completed = false)
        viewModel.state = viewModel.state.copy(
            habitsResponse = Response.Success(listOf(habit))
        )

        viewModel.toggleHabit(habit)
        advanceUntilIdle()

        val updatedHabit = (viewModel.state.habitsResponse as Response.Success).data.first()
        assertEquals(true, updatedHabit.completed)
        assertEquals(100, viewModel.state.progressPercent)


        verify { notificationHandler.cancelHabitNotification("Test Habit") }
    }

    @Test
    fun `deleteHabit should call use case and update state`() = runTest {
        viewModel.deleteHabit("1")
        advanceUntilIdle()

        assert(viewModel.state.deleteResponse is Response.Success)
    }

    @Test
    fun `logout should call authUseCases logout`() {
        every { authUseCases.logout() } just Runs

        viewModel.logout()

        verify {
            authUseCases.logout()
        }
    }
}
