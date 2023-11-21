package com.example.listadapterpractice.repository

import com.example.listadapterpractice.dataSource.UpdateUserDataSource
import com.example.listadapterpractice.model.User

interface UpdateUserRepository{
    suspend fun updateUser(user : User)
}

class UpdateUserRepositoryImpl(private val updateUserDataSource: UpdateUserDataSource) : UpdateUserRepository{
    override suspend fun updateUser(user: User) {
        updateUserDataSource.updateUser(user)
    }

}