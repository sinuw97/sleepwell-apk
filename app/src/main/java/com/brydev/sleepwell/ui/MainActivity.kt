package com.brydev.sleepwell.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brydev.sleepwell.ApiClient
import com.brydev.sleepwell.R
import com.brydev.sleepwell.api.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        sharedPreferences = getSharedPreferences("SleepWellPrefs", MODE_PRIVATE)

        val edtEmail: EditText = findViewById(R.id.edtEmail)
        val edtPassword: EditText = findViewById(R.id.edtPassword)
        val btnSignIn: Button = findViewById(R.id.btnSignIn)
        val txtRegister: TextView = findViewById(R.id.txtRegister)

        btnSignIn.isEnabled = false

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btnSignIn.isEnabled = edtEmail.text.isNotEmpty() && edtPassword.text.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        edtEmail.addTextChangedListener(textWatcher)
        edtPassword.addTextChangedListener(textWatcher)

        txtRegister.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnSignIn.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            loginWithEmail(email, password)
        }
    }

    private fun loginWithEmail(email: String, password: String) {
        val apiClient = ApiClient.instance
        val request = mapOf("email" to email, "password" to password)

        apiClient.loginUser(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Toast.makeText(this@MainActivity, loginResponse?.message ?: "Login successful", Toast.LENGTH_SHORT).show()

                    // Save the JWT token in SharedPreferences
                    loginResponse?.token?.let { saveToken(it) }

                    navigateToHome()
                } else {
                    Toast.makeText(this@MainActivity, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to connect: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token)
        editor.putBoolean("is_logged_in", true)
        editor.apply()
    }


    private fun navigateToHome() {
        val intent = Intent(this@MainActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)
        if (isLoggedIn) {
            navigateToHome()
        }
    }
}

