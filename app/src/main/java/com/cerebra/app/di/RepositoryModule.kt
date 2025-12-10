package com.cerebra.app.di

import com.cerebra.app.data.repository.CerebraRepository
import com.cerebra.app.data.repository.CerebraRepositoryImpl
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
    abstract fun bindCerebraRepository(
        cerebraRepositoryImpl: CerebraRepositoryImpl
    ): CerebraRepository
}
