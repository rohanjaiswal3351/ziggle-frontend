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

    @GET("users/getNextValidUsers")
    suspend fun getNextValidUsers(
        @Query("uid") uid: String,
        @Query("rewindCheck") rewindCheck: Int,
        @Query("genderCheck") genderCheck: Int,
        @Query("distanceCheck") distanceCheck: Int,
        @Query("lastUserUid") lastUserUid: String): Response<List<UserModel>>

    @GET("users/getUserById")
    suspend fun getUserById(
        @Query("uid") uid: String
    ): Response<UserModel>

    @GET("users/getLikedUsers")
    suspend fun getLikedUsers(
        @Query("uid") uid:String
    ): Response<List<UserModel>>

    @GET("users/getFriends")
    suspend fun getFriends(
        @Query("uid") uid:String
    ): Response<List<UserModel>>

    @GET("users/getAllUsers")
    suspend fun getAllUsers(): Response<List<UserModel>>

    @GET("/users/getNextUsers")
    suspend fun getNextUsers(
        @Query("lastUserKey") lastUserKey: String,
        @Query("pageSize") pageSize: Int): Response<List<UserModel>>

    @POST("/users/addUser")
    suspend fun addUser(@Body user: UserModel): Response<Unit>

    @POST("/users/updateUser")
    suspend fun updateUser(@Body user: UserModel): Response<Unit>

    @DELETE("/users/deleteUser")
    suspend fun deleteUser(@Query("uid") uid: String): Response<Unit>

    @PUT("/users/updateUserName")
    suspend fun updateUserName(
        @Query("uid") uid: String,
        @Query("name") name: String): Response<Unit>

}