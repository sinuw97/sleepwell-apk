package com.brydev.sleepwell.api

import com.brydev.sleepwell.api.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/register")
    fun registerUser(@Body request: Map<String, String>): Call<RegisterResponse>

    @Headers("Content-Type: application/json")
    @POST("/login")
    fun loginUser(@Body request: Map<String, String>): Call<RegisterResponse>
}
