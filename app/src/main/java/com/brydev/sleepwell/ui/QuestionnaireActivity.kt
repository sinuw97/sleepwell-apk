package com.brydev.sleepwell.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.brydev.sleepwell.ApiClient
import com.brydev.sleepwell.R
import com.brydev.sleepwell.adapter.QuestionPagerAdapter
import com.brydev.sleepwell.model.InputType
import com.brydev.sleepwell.model.PredictSleepRequest
import com.brydev.sleepwell.model.Question
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionnaireActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: QuestionPagerAdapter
    private val questions: List<Question> by lazy {
        listOf(
            Question("Berapa usia kamu?", "age", InputType.NUMBER),
            Question("Jenis kelamin?", "gender", InputType.RADIO, options = listOf("Perempuan", "Laki-laki")),
            Question("Berapa lama kamu tidur (jam)?", "sleepDuration", InputType.NUMBER),
            Question("Berapa kali kamu terbangun ketika tidur?", "awakenings", InputType.NUMBER),
            Question("Berapa lama kamu masih bangun ketika di kasur sebelum tidur (jam)?", "stayAwake", InputType.NUMBER),
            Question("Berapa banyak kamu mengkonsumsi kafein hari ini (mg)?", "caffeine", InputType.NUMBER),
            Question("Berapa banyak kamu mengkonsumsi alkohol hari ini (mg)?", "alcohol", InputType.NUMBER),
            Question("Apakah kamu seorang perokok?", "smoking", InputType.RADIO, options = listOf("No", "Yes")),
            Question("Berapa kali kamu olahraga setiap minggu?", "exercise", InputType.NUMBER)
        )
    }

    private val answers: MutableMap<String, Any> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)
        supportActionBar?.hide()
        viewPager = findViewById(R.id.question_view_pager)

        adapter = QuestionPagerAdapter(questions, answers) { position ->
            validateAnswer(position)
        }
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false // Disable user swipe, navigation handled by button
    }

    private fun validateAnswer(position: Int) {
        val currentQuestion = questions[position]
        val answer = answers[currentQuestion.key]

        if (answer == null || answer.toString().isEmpty()) {
            Toast.makeText(this, "Isi jawaban terlebih dahulu!", Toast.LENGTH_SHORT).show()
            return
        }

        when (currentQuestion.inputType) {
            InputType.NUMBER -> {
                val number = answer.toString().toFloatOrNull()
                if (number == null || number < 0) {
                    Toast.makeText(this, "Jawaban harus berupa angka positif!", Toast.LENGTH_SHORT).show()
                    return
                }
            }
            InputType.RADIO -> {
                if (!currentQuestion.options.orEmpty().contains(answer.toString())) {
                    Toast.makeText(this, "Pilih salah satu opsi yang tersedia!", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }

        if (position == questions.lastIndex) {
            submitAnswers()
        } else {
            viewPager.currentItem = position + 1
        }

        Log.d("DEBUG", "Key saat validasi: ${currentQuestion.key}, Jawaban: ${answers[currentQuestion.key]}")

    }

    private fun submitAnswers() {
        Toast.makeText(this, "Mengirim data...", Toast.LENGTH_SHORT).show()
        Log.d("DEBUG", "Answers: $answers")

        lifecycleScope.launch {
            val apiService = ApiClient.instancePredict

            // Ambil token dari SharedPreferences
            val sharedPref = getSharedPreferences("SleepWellPrefs", MODE_PRIVATE)
            val token = sharedPref.getString("auth_token", null)

            if (token.isNullOrEmpty()) {
                Toast.makeText(this@QuestionnaireActivity, "Token tidak ditemukan. Silakan login ulang.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            // Buat request body sesuai format
            val request = PredictSleepRequest(
                age = answers["age"]?.toString()?.toFloatOrNull()?.toInt() ?: 0,
                gender = if (answers["gender"] == "Laki-laki") 1 else 0,
                sleepDuration = answers["sleepDuration"]?.toString()?.toFloat() ?: 0f,
                awakenings = answers["awakenings"]?.toString()?.toFloat() ?: 0f,
                stayAwake = answers["stayAwake"]?.toString()?.toFloat() ?: 0f,
                caffeine = answers["caffeine"]?.toString()?.toFloat() ?: 0f,
                alcohol = answers["alcohol"]?.toString()?.toFloat() ?: 0f,
                smoking = if (answers["smoking"] == "Yes") 1 else 0,
                exercise = answers["exercise"]?.toString()?.toFloat() ?: 0f
            )

            Log.d("DEBUG", "Data to API: $request")
            Log.d("DEBUG", "Request Body: $request")

            apiService.predictSleep("Bearer $token", request).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        Log.d("DEBUG", "Response sukses: ${response.body()}")
                        Log.d("DEBUG", "Response body: ${response.body()}")
                        response.body()?.data?.let { predictionResult ->
                            showPredictionResult(predictionResult)
                        } ?: run {
                            Log.e("DEBUG", "Data prediksi kosong.")
                            Toast.makeText(this@QuestionnaireActivity, "Data prediksi kosong.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("DEBUG", "Response error: ${response.code()} - $errorBody")
                        Toast.makeText(this@QuestionnaireActivity, "Gagal memproses prediksi: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("DEBUG", "onFailure: ${t.message}")
                    Toast.makeText(this@QuestionnaireActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun showPredictionResult(result: PredictionResult) {
        val message = """
            Label: ${result.label}
            Prediksi: ${result.prediction}
            Saran: ${result.suggestion}
            Dibuat pada: ${result.createdAt}
        """.trimIndent()
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        // Implementasikan navigasi ke layar hasil prediksi, jika ada.
    }
}
