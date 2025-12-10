package com.cerebra.app.di

import android.content.Context
import androidx.room.Room
import com.cerebra.app.data.local.AppDatabase
import com.cerebra.app.data.local.dao.TextDocumentDao
import com.cerebra.app.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cerebra_database.db"
        ).fallbackToDestructiveMigration() // For MVP simplicity
         .build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideTextDocumentDao(database: AppDatabase): TextDocumentDao {
        return database.textDocumentDao()
    }
}
