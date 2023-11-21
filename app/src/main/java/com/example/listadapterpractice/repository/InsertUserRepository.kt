package com.example.listadapterpractice.repository

import com.example.listadapterpractice.dataSource.InsertUserDataSource
import com.example.listadapterpractice.model.User

interface InsertUserRepository{
    suspend fun insertUser(user : User)
}

class InsertUserRepositoryImpl(private val insertUserDataSource : InsertUserDataSource) : InsertUserRepository{
    override suspend fun insertUser(user: User) {
        insertUserDataSource.insertUser(user)
    }

}