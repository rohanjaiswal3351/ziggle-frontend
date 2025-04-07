package com.rohan.datingapp.client

//import com.rohan.datingapp
import com.rohan.datingapp.BuildConfig
import com.rohan.datingapp.service.UserApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object UserClient {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL) // Dynamically fetch base URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build())
            .build()
    }

    val userService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
}