package com.rohan.datingapp.model

data class MessageModel(
    val chatId: String? = "",
    var key: String? = "",
    val senderId: String? = "",
    val message: String? = "",
    val currentTime: String? = "",
    val currentDate: String? = "",
    val media: String? = "",
    var isSeen:String? = ""
)