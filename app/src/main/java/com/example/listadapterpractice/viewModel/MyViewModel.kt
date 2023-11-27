package com.example.listadapterpractice.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.listadapterpractice.adapter.UserAdapter
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
        val searchUser = User("searchWord", ViewType.SEARCH, 0)
        val totalList = mutableListOf<User>()
        totalList.add(searchUser)
        totalList.addAll(userList)

        return totalList
    }

    init {
        _userList = userRepository.getAllUser()
    }

    //todo:비즈니스 로직 처리를 위해서 메소드의 매개변수로 어댑터를 넣었습니다. 그런데 뷰모델에서 어댑터를 쓰는게 좋은 방법인지는 잘 모르겠네요.
    // 생성자를 이용한 의존성 주입으로 처리하는게 더 나은 방법인지 멘토님의 의견이 궁금합니다.
    fun setSearchUser(adapter: UserAdapter, userName: String) {
        viewModelScope.launch {
            adapter.submitList(userRepository.getUser(userName))
        }
    }


    fun insertUser(userName: String) = viewModelScope.launch {
        val user = User(userName, ViewType.SEARCH_RESULT, 0)
        userRepository.insertUser(user)
    }

    fun updateUser(user: User) = viewModelScope.launch {
        //user.name = userName
        Logger.v(user.toString())
        userRepository.updateUser(user)
    }

}