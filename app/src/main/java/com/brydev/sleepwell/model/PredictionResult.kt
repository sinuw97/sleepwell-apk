package com.brydev.sleepwell.model

data class PredictionResult(
    val result: String, // Tambahkan field sesuai dengan respons API
    val confidence: Float
)
