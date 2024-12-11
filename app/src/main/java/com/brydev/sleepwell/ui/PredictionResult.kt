package com.brydev.sleepwell.ui

data class ApiResponse(
    val status: String,
    val message: String,
    val data: PredictionResult
)

data class PredictionResult(
    val predictId: String,
    val userId: String,
    val label: String,
    val prediction: String,
    val suggestion: String,
    val createdAt: String
)
