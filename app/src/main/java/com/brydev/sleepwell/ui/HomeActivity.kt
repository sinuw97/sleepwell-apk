package com.brydev.sleepwell.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.brydev.sleepwell.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        // Mengatur tema (dark mode atau light mode)
        val isDarkMode = sharedPreferences.getBoolean("DARK_MODE", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
    }

    private fun navigateToQuestionnaire() {
        // Ambil data JWT dari SharedPreferences
        val jwtToken = sharedPreferences.getString("JWT_TOKEN", null)

        // Buat Intent untuk navigasi ke QuestionnaireActivity
        val intent = Intent(this, QuestionnaireActivity::class.java)
        intent.putExtra("JWT_TOKEN", jwtToken) // Tambahkan JWT ke Intent
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
}
