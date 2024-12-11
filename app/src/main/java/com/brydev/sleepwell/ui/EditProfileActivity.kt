package com.brydev.sleepwell.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brydev.sleepwell.ApiClient
import com.brydev.sleepwell.R
import com.brydev.sleepwell.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etGender: EditText
    private lateinit var etBirthdate: EditText
    private lateinit var btnSave: Button
    private lateinit var jwtToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        jwtToken = intent.getStringExtra("TOKEN") ?: ""

        // Inisialisasi View
        etName = findViewById(R.id.etName)
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etGender = findViewById(R.id.etGender)
        etBirthdate = findViewById(R.id.etBirthdate)
        btnSave = findViewById(R.id.btnEditProfile)

        btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val userData = mapOf(
            "name" to etName.text.toString(),
            "username" to etUsername.text.toString(),
            "email" to etEmail.text.toString(),
            "gender" to etGender.text.toString(),
            "birthdate" to etBirthdate.text.toString()
        )

        ApiClient.instance.updateUserProfile("Bearer $jwtToken", userData)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@EditProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Toast.makeText(this@EditProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
