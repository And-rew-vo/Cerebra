package com.cerebra.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cerebra.app.data.local.dao.TextDao
import com.cerebra.app.data.local.dao.UserDao
import com.cerebra.app.data.local.entity.TextEntity
import com.cerebra.app.data.local.entity.UserEntity

@Database(entities = [UserEntity::class, TextEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun textDao(): TextDao
}
