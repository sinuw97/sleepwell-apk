package com.brydev.sleepwell.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val data: UserData
)

data class UserData(
    val name: String,
    val username: String,
    val email: String,
    val birthdate: String,
    val gender: String
)
