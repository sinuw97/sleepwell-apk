package com.brydev.sleepwell.api

import com.brydev.sleepwell.api.model.RegisterResponse
import com.brydev.sleepwell.model.PredictSleepRequest
import com.brydev.sleepwell.model.RegisterRequest
import com.brydev.sleepwell.model.UserResponse
import com.brydev.sleepwell.ui.PredictionResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    // Endpoint untuk login
    @POST("login")
    fun loginUser(@Body request: Map<String, String>): Call<RegisterResponse>

    // Endpoint untuk mendaftar pengguna
    @POST("register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    // Endpoint untuk mendapatkan profil pengguna
    @GET("profile")
    fun getUserProfile(@Header("Authorization") authHeader: String): Call<UserResponse>
    @POST("predict")
    fun predictSleep(@Body data: PredictSleepRequest): Call<PredictionResult>
    }


