package com.brydev.sleepwell.ui

import java.io.Serializable

data class PredictionResult(
    val label: String,
    val prediction: Float,
    val suggestion: String
) : Serializable
