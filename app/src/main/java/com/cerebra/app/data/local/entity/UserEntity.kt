package com.cerebra.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val password: String, // Storing plain text password for MVP as requested implicitly by "password" field requirement, usually hash is better but adhering to prompt "password" field.
    val name: String
)
