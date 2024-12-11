package com.brydev.sleepwell.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brydev.sleepwell.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menggunakan ViewBinding untuk mengakses elemen UI
        val result = intent.getSerializableExtra("result") as? PredictionResult
        if (result != null) {
            binding.predictionLabel.text = result.label
            binding.predictionScore.text = "${result.prediction}%"
            binding.suggestionText.text = result.suggestion
        } else {
            Toast.makeText(this, "Gagal memuat hasil prediksi.", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.saveButton.setOnClickListener {
            saveHistory(result)
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }
    private fun saveHistory(result: PredictionResult?) {
        if (result != null) {
            // Logika untuk menyimpan histori (bisa di database atau lainnya)
            Toast.makeText(this, "Hasil prediksi disimpan.", Toast.LENGTH_SHORT).show()
        } else {
            // Jika result null, tampilkan pesan error
            Toast.makeText(this, "Gagal menyimpan histori.", Toast.LENGTH_SHORT).show()
        }
    }
}