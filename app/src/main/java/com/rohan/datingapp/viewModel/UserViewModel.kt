package com.rohan.datingapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val repository = UserRepository()
    val users = MutableStateFlow<List<UserModel>>(emptyList())
    val errorMessage = MutableStateFlow<String?>(null)

    fun loadNextUsers(uid: String, pageSize: Int) {
        viewModelScope.launch {
            repository.getNextUsers(uid, pageSize)
                .onSuccess { users.value = it }
                .onFailure { errorMessage.value = it.message }
        }
    }
}