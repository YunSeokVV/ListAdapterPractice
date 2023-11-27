package com.example.listadapterpractice.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listadapterpractice.adapter.ViewType
import com.example.listadapterpractice.model.User
import com.example.listadapterpractice.repository.UserRepository
import com.orhanobut.logger.Logger
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

internal class MyViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userList: LiveData<List<User>>
    val usersList: LiveData<List<User>>
        get() {
            return _userList
        }

    fun searchUserUI(userList: List<User>): List<User> {
        val searchUser = User("searchWord", ViewType.SEARCH)
        var totalList = mutableListOf<User>()
        totalList.add(searchUser)
        totalList.addAll(userList)

        return totalList
    }

    init {
        _userList = userRepository.getAllUser()
    }

    //var clickedPosition: Int = 0

    suspend fun getUser(userName: String): List<User> {
        return viewModelScope.async {
            Logger.v(userName.toString())
            return@async userRepository.getUser(userName)
        }.await()
    }



    fun insertUser(userName: String) = viewModelScope.launch {
        val user = User(userName, ViewType.SEARCH_RESULT)
        userRepository.insertUser(user)
    }

    fun updateUser(user: User) = viewModelScope.launch {
        //user.name = userName
        userRepository.updateUser(user)
    }

}