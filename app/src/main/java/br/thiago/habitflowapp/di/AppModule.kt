package br.thiago.habitflowapp.di

import br.thiago.habitflowapp.core.Constants.HABIT
import br.thiago.habitflowapp.core.Constants.USERS
import br.thiago.habitflowapp.data.repository.AuthRepositoryImpl
import br.thiago.habitflowapp.data.repository.HabitRepositoryImpl
import br.thiago.habitflowapp.data.repository.UsersRepositoryImpl
import br.thiago.habitflowapp.domain.repository.AuthRepository
import br.thiago.habitflowapp.domain.repository.HabitRepository
import br.thiago.habitflowapp.domain.repository.UsersRepository
import br.thiago.habitflowapp.domain.use_cases.auth.AuthUseCases
import br.thiago.habitflowapp.domain.use_cases.auth.ForgotPassword
import br.thiago.habitflowapp.domain.use_cases.auth.GetCurrentUser
import br.thiago.habitflowapp.domain.use_cases.auth.Login
import br.thiago.habitflowapp.domain.use_cases.auth.Logout
import br.thiago.habitflowapp.domain.use_cases.auth.Signup
import br.thiago.habitflowapp.domain.use_cases.habits.CreateHabit
import br.thiago.habitflowapp.domain.use_cases.habits.DeleteHabit
import br.thiago.habitflowapp.domain.use_cases.habits.GetHabitById
import br.thiago.habitflowapp.domain.use_cases.habits.GetHabits
import br.thiago.habitflowapp.domain.use_cases.habits.GetHabitsByIdUser
import br.thiago.habitflowapp.domain.use_cases.habits.HabitsUseCases
import br.thiago.habitflowapp.domain.use_cases.habits.ToggleHabitUseCase
import br.thiago.habitflowapp.domain.use_cases.habits.UpdateHabit
import br.thiago.habitflowapp.domain.use_cases.users.Create
import br.thiago.habitflowapp.domain.use_cases.users.GetUserById
import br.thiago.habitflowapp.domain.use_cases.users.UsersUseCases
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Qualifier
import javax.inject.Named
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = Firebase.remoteConfig



//    @Provides
//    @Singleton
//    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Named(USERS)
    fun provideStorageUsersRef(storage: FirebaseStorage): StorageReference = storage.reference.child(USERS)

    @Provides
    @Named(USERS)
    fun provideUsersRef(db: FirebaseFirestore): CollectionReference = db.collection(USERS)

    @Provides
    @Named(HABIT)
    fun provideStorageHabitsRef(storage: FirebaseStorage): StorageReference = storage.reference.child(HABIT)

    @Provides
    @Named(HABIT)
    fun provideHabitsRef(db: FirebaseFirestore): CollectionReference = db.collection(HABIT)

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    fun provideUsersRepository(impl: UsersRepositoryImpl): UsersRepository = impl

    @Provides
    fun provideHabitsRepository(impl: HabitRepositoryImpl): HabitRepository = impl

    @Provides
    fun provideAuthUseCases(repository: AuthRepository) = AuthUseCases(
        getCurrentUser = GetCurrentUser(repository),
        login = Login(repository),
        logout = Logout(repository),
        signup = Signup(repository),
        forgotPassword = ForgotPassword(repository)
    )

    @Provides
    fun provideUsersUseCases(repository: UsersRepository) = UsersUseCases(
        create = Create(repository),
        getUserById = GetUserById(repository),

    )

    @Provides
    fun provideHabitsUseCases(repository: HabitRepository) =HabitsUseCases(
        create = CreateHabit(repository),
        getHabits = GetHabits(repository),
        getHabitsByIdUser = GetHabitsByIdUser(repository),
        deleteHabit = DeleteHabit(repository),
        updateHabit = UpdateHabit(repository),
        toggleHabitCompletion = ToggleHabitUseCase(repository),
        getHabitById = GetHabitById(repository)

    )

}