package com.example.listadapterpractice.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    var name : String
){
    @PrimaryKey(autoGenerate = true)
    var idx: Long = 0
}