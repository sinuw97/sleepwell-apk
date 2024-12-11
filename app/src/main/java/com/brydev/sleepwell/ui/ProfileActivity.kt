package com.brydev.sleepwell.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.brydev.sleepwell.ApiClient
import com.brydev.sleepwell.R
import com.brydev.sleepwell.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var etName: EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etGender: EditText
    private lateinit var etBirthdate: EditText
    private lateinit var btnEditProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        // Hilangkan ActionBar
        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("SleepWellPrefs", MODE_PRIVATE)

        etName = findViewById(R.id.etName)
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etGender = findViewById(R.id.etGender)
        etBirthdate = findViewById(R.id.etBirthdate)
        btnEditProfile = findViewById(R.id.btnEditProfile)

        fetchUserProfile()

        btnEditProfile.setOnClickListener {
            // Optionally navigate to EditProfileActivity to allow the user to edit the profile
        }
    }

    private fun fetchUserProfile() {
        val token = sharedPreferences.getString("auth_token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Token not found", Toast.LENGTH_SHORT).show()
            return
        }

        ApiClient.instance.getUserProfile("Bearer $token")
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let { user ->
                            etName.setText(user.name)
                            etUsername.setText(user.username)
                            etEmail.setText(user.email)
                            etGender.setText(user.gender)
                            etBirthdate.setText(user.birthdate)
                        }
                    } else {
                        Toast.makeText(this@ProfileActivity, "Failed to load profile", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Toast.makeText(this@ProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
