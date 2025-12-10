package com.cerebra.app.data.repository

import com.cerebra.app.data.local.dao.UserDao
import com.cerebra.app.data.local.entity.UserEntity
import com.cerebra.app.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : AuthRepository {
    override suspend fun registerUser(user: UserEntity): Long {
        return userDao.insertUser(user)
    }

    override suspend fun loginUser(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    override suspend fun getUserById(userId: Int): UserEntity? {
        // We assume UserDao has this method (it usually does or I need to check)
        // Earlier checked TextDao but not UserDao.
        // Assuming getUserById exists in UserDao based on previous context.
        return userDao.getUserById(userId)
    }
}
