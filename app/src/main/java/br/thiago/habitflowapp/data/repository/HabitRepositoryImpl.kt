package br.thiago.habitflowapp.data.repository

import android.util.Log
import br.thiago.habitflowapp.core.Constants.HABIT
import br.thiago.habitflowapp.core.Constants.USERS
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.model.User
import br.thiago.habitflowapp.domain.repository.HabitRepository
import br.thiago.habitflowapp.domain.model.Habit
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class HabitRepositoryImpl @Inject constructor(

    @Named(HABIT) private val habitRef: CollectionReference,
    @Named(USERS) private val usersRef: CollectionReference,
    ) : HabitRepository {



    @OptIn(DelicateCoroutinesApi::class)
    override fun getHabits(): Flow<Response<List<Habit>>> = callbackFlow {
        val snapshotListener = habitRef.addSnapshotListener { snapshot, e ->

            launch(Dispatchers.IO) {
            val postsResponse = if (snapshot != null) {
                    val habits = snapshot.toObjects(Habit::class.java)

                    snapshot.documents.forEachIndexed { index, document ->
                        habits[index].id = document.id
                    }

                    val idUserArray = ArrayList<String>()

                    habits.forEach { habit ->
                        idUserArray.add(habit.idUser)
                    }

                    val idUserList = idUserArray.toSet().toList()

                    idUserList.map { id ->
                        async {
                            val user =
                                usersRef.document(id).get().await().toObject(User::class.java)!!
                            habits.forEach { post ->
                                if (post.idUser == id) {
                                    post.user = user
                                }
                            }

                        }
                    }.forEach {
                        it.await()
                    }

                    Response.Success(habits)
                } else {
//                    Response.Failure(e)
                Response.Failure(e as? Exception)
                }
                trySend(postsResponse)
            }

        }
        awaitClose {
            snapshotListener.remove()
        }

    }

    override fun getHabitsByUserId(idUser: String): Flow<Response<List<Habit>>> = callbackFlow {
        val snapshotListener =
            habitRef.whereEqualTo("idUser", idUser).addSnapshotListener { snapshot, e ->


                val habitsResponse = if (snapshot != null) {
                    val listHabit = snapshot.toObjects(Habit::class.java)
                    snapshot.documents.forEachIndexed{
                            index,document ->
                        listHabit[index].id = document.id

                    }


                    Response.Success(listHabit)
                } else {
                    Response.Failure(e)

                }
                trySend(habitsResponse)
            }



        awaitClose {
            snapshotListener.remove()
        }

    }
    override suspend fun create(habit: Habit): Response<Boolean> {
        return try {
            habitRef.add(habit).await()
            Response.Success(true)

        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }


    override suspend fun update(habit: Habit): Response<Boolean> {
        return try {
            val map: MutableMap<String, Any> = hashMapOf(
                "name" to habit.name,
                "description" to habit.description,
                "goal" to habit.goal,
                "streak" to habit.streak,
                "completed" to habit.completed,
                "frequency" to habit.frequency.name,
                "reminderTime" to habit.reminderTime,
                "active" to habit.active,
                "selectedDays" to habit.selectedDays,
                "idUser" to habit.idUser,
                "createdAt" to habit.createdAt
            )

            habitRef.document(habit.id).update(map).await()
            Response.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }

    override suspend fun delete(idHabit: String): Response<Boolean> {

        return try {
            habitRef.document(idHabit).delete().await()
            Response.Success(true)

        }catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }

    }

    override suspend fun updateHabit(habit: Habit): Response<Boolean> {
            return try {
                habitRef.document(habit.id).set(habit).await()
                Response.Success(true)
            } catch (e: Exception) {
                e.printStackTrace()
                Response.Failure(e)
            }


    }

    override suspend fun getHabitById(habitId: String): Habit? {
        return try {
            val documentSnapshot = habitRef.document(habitId).get().await()
            val habit = documentSnapshot.toObject(Habit::class.java)
            habit?.id = documentSnapshot.id
            habit
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}