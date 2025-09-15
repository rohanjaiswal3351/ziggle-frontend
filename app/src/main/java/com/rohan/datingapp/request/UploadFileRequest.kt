package com.rohan.datingapp.request

import okhttp3.MultipartBody

data class UploadFileRequest(
    private val file: MultipartBody.Part,
    private val userId: String,
    private val check: Int
)
