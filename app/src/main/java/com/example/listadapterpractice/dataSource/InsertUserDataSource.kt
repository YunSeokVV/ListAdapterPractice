package com.example.listadapterpractice.dataSource

import com.example.listadapterpractice.database.UserDao
import com.example.listadapterpractice.model.User

interface InsertUserDataSource {
    suspend fun insertUser(user : User)
}

class InsertUserDataSourceImpl(private val userDao: UserDao) : InsertUserDataSource{
    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

}