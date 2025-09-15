package com.rohan.datingapp.repository

import android.util.Log
import com.rohan.datingapp.client.UserClient
import com.rohan.datingapp.model.MessageModel
import com.rohan.datingapp.model.UserModel
import retrofit2.Response

class MessageRepository {

    private val apiService = UserClient.messageService

    suspend fun getChatRoomById(
        chatId: String
    ): Result<List<MessageModel>> {
        return try {
            val response = apiService.getChatRoomById(chatId)
            Log.d("ApiResponse", "${response}")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isChatIdValid(
        chatId: String
    ): Result<Boolean> {
        return try {
            val response = apiService.isChatIdValid(chatId)
            Log.d("ApiResponse", "${response}")
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