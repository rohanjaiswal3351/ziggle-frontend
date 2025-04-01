package com.rohan.datingapp.notification

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @Headers("Content-Type:application/json",
    "Authorization:key=AAAAEyQRUeg:APA91bEP_HX3h6BXoRcPhmnbeTuNFeIbCGygFUFf4hsPjcjRlAql5QkdvVub8PqQpUVYRMvEJs8XUUF0UfnpvYimFxOyhFkjYR4plBFu_v4HQal2cjJay7gAuhFdiamIKpqxl82ZQ-ba")
    @POST("fcm/send")
    fun sendNotification(@Body notificationModel: PushNotificationModel)
    : Call<PushNotificationModel>


}