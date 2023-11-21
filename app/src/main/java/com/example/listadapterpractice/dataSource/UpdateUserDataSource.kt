package com.example.listadapterpractice.dataSource

import com.example.listadapterpractice.database.UserDao
import com.example.listadapterpractice.model.User
import com.orhanobut.logger.Logger

interface UpdateUserDataSource{
    suspend fun updateUser(user : User)
}

class UpdateUserDataSourceImpl(private val userDao: UserDao) : UpdateUserDataSource{
    override suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

}