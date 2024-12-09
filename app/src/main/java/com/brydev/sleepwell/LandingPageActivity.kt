package com.brydev.sleepwell

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*

class LandingPageActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: LandingPageAdapter
    private lateinit var images: List<Int>
    private lateinit var btnSkip: TextView
    private lateinit var btnNext: MaterialButton

    // Coroutine for auto-scrolling
    private val autoScrollJob = CoroutineScope(Dispatchers.Main).launch {
        while (true) {
            delay(3000) // Delay 3 seconds
            val nextItem = (viewPager.currentItem + 1) % images.size
            viewPager.currentItem = nextItem
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        // List of images to display in ViewPager
        images = listOf(
            R.drawable.tidur,
            R.drawable.kas,
            R.drawable.bobok,
            R.drawable.bobokan
        )

        // Initializing views
        viewPager = findViewById(R.id.viewPager)
        btnSkip = findViewById(R.id.btnSkip)
        btnNext = findViewById(R.id.btnNext)

        // Setting up ViewPager adapter
        adapter = LandingPageAdapter(images)
        viewPager.adapter = adapter

        // Attach TabLayout to ViewPager with TabLayoutMediator
        TabLayoutMediator(findViewById(R.id.indicator), viewPager) { tab, position ->
            // Customize the tab indicator if needed, for example, setting titles or icons
            tab.text = "Page ${position + 1}"
        }.attach()

        // Next button click listener for navigating to the next page
        btnNext.setOnClickListener {
            val nextItem = (viewPager.currentItem + 1) % images.size
            viewPager.currentItem = nextItem
        }

        // Skip button click listener to skip the landing page and go to MainActivity
        btnSkip.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    // Cancel auto-scrolling coroutine when activity is destroyed
    override fun onDestroy() {
        autoScrollJob.cancel()
        super.onDestroy()
    }
}
