package com.example.listadapterpractice.model

import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class ViewType

class Search : ViewType()

@Entity
data class User(
    val name : String,
    @PrimaryKey(autoGenerate = true)
    val idx : Int
) : ViewType() {
    fun toUI() : UIUser{
        return UIUser(this.name)
    }
}

//extension
fun List<User>.toUIUsers() : List<UIUser>{
    //this == List<User>
    return this.map {
        it.toUI()
    }
}

fun test(){
    val tmp = listOf<User>()
    tmp.toUIUsers()
}

data class UIUser(val name : String)