package com.rohan.datingapp.service

import com.rohan.datingapp.model.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserApiService {
    @GET("/users/getNextUsers")
    suspend fun getNextUsers(
        @Query("uid") lastUserKey: String,
        @Query("pageSize") pageSize: Int
    ): Response<List<UserModel>>

    @POST("/users/addUser")
    suspend fun addUser(@Body user: UserModel): Response<Unit>

    @DELETE("/users/deleteUser")
    suspend fun deleteUser(@Query("uid") uid: String): Response<Unit>

    @PUT("/users/updateUserName")
    suspend fun updateUserName(
        @Query("uid") uid: String,
        @Query("name") name: String
    ): Response<Unit>

    // Add similar functions for other endpoints
    // Use @PUT for update operations with appropriate parameters
}