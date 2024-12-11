package com.brydev.sleepwell

import com.brydev.sleepwell.api.model.RegisterResponse
import com.brydev.sleepwell.model.PredictSleepRequest
import com.brydev.sleepwell.ui.PredictionResult
import com.brydev.sleepwell.model.RegisterRequest
import com.brydev.sleepwell.model.UserResponse
import com.brydev.sleepwell.ui.ApiResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://sleepwell-backend-563173319559.asia-southeast2.run.app/"
    private const val BASE_URL_PREDICT = "https://sleepwell-ml-563173319559.asia-southeast2.run.app/"

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Logging untuk debugging
            .connectTimeout(30, TimeUnit.SECONDS) // Timeout untuk koneksi
            .readTimeout(30, TimeUnit.SECONDS) // Timeout untuk pembacaan
            .writeTimeout(30, TimeUnit.SECONDS) // Timeout untuk penulisan
            .build()
    }

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)  // Base URL tanpa slash ekstra di bagian akhir
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    // Instance untuk layanan prediksi
    val instancePredict: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_PREDICT)  // Base URL untuk layanan prediksi
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}

interface ApiService {
    @POST("register") // Endpoint untuk register, tanpa '/' di depan
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("login") // Endpoint untuk login, tanpa '/' di depan
    fun loginUser(@Body request: Map<String, String>): Call<RegisterResponse>

    @GET("users/profile") // Endpoint untuk mengambil profil, tanpa '/' di depan
    fun getUserProfile(@Header("Authorization") token: String): Call<UserResponse>

    @POST("predict")
    fun predictSleep(
        @Header("Authorization") token: String,
        @Body body: PredictSleepRequest
    ): Call<ApiResponse>

    @POST("users/profile") // Endpoint untuk update profil, tanpa '/' di depan
    fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body userData: Map<String, String>
    ): Call<UserResponse>
}

