package br.thiago.habitflowapp.data.repository

import br.thiago.habitflowapp.utils.mockTask
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.model.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import io.mockk.coEvery
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class UsersRepositoryImplTest {


    private val testDispatcher = StandardTestDispatcher()


    private lateinit var usersRef: CollectionReference
    private lateinit var repository: UsersRepositoryImpl
    private lateinit var documentRef: DocumentReference

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        usersRef = mockk()
        documentRef = mockk()
        repository = UsersRepositoryImpl(usersRef)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun `create should return success when set succeeds`() = runTest {
        val user = User(id = "123", email = "test@example.com", password = "123456")

        // Mock do DocumentReference
        every { usersRef.document(user.id) } returns documentRef


        val task = mockTask<Void>(null)
        every { documentRef.set(any()) } returns task

        val result = repository.create(user)

        assertTrue(result is Response.Success)
        assertEquals(true, (result as Response.Success).data)
        assertEquals("", user.password)
    }


    @Test
    fun `create should return failure when set throws exception`() = runTest {
        val user = User(id = "123", email = "test@example.com", password = "123456")
        val exception = Exception("Firestore error")

        every { usersRef.document(user.id) } returns documentRef
        coEvery { documentRef.set(any()) } throws exception

        val result = repository.create(user)

        assertTrue(result is Response.Failure)
        assertEquals(exception, (result as Response.Failure).exception)
    }
    @Test
    fun `getUserById should emit user from snapshot`() = runTest {
        val userId = "u1"
        val user = User(id = userId, email = "user1@gmail.com")

        // Mock do DocumentSnapshot
        val snapshot = mockk<DocumentSnapshot> {
            every { toObject(User::class.java) } returns user
        }

        // Usa o documentRef global
        every { documentRef.addSnapshotListener(any()) } answers {
            val listener = firstArg<EventListener<DocumentSnapshot>>()
            listener.onEvent(snapshot, null)
            mockk(relaxed = true)
        }

        every { usersRef.document(userId) } returns documentRef

        val emittedUser = repository.getUserById(userId).first()

        assertEquals(user, emittedUser)
    }




}


//package br.thiago.habitflowapp.data.repository
//
//import br.thiago.habitflowapp.data.utils.mockVoidTask
//import br.thiago.habitflowapp.data.utils.mockTask
//import br.thiago.habitflowapp.domain.model.Response
//import br.thiago.habitflowapp.domain.model.User
//import com.google.firebase.firestore.CollectionReference
//import com.google.firebase.firestore.DocumentReference
//import io.mockk.every
//import io.mockk.mockk
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.runTest
//import org.junit.Assert.assertEquals
//import org.junit.Assert.assertTrue
//import org.junit.Before
//import org.junit.Test
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class UsersRepositoryImplTest {
//
//    private lateinit var usersRef: CollectionReference
//    private lateinit var documentRef: DocumentReference
//    private lateinit var repository: UsersRepositoryImpl
//
//    @Before
//    fun setUp() {
//        usersRef = mockk()
//        documentRef = mockk()
//        repository = UsersRepositoryImpl(usersRef)
//    }
//
//    @Test
//    fun `create should return success when set succeeds`() = runTest {
//        val user = User(id = "123", email = "test@example.com", password = "123456")
//
//        every { usersRef.document(user.id) } returns documentRef
//        every { documentRef.set(any()) } returns mockVoidTask()
//
//        val result = repository.create(user)
//
//        assertTrue(result is Response.Success)
//        assertEquals(true, (result as Response.Success).data)
//        assertEquals("", user.password)
//    }
//
//    @Test
//    fun `create should return failure when set fails`() = runTest {
//        val user = User(id = "123", email = "test@example.com", password = "123456")
//        val exception = Exception("Firestore simulated error")
//
//        every { usersRef.document(user.id) } returns documentRef
//        every { documentRef.set(any()) } returns mockTask<Void>(null, exception)
//
//        val result = repository.create(user)
//
//        assertTrue(result is Response.Failure)
//        assertEquals(exception, (result as Response.Failure).exception)
//    }
//}
