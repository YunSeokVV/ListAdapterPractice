package com.example.listadapterpractice.dataSource

import androidx.lifecycle.LiveData
import com.example.listadapterpractice.database.UserDao
import com.example.listadapterpractice.model.User


interface UserDataSource {
    suspend fun getUser(userName : String) : List<User>
    fun getAllUser() : LiveData<List<User>>
    suspend fun insertUser(user : User)

    suspend fun updateUser(user : User)
}
class UserDataSourceImpl(private val userDao : UserDao) : UserDataSource{
    override suspend fun getUser(userName : String): List<User> {
        return userDao.getUser(userName)
    }

    override fun getAllUser(): LiveData<List<User>> {
        return userDao.getAllUser()
    }

    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
}