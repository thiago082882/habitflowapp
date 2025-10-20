package br.thiago.habitflowapp.di

import br.thiago.habitflowapp.core.Constants.USERS
import br.thiago.habitflowapp.data.repository.AuthRepositoryImpl
import br.thiago.habitflowapp.data.repository.UsersRepositoryImpl
import br.thiago.habitflowapp.domain.repository.AuthRepository
import br.thiago.habitflowapp.domain.repository.UsersRepository
import br.thiago.habitflowapp.domain.use_cases.auth.AuthUseCases
import br.thiago.habitflowapp.domain.use_cases.auth.ForgotPassword
import br.thiago.habitflowapp.domain.use_cases.auth.GetCurrentUser
import br.thiago.habitflowapp.domain.use_cases.auth.Login
import br.thiago.habitflowapp.domain.use_cases.auth.Logout
import br.thiago.habitflowapp.domain.use_cases.auth.Signup
import br.thiago.habitflowapp.domain.use_cases.users.Create
import br.thiago.habitflowapp.domain.use_cases.users.UsersUseCases
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Named(USERS)
    fun provideStorageUsersRef(storage: FirebaseStorage): StorageReference = storage.reference.child(USERS)

    @Provides
    @Named(USERS)
    fun provideUsersRef(db: FirebaseFirestore): CollectionReference = db.collection(USERS)

//    @Provides
//    @Named(POSTS)
//    fun provideStoragePostsRef(storage: FirebaseStorage): StorageReference = storage.reference.child(POSTS)
//
//    @Provides
//    @Named(POSTS)
//    fun providePostsRef(db: FirebaseFirestore): CollectionReference = db.collection(POSTS)

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    fun provideUsersRepository(impl: UsersRepositoryImpl): UsersRepository = impl

//    @Provides
//    fun providePostsRepository(impl: PostRepositoryImpl): PostRepository = impl
//
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

    )

//    @Provides
//    fun providePostsUseCases(repository: PostRepository) = PostsUseCases(
//        create = CreatePost(repository),
//        getPosts = GetPosts(repository),
//        getPostsByIdUser = GetPostsByIdUser(repository),
//        deletePost = DeletePost(repository),
//        updatePost = UpdatePost(repository),
//        likePost = LikePost(repository),
//        deleteLikePost = DeleteLikePost(repository)
//    )

}