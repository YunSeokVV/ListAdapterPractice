package com.example.listadapterpractice.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.listadapterpractice.model.User


@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getUser(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user : User)

    @Update
    suspend fun updateUser(user : User)
}