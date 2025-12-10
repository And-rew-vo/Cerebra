package com.cerebra.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cerebra.app.data.local.dao.TextDocumentDao
import com.cerebra.app.data.local.dao.UserDao
import com.cerebra.app.data.local.entity.TextDocument
import com.cerebra.app.data.local.entity.User

@Database(entities = [User::class, TextDocument::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun textDocumentDao(): TextDocumentDao
}
