package com.brydev.sleepwell.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brydev.sleepwell.databinding.ActivityResultBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("SleepWellPrefs", MODE_PRIVATE)

        // Ambil data dari SharedPreferences
        val label = sharedPreferences.getString("label", "N/A")
        val prediction = sharedPreferences.getString("prediction", "0%")
        val suggestion = sharedPreferences.getString("suggestion", "No suggestion")
        val createdAt = sharedPreferences.getString("createdAt", "Unknown date")

        // Set data ke UI
        binding.predictionLabel.text = label
        binding.predictionScore.text = prediction+"%"
        binding.suggestionText.text = suggestion
        binding.createdDate.text = formatDate(createdAt)

        binding.saveButton.setOnClickListener {
            saveHistory(label, prediction, suggestion, createdAt)
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun saveHistory(
        label: String?,
        prediction: String?,
        suggestion: String?,
        createdAt: String?
    ) {
        if (label != null && suggestion != null && createdAt != null) {
            val sharedPreferences = getSharedPreferences("SleepWellPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val formated_date = formatDate(createdAt)

            editor.putString("last_label", label)
            editor.putString("last_prediction", prediction)
            editor.putString("last_suggestion", suggestion)
            editor.putString("last_created", createdAt) // Tanpa format ulang, simpan raw data
            editor.apply()

            Toast.makeText(this, "Hasil prediksi disimpan.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Gagal menyimpan histori.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun formatDate(createdAt: String?): String {
        try {
            val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormatter.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormatter.parse(createdAt)

            val outputFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            date?.let {
                return outputFormatter.format(it)
            } ?: run {
                return "Invalid Date"
            }
        } catch (e: Exception) {
            Log.e("ProfileActivity", "Error formatting date: ${e.message}")
            e.printStackTrace()
            return "Error formatting date"
        }
    }
}