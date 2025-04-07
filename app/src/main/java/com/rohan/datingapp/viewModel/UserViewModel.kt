package com.rohan.datingapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {
    private val repository = UserRepository()
    val users = MutableStateFlow<List<UserModel>>(emptyList())
    val errorMessage = MutableStateFlow<String?>(null)

    fun loadNextUsers(lastUserKey: String, pageSize: Int) {
        viewModelScope.launch {
            repository.getNextUsers(lastUserKey, pageSize)
                .onSuccess { users.value = it }
                .onFailure { errorMessage.value = it.message }
        }
    }
}