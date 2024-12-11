package com.brydev.sleepwell.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.brydev.sleepwell.R

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Hilangkan ActionBar
        supportActionBar?.hide()

        // Jadikan layar full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Inisialisasi logo
        val splashScreenImage: ImageView = findViewById(R.id.SplashScreenImage)

        // Load animasi
        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        // Jalankan animasi
        splashScreenImage.startAnimation(zoomIn)
        splashScreenImage.startAnimation(fadeIn)

        // Pindah ke MainActivity setelah animasi selesai
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}
