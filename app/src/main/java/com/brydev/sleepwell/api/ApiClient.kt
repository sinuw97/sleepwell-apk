import com.brydev.sleepwell.api.model.RegisterResponse
import com.brydev.sleepwell.model.RegisterRequest
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

object ApiClient {
    private const val BASE_URL = "https://sleepwell-backend-563173319559.asia-southeast2.run.app"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

interface ApiService {
    @POST("/register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>
    @POST("/login")
    fun loginUser(@Body request: Map<String, String>): Call<RegisterResponse>
}



