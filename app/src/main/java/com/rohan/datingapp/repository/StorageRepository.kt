package com.rohan.datingapp.repository

import android.util.Log
import com.rohan.datingapp.client.UserClient
import com.rohan.datingapp.request.UploadFileRequest
import retrofit2.Response

class StorageRepository {

    private val apiService = UserClient.storageService

    suspend fun deleteFile(
        filePath: String
    ): Result<Boolean> {
        return try {
            val response = apiService.deleteFile(filePath)
            Log.d("ApiResponse", "${response}")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadFile(
        uploadFileRequest: UploadFileRequest
    ): Result<String> {
        return try{
            val response = apiService.uploadFile(uploadFileRequest)
            Log.d("ApiResponse", "${response}")
            handleResponse(response)
        }catch (e: Exception){
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