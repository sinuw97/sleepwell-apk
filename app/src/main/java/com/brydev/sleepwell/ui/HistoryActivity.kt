package com.brydev.sleepwell.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brydev.sleepwell.R
import com.brydev.sleepwell.adapter.HistoryAdapter
import com.brydev.sleepwell.model.HistoryItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryActivity : AppCompatActivity() {

    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hilangkan ActionBar
        supportActionBar?.hide()
        setContentView(R.layout.activity_history)

        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Sample data with auto-generated date
        val historyList = listOf(
            HistoryItem("Low Sleep", getCurrentDate()),
            HistoryItem("Good Quality", getCurrentDate()),
            HistoryItem("Good Quality", getCurrentDate()),
            HistoryItem("Low Sleep", getCurrentDate())
        )

        // Set RecyclerView
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = HistoryAdapter(historyList)

        // Bottom navigation
        bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.nav_lamp -> {
                    startActivity(Intent(this, QuestionnaireActivity::class.java))
                    true
                }
                R.id.nav_lamp -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
