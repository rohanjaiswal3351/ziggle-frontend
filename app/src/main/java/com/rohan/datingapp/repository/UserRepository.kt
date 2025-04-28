package com.rohan.datingapp.repository

import com.rohan.datingapp.client.UserClient
import com.rohan.datingapp.model.UserModel
import retrofit2.Response

class UserRepository {
    private val apiService = UserClient.userService

    suspend fun getNextUsers(uid: String, pageSize: Int): Result<List<UserModel>> {
        return try {
            val response = apiService.getNextUsers(uid, pageSize)
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun <T> handleResponse(response: Response<T>): Result<T> {
        return if (response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            Result.failure(RuntimeException(response.errorBody()?.string() ?: "Unknown error"))
        }
    }
}