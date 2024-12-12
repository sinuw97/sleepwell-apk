package com.brydev.sleepwell.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Switch
import com.brydev.sleepwell.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Hilangkan ActionBar
        supportActionBar?.hide()

        val themeSwitch: Switch = findViewById(R.id.switch_theme)
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("DARK_MODE", false)
        themeSwitch.isChecked = isDarkMode

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val homeActivity = HomeActivity()
            homeActivity.toggleTheme(isChecked)
        }
    }
}
