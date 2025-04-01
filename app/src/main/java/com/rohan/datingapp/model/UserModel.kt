package com.rohan.datingapp.model

data class UserModel(
    val uid : String? = "",
    var fcmToken: String? = "",
    val email : String? = "",
    var name : String? = "",
    var city : String? = "",
    var gender : String? = "",
    var star : String? = "",
    var image : String? = "",
    var image1: String? = "",
    var image2: String? = "",
    var image3: String? = "",
    var age : String? = "",
    var bio: String? = "",
    val rightSwipeBy:ArrayList<String>? = null,
    var height:String? = "",
    var exercise:String? = "",
    var education:String? = "",
    var instaId:String? = "",
    var snapId:String? = "",
    val matches:ArrayList<String>? = null,
    var interests:ArrayList<String>? = null,
    val interact:ArrayList<String>? = null,
    var blockedUsers:ArrayList<String>? = null,
    var likeNotify:String? = "No"
)
