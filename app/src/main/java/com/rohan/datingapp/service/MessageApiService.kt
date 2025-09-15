package com.rohan.datingapp.service

import com.rohan.datingapp.model.MessageModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MessageApiService {

    @GET("messages/getChatRoomById")
    suspend fun getChatRoomById(
        @Query("chatId") chatId: String
    ): Response<List<MessageModel>>

    @GET("messages/isChatIdValid")
    suspend fun isChatIdValid(
        @Query("chatId") chatId: String
    ): Response<Boolean>

}