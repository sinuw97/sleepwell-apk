package com.brydev.sleepwell.model

data class PredictionResult(
    val label: String,
    val prediction: String,
    val suggestion: String,
    val createdAt: String
)
