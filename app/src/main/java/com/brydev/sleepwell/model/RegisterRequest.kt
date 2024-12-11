package com.brydev.sleepwell.model

data class RegisterRequest(
    val username: String,
    val name: String,
    val email: String,
    val password: String,
    val gender: String,
    val dob: String
)

