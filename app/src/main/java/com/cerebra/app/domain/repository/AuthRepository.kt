package com.cerebra.app.domain.repository

import com.cerebra.app.data.local.entity.UserEntity

interface AuthRepository {
    suspend fun registerUser(user: UserEntity): Long
    suspend fun loginUser(email: String): UserEntity?
}
