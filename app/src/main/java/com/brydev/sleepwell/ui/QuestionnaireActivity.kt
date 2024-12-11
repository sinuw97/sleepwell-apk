package com.brydev.sleepwell.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.brydev.sleepwell.ApiClient
import com.brydev.sleepwell.R
import com.brydev.sleepwell.adapter.QuestionPagerAdapter
import com.brydev.sleepwell.model.InputType
import com.brydev.sleepwell.model.Question
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionnaireActivity : AppCompatActivity() {

    lateinit var viewPager: ViewPager2
    private lateinit var questions: List<Question>
    private var answers = mutableMapOf<String, Any>()
    private lateinit var adapter: QuestionPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)
        supportActionBar?.hide()

        viewPager = findViewById(R.id.question_view_pager)

        // Daftar pertanyaan
        questions = listOf(
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

        adapter = QuestionPagerAdapter(questions, answers) { position ->
            validateAnswer(position)
        }
        viewPager.adapter = adapter
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
            navigateToResult()
        } else {
            viewPager.currentItem = position + 1
        }
    }

    private fun navigateToResult() {
        val data = mapOf(
            "age" to (answers["age"]?.toString()?.toIntOrNull() ?: 0),
            "gender" to if (answers["gender"] == "Laki-laki") 1 else 0,
            "sleepDuration" to (answers["sleepDuration"]?.toString()?.toFloatOrNull() ?: 0f),
            "awakenings" to (answers["awakenings"]?.toString()?.toFloatOrNull() ?: 0f),
            "stayAwake" to (answers["stayAwake"]?.toString()?.toFloatOrNull() ?: 0f),
            "caffeine" to (answers["caffeine"]?.toString()?.toFloatOrNull() ?: 0f),
            "alcohol" to (answers["alcohol"]?.toString()?.toFloatOrNull() ?: 0f),
            "smoking" to if (answers["smoking"] == "Yes") 1 else 0,
            "exercise" to (answers["exercise"]?.toString()?.toFloatOrNull() ?: 0f)
        )

        Log.d("DEBUG", "Data to API: $data")

        val call = ApiClient.instance.predictSleep(data)
        call.enqueue(object : Callback<PredictionResult> {
            override fun onResponse(call: Call<PredictionResult>, response: Response<PredictionResult>) {
                if (response.isSuccessful) {
                    Log.d("DEBUG", "API Response: ${response.body()}")
                    val intent = Intent(this@QuestionnaireActivity, ResultActivity::class.java)
                    intent.putExtra("result", response.body())
                    startActivity(intent)
                } else {
                    Log.e("DEBUG", "API Error: ${response.errorBody()?.string()}")
                    Toast.makeText(this@QuestionnaireActivity, "Gagal memprediksi, periksa input!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PredictionResult>, t: Throwable) {
                Log.e("DEBUG", "API Failure: ${t.message}")
                Toast.makeText(this@QuestionnaireActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
