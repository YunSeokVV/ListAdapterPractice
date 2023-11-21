package com.example.listadapterpractice.repository

import androidx.lifecycle.LiveData
import com.example.listadapterpractice.dataSource.GetUserDataSource
import com.example.listadapterpractice.model.User

interface GetUserRepository {
    fun getUser() : LiveData<List<User>>
}

class GetUserRepositoryImpl(private val getUserDataSource : GetUserDataSource) : GetUserRepository{
    override fun getUser(): LiveData<List<User>> {

        return getUserDataSource.getUser()
    }

}