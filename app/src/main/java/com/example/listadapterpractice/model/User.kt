package com.example.listadapterpractice.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val name : String,
    val viewType : Int,
    @PrimaryKey(autoGenerate = true)
    val idx : Int
)