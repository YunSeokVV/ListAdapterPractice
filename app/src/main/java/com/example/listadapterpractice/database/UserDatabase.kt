package com.example.listadapterpractice.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.listadapterpractice.model.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase(){
    abstract fun userDao() : UserDao


    companion object{
        private var INSTANCE : UserDatabase? = null
        fun getInstance(context : Context) : UserDatabase{
            if (INSTANCE == null) {
                //다른 메소드가 접근하는 것을 허용하지 않게끔 lock 시켜주고 하나의 스레드만 접근할 수 있게끔 해준다.
                synchronized(UserDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, UserDatabase::class.java, "user_database"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }

}