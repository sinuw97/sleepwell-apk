package com.brydev.sleepwell.model

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String?,
    @SerializedName("gender") val gender: String,
    @SerializedName("dob") val dob: String
)
