package com.brydev.sleepwell

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Switch

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

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
