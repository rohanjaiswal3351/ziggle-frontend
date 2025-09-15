package com.rohan.datingapp.service

import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.request.UploadFileRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Query

interface StorageApiService {

    @DELETE("/storage/deleteFile")
    suspend fun deleteFile(@Query("filePath") filePath: String): Response<Boolean>

    @POST("/storage/uploadFile")
    suspend fun uploadFile(@Body uploadFileRequest: UploadFileRequest): Response<String>
}