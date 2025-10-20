package br.thiago.habitflowapp.data.repository


import br.thiago.habitflowapp.core.Constants
import br.thiago.habitflowapp.domain.model.Response
import br.thiago.habitflowapp.domain.model.User
import br.thiago.habitflowapp.domain.repository.UsersRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class UsersRepositoryImpl @Inject constructor(
    @Named(Constants.USERS) private val usersRef: CollectionReference,
    @Named(Constants.USERS) private val storageUsersRef: StorageReference,

    ) : UsersRepository {

    override suspend fun create(user: User): Response<Boolean> {

        return try {
            user.password = ""
            usersRef.document(user.id).set(user).await()
            Response.Success(true)

        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)

        }

    }

    override suspend fun update(user: User): Response<Boolean> {
        return try {
            val map: MutableMap<String, Any> = HashMap()
            map["username"] = user.username
            usersRef.document(user.id).update(map).await()
            Response.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)

        }
    }


    override fun getUserById(id: String): Flow<User> = callbackFlow {
        val snapshotListener = usersRef.document(id).addSnapshotListener { snapshot, e ->
            val user = snapshot?.toObject(User::class.java) ?: User()
            trySend(user)


        }
        awaitClose {
            snapshotListener.remove()
        }
    }
}