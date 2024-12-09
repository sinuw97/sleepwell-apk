package com.brydev.sleepwell

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.brydev.sleepwell.api.model.RegisterResponse
import com.brydev.sleepwell.model.RegisterRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val etName: EditText = findViewById(R.id.etName)
        val etUsername: EditText = findViewById(R.id.etUsername)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val etDate: EditText = findViewById(R.id.etDate)
        val spGender: Spinner = findViewById(R.id.spinnerGender)
        val btnRegister: Button = findViewById(R.id.btnRegister)

        val genderOptions = arrayOf("Male", "Female")
        spGender.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            genderOptions
        )

        btnBack.setOnClickListener { finish() }

        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "${selectedYear}-${
                    (selectedMonth + 1).toString().padStart(2, '0')
                }-${selectedDay.toString().padStart(2, '0')}"
                etDate.setText(formattedDate)
            }, year, month, day)
            datePicker.show()
        }

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val dob = etDate.text.toString().trim()
            val gender = when (spGender.selectedItem.toString()) {
                "Male" -> "M"
                "Female" -> "F"
                else -> ""
            }

            if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || dob.isEmpty() || gender.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = RegisterRequest(username, name, email, password, gender, dob)

            ApiClient.instance.registerUser(request).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        Toast.makeText(
                            this@RegisterActivity,
                            registerResponse?.message ?: "Success",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Jika status response adalah success, maka bersihkan teks input
                        if (registerResponse?.status == "success") {
                            // Clear all fields
                            etName.text.clear()
                            etUsername.text.clear()
                            etEmail.text.clear()
                            etPassword.text.clear()
                            etDate.text.clear()
                            spGender.setSelection(0) // Set gender spinner to default "Male"
                            finish()  // Tutup activity setelah sukses
                        }
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Error: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Failed: ${t.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

    }
}
