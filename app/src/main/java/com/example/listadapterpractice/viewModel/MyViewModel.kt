package com.example.listadapterpractice.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listadapterpractice.adapter.UserAdapter
import com.example.listadapterpractice.model.Search
import com.example.listadapterpractice.model.ViewType
import com.example.listadapterpractice.model.User
import com.example.listadapterpractice.repository.UserRepository
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

internal class MyViewModel(
    private val userRepository: UserRepository
) : ViewModel() {


    private val _userList: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>()
    }

    val userList: LiveData<List<User>>
        get() {
            return _userList
        }


    fun getAllUser(): Deferred<List<User>> {
        return viewModelScope.async {
            userRepository.getAllUser()
        }
    }

    fun searchUserUI(listUser: List<ViewType>): List<ViewType> {
        val searchUser = Search()

        var allHolder = mutableListOf<ViewType>()
        allHolder.add(searchUser)
        allHolder.addAll(listUser)
        Logger.v(allHolder.toString())
        return allHolder
    }

    init {
        viewModelScope.launch {
            _userList.value = userRepository.getAllUser()
        }

    }

    fun searchUser(userName: String) {
        viewModelScope.launch {
            _userList.value = userRepository.getUser(userName)
            Logger.v(_userList.value.toString())
        }
    }

    //todo :  이 메소드를 멘토님이 수업중에 왜 설명해주셨는지 정확히 기억하는가?
    fun test(adapter: UserAdapter) {
        val list = listOf<ViewType>(
            Search(), User("jys", 0), User("jys2", 0)
        )
        adapter.submitList(list)

    }

    fun insertUser(userName: String) = viewModelScope.launch {
        //al user = User(userName, ViewTypeInteger.SEARCH_RESULT, 0)
        val user = User(userName, 0)

        userRepository.insertUser(user)
    }

    fun updateUser(user: User) = viewModelScope.launch {
        //user.name = userName
        Logger.v(user.toString())
        userRepository.updateUser(user)
    }


}