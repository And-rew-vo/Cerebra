package com.cerebra.app.di

import com.cerebra.app.data.repository.AuthRepositoryImpl
import com.cerebra.app.data.repository.TextRepositoryImpl
import com.cerebra.app.domain.repository.AuthRepository
import com.cerebra.app.domain.repository.TextRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTextRepository(
        textRepositoryImpl: TextRepositoryImpl
    ): TextRepository
}
