package com.brydev.sleepwell.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.brydev.sleepwell.R
import com.brydev.sleepwell.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class HomeActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var btnSetting: ImageView
    private lateinit var btnSeeAll: TextView
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sleepWellPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnSetting = findViewById(R.id.settingIcon)
        btnSeeAll = findViewById(R.id.txtSeeAll)

        btnSetting.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        btnSeeAll.setOnClickListener{
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        sleepWellPrefs = getSharedPreferences("SleepWellPrefs", MODE_PRIVATE)

        // Mengatur tema (dark mode atau light mode)
        val isDarkMode = sharedPreferences.getBoolean("DARK_MODE", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Hilangkan ActionBar
        supportActionBar?.hide()

        // Ambil nama user dari SharedPreferences
        val sharedPreferences = getSharedPreferences("SleepWellPrefs", MODE_PRIVATE)
        val user_name = sharedPreferences.getString("userName", "User") // Default "User" jika nama kosong

        val userName = intent.getStringExtra("USER_NAME")
        val userNameTextView: TextView = findViewById(R.id.userName)
        userNameTextView.text = userName ?: user_name

        // Inisialisasi BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigation)

        // Set Listener untuk navigasi item di BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> true
                R.id.nav_lamp -> true
                R.id.nav_Profilw -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Tambahkan Listener untuk SleepPredict
        val sleepPredictIcon = findViewById<LinearLayout>(R.id.menuContainer)
        sleepPredictIcon.setOnClickListener {
            navigateToQuestionnaire()
        }
        // Tampilkan data prediksi
        loadPredictionHistory()

    }

    private fun navigateToQuestionnaire() {
        // Ambil data JWT dari SharedPreferences
        val jwtToken = sharedPreferences.getString("auth_token", null)

        // Buat Intent untuk navigasi ke QuestionnaireActivity
        val intent = Intent(this, QuestionnaireActivity::class.java)
        intent.putExtra("auth_token", jwtToken) // Tambahkan JWT ke Intent
        startActivity(intent)
    }

    fun toggleTheme(isDarkMode: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("DARK_MODE", isDarkMode)
        editor.apply()
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun loadPredictionHistory() {
        val label = sleepWellPrefs.getString("last_label", null)
        val prediction = sleepWellPrefs.getString("last_prediction", null)
        val predictionPersen = prediction + "%"
        val suggestion = sleepWellPrefs.getString("last_suggestion", null)
        val createdAt = sleepWellPrefs.getString("last_created", null)

        if (label != null && prediction != null && suggestion != null && createdAt != null) {
            val imgBingung: ImageView = findViewById(R.id.imgBingung)
            imgBingung.visibility = View.GONE

            binding.txtNothingHappens.text = """
                Label: $label
                Score: ${predictionPersen}
                Suggestion: $suggestion
                Date: ${formatDate(createdAt)}
            """.trimIndent()
        } else {
            binding.txtNothingHappens.text = "Nothing happened here..."
        }
        Log.d("HomeActivity", "Label: $label, Prediction: $prediction, Suggestion: $suggestion, CreatedAt: $createdAt")

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
