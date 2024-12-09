package com.brydev.sleepwell.api.model

data class RegisterResponse(
    val status: String,
    val message: String,
    val token: String? = null
)
