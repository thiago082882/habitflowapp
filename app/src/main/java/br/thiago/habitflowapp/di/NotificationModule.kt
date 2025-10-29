package br.thiago.habitflowapp.di

import br.thiago.habitflowapp.data.repository.NotificationHandlerImpl
import br.thiago.habitflowapp.domain.repository.NotificationHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    @Singleton
    abstract fun bindNotificationHandler(
        impl: NotificationHandlerImpl
    ): NotificationHandler
}
