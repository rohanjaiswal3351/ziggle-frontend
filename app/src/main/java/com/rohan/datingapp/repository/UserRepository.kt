package com.rohan.datingapp.repository

import android.util.Log
import com.rohan.datingapp.R
import com.rohan.datingapp.client.UserClient
import com.rohan.datingapp.model.UserModel
import retrofit2.Response
import kotlin.math.log

class UserRepository {
    private val apiService = UserClient.userService

    suspend fun getNextValidUsers(
        uid: String,
        rewindCheck: Int,
        genderCheck: Int,
        distanceCheck: Int,
        lastUserUid: String
    ): Result<List<UserModel>> {
        return try {
            val response = apiService.getNextValidUsers(uid, rewindCheck, genderCheck, distanceCheck, lastUserUid)
            Log.d("ApiResponse", "${response}")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllUsers(): Result<List<UserModel>> {
        return try {
            val response = apiService.getAllUsers()
            Log.d("ApiResponse", "${response}")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLikedUsers(uid: String): Result<List<UserModel>> {
        return try {
            val response = apiService.getLikedUsers(uid)
            Log.d("ApiResponse", "${response}")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFriends(uid: String): Result<List<UserModel>> {
        return try {
            val response = apiService.getFriends(uid)
            Log.d("ApiResponse", "${response}")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getNextUsers(lastUserKey: String, pageSize: Int): Result<List<UserModel>> {
        return try {
            val response = apiService.getNextUsers(lastUserKey, pageSize)
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