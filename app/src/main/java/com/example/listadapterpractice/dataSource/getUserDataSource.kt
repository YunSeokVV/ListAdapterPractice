package com.example.listadapterpractice.dataSource

import androidx.lifecycle.LiveData
import com.example.listadapterpractice.database.UserDao
import com.example.listadapterpractice.model.User

interface GetUserDataSource {
    fun getUser() : LiveData<List<User>>
}

class GetUserDataSourceImpl(private val userDao : UserDao) : GetUserDataSource{
    override fun getUser(): LiveData<List<User>> {

        return userDao.getUser()
    }

}