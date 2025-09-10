package com.rohan.datingapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {
    private val repository = UserRepository()
    val users = MutableStateFlow<List<UserModel>>(emptyList())
    val usersV2 = MutableStateFlow<List<UserModel>>(emptyList())
    val errorMessage = MutableStateFlow<String?>(null)
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun loadNextValidUsers(
        uid: String,
        rewindCheck: Int,
        genderCheck: Int,
        distanceCheck: Int,
        lastUserUid: String
    ) {
        viewModelScope.launch {
            _loading.value = true
            repository.getNextValidUsers(uid, rewindCheck, genderCheck, distanceCheck, lastUserUid)
                .onSuccess {
                    users.value = it
                    _loading.value = false
                }
                .onFailure {
                    errorMessage.value = it.message
                    _loading.value = false
                }
        }
    }

    fun getLikedUsers(uid:  String){
        viewModelScope.launch {
            _loading.value = true
            repository.getLikedUsers(uid)
                .onSuccess {
                    users.value = it
                    _loading.value = false
                }
                .onFailure {
                    errorMessage.value = it.message
                    _loading.value = false
                }
        }
    }

    fun getFriends(uid:  String){
        viewModelScope.launch {
            repository.getFriends(uid)
                .onSuccess { usersV2.value = it }
                .onFailure { errorMessage.value = it.message }
        }
    }

    fun getAllUsers(){
        viewModelScope.launch {
            repository.getAllUsers()
                .onSuccess { users.value = it }
                .onFailure { errorMessage.value = it.message }
        }
    }

    fun loadNextUsers(lastUserKey: String, pageSize: Int) {
        viewModelScope.launch {
            repository.getNextUsers(lastUserKey, pageSize)
                .onSuccess { users.value = it }
                .onFailure { errorMessage.value = it.message }
        }
    }
}