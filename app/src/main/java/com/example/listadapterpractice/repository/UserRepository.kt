package com.example.listadapterpractice.repository

import androidx.lifecycle.LiveData
import com.example.listadapterpractice.dataSource.UserDataSource
import com.example.listadapterpractice.model.User

interface UserRepository {
    fun getAllUser(): LiveData<List<User>>

    suspend fun getUser(userName : String): List<User>

    suspend fun insertUser(user: User)
    suspend fun updateUser(user: User)
}

class UserRepositoryImpl(private val userDataSource: UserDataSource) : UserRepository {
    override fun getAllUser(): LiveData<List<User>> {
        return userDataSource.getAllUser()
    }

    override suspend fun getUser(userName : String): List<User> {
        return userDataSource.getUser(userName)
    }

    override suspend fun insertUser(user: User) {
        userDataSource.insertUser(user)
    }

    override suspend fun updateUser(user: User) {
        userDataSource.updateUser(user)
    }

}