package br.thiago.habitflowapp.data.repository


import br.thiago.habitflowapp.domain.model.FrequencyType
import br.thiago.habitflowapp.domain.model.Habit
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.model.User
import br.thiago.habitflowapp.utils.mockTask
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class HabitRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var habitRef: CollectionReference
    private lateinit var usersRef: CollectionReference
    private lateinit var documentRef: DocumentReference
    private lateinit var repository: HabitRepositoryImpl

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        habitRef = mockk()
        usersRef = mockk()
        documentRef = mockk()
        repository = HabitRepositoryImpl(habitRef, usersRef)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getHabits should emit Success with populated users`() = runTest {
        val userId = "idUser"
        val user = User(id = userId, email = "user@gmail.com")
        val habit = Habit(
            id = "h1",
            idUser = userId,
            name = "Meditate",
            description = "Meditate for 10 minutes",
            goal = "10 minutes",
            streak = 0,
            completed = false,
            frequency = FrequencyType.DAILY,
            reminderTime = "07:00",
            active = true,
            selectedDays = listOf(true, false, true, false, true, false, true),
            user = null,
            createdAt = System.currentTimeMillis()
        )

        // Mock da resposta do Firestore de usuários
        val userSnap = mockk<DocumentSnapshot> {
            every { toObject(User::class.java) } returns user
        }

        val userDoc = mockk<DocumentReference>()
        every { usersRef.document(userId) } returns userDoc
        coEvery { userDoc.get() } returns mockTask(userSnap)

        // Mock do snapshot de hábitos
        val snapshot = mockk<QuerySnapshot>()
        val doc = mockk<DocumentSnapshot> { every { id } returns "h1" }

        every { snapshot.toObjects(Habit::class.java) } returns listOf(habit)
        every { snapshot.documents } returns listOf(doc)

        val registration = mockk<ListenerRegistration>(relaxed = true)
        val listenerSlot = slot<EventListener<QuerySnapshot>>()

        every { habitRef.addSnapshotListener(capture(listenerSlot)) } answers {
            listenerSlot.captured.onEvent(snapshot, null)
            registration
        }

        val result = repository.getHabits().first()

        assertTrue(result is Response.Success)
        val habits = (result as Response.Success).data
        assertEquals(1, habits.size)
        assertEquals("h1", habits[0].id)
        assertEquals("user@gmail.com", habits[0].user?.email)
    }

    
    @Test
    fun `getHabitsByUserId should emit success with users populated`() = runTest {
        val userId = "idUser"
        val user = User(id = userId, email = "user@gmail.com")
        val habit = Habit(
            id = "h1",
            idUser = userId,
            name = "Meditate",
            description = "Meditate for 10 minutes",
            goal = "10 minutes",
            streak = 0,
            completed = false,
            frequency = FrequencyType.DAILY,
            reminderTime = "07:00",
            active = true,
            selectedDays = listOf(true, false, true, false, true, false, true),
            user = user,
            createdAt = System.currentTimeMillis(),

            )

        // Mock do DocumentSnapshot do usuário
        val snap = mockk<DocumentSnapshot> {
            every { toObject(User::class.java) } returns user
        }

        every { documentRef.addSnapshotListener(any()) } answers {
            val listener = firstArg<EventListener<DocumentSnapshot>>()
            listener.onEvent(snap, null)
            mockk(relaxed = true)
        }

        every { usersRef.document(userId) } returns documentRef

        // Mock do QuerySnapshot dos hábitos
        val snapshot = mockk<QuerySnapshot>()

        val doc = mockk<DocumentSnapshot> { every { id } returns "h1" }

        every { snapshot.toObjects(Habit::class.java) } returns listOf(habit)

        every { snapshot.documents } returns listOf(doc)

        // Mock do Query e ListenerRegistration
        val query = mockk<com.google.firebase.firestore.Query>()
        val registration = mockk<ListenerRegistration>(relaxed = true)
        val listener = slot<EventListener<QuerySnapshot>>()


        every { habitRef.whereEqualTo("idUser", userId) } returns query
        every { query.addSnapshotListener(capture(listener)) } answers {
            listener.captured.onEvent(snapshot, null)
            registration
        }


        val result = repository.getHabitsByUserId(userId).first()

        assertTrue(result is Response.Success)
        val habits = (result as Response.Success).data
        assertEquals(1, habits.size)
        assertEquals("h1", habits[0].id)
        assertEquals("user@gmail.com", habits[0].user?.email)
    }


    @Test
    fun `getHabitsByUserId should emit success`() = runTest {
        val idUser = "u1"
        val habit = Habit(id = "h1", idUser = idUser, name = "Meditate")

        val snapshot = mockk<QuerySnapshot>()
        val doc = mockk<DocumentSnapshot> { every { id } returns "h1" }
        every { snapshot.toObjects(Habit::class.java) } returns listOf(habit)
        every { snapshot.documents } returns listOf(doc)

        val query = mockk<com.google.firebase.firestore.Query>()
        val registration = mockk<ListenerRegistration>(relaxed = true)
        val listener = slot<EventListener<QuerySnapshot>>()

        every { habitRef.whereEqualTo("idUser", idUser) } returns query
        every { query.addSnapshotListener(capture(listener)) } answers {
            listener.captured.onEvent(snapshot, null)
            registration
        }

        val result = repository.getHabitsByUserId(idUser).first()

        assertTrue(result is Response.Success)
        val habits = (result as Response.Success).data
        assertEquals(1, habits.size)
        assertEquals("h1", habits[0].id)
    }


    @Test
    fun `create should return success when add succeeds`() = runTest {
        val habit = Habit(id = "1", name = "Drink Water", idUser = "u1")
        val task: Task<DocumentReference> = mockTask(mockk())
        every { habitRef.add(habit) } returns task

        val result = repository.create(habit)

        assertTrue(result is Response.Success)
        assertEquals(true, (result as Response.Success).data)
    }

    @Test
    fun `create should return failure when add throws exception`() = runTest {
        val habit = Habit(id = "1", name = "Drink Water", idUser = "u1")
        val exception = Exception("Firestore add error")
        every { habitRef.add(habit) } throws exception

        val result = repository.create(habit)

        assertTrue(result is Response.Failure)
        assertEquals(exception, (result as Response.Failure).exception)
    }

    @Test
    fun `updateHabit should return success when set succeeds`() = runTest {
        val habit = Habit(id = "h1", name = "Read", idUser = "u1")

        every { habitRef.document(habit.id) } returns documentRef
        val task = mockTask<Void>(null)
        every { documentRef.set(habit) } returns task

        val result = repository.updateHabit(habit)

        assertTrue(result is Response.Success)
        assertEquals(true, (result as Response.Success).data)
    }


    @Test
    fun `update should return success and send correct map`() = runTest {
        val habit = Habit(
            id = "h1", name = "Exercise", description = "Morning workout",
            goal = "30 minutes", streak = 2, completed = true,
            frequency = FrequencyType.WEEKLY, reminderTime = "07:00",
            active = false, selectedDays = listOf(false, true, true, false, true, false, true),
            idUser = "u1"
        )

        every { habitRef.document(habit.id) } returns documentRef
        val task = mockTask<Void>(null)
        coEvery { documentRef.update(any<Map<String, Any>>()) } returns task

        val result = repository.update(habit)

        assertTrue(result is Response.Success)
        assertEquals(true, (result as Response.Success).data)

        coVerify {
            documentRef.update(match {
                it["name"] == habit.name &&
                        it["description"] == habit.description &&
                        it["goal"] == habit.goal &&
                        it["streak"] == habit.streak &&
                        it["completed"] == habit.completed &&
                        it["frequency"] == habit.frequency.name &&
                        it["reminderTime"] == habit.reminderTime &&
                        it["active"] == habit.active &&
                        it["selectedDays"] == habit.selectedDays &&
                        it["idUser"] == habit.idUser &&
                        it["createdAt"] == habit.createdAt
            })
        }
    }

    @Test
    fun `update should return failure when update throws exception`() = runTest {
        val habit = Habit(id = "h1", name = "Exercise", idUser = "u1")
        every { habitRef.document(habit.id) } returns documentRef
        val exception = Exception("Firestore update error")
        coEvery { documentRef.update(any<Map<String, Any>>()) } throws exception

        val result = repository.update(habit)

        assertTrue(result is Response.Failure)
        assertEquals(exception, (result as Response.Failure).exception)
    }


    @Test
    fun `delete should return success when delete succeeds`() = runTest {
        val habitId = "h1"
        every { habitRef.document(habitId) } returns documentRef
        val task = mockTask<Void>(null)
        coEvery { documentRef.delete() } returns task

        val result = repository.delete(habitId)

        assertTrue(result is Response.Success)
        assertEquals(true, (result as Response.Success).data)
    }

    @Test
    fun `delete should return failure when delete throws exception`() = runTest {
        val habitId = "h1"
        every { habitRef.document(habitId) } returns documentRef
        val exception = Exception("Firestore delete error")
        coEvery { documentRef.delete() } throws exception

        val result = repository.delete(habitId)

        assertTrue(result is Response.Failure)
        assertEquals(exception, (result as Response.Failure).exception)
    }

    @Test
    fun `getHabitById should return habit`() = runTest {
        val habit = Habit(id = "h1", name = "Exercise")
        val docSnapshot = mockk<DocumentSnapshot>()
        every { habitRef.document("h1") } returns documentRef
        every { documentRef.get() } returns mockTask(docSnapshot)
        every { docSnapshot.toObject(Habit::class.java) } returns habit
        every { docSnapshot.id } returns "h1"

        val result = repository.getHabitById("h1")
        assertNotNull(result)
        assertEquals("h1", result?.id)
    }

    @Test
    fun `getHabitById should return null on exception`() = runTest {
        every { habitRef.document("h1") } returns documentRef
        val exception = Exception("Firestore error")
        every { documentRef.get() } throws exception

        val result = repository.getHabitById("h1")
        assertNull(result)
    }
}
