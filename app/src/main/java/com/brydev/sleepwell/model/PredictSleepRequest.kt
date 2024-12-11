package com.brydev.sleepwell.model

data class PredictSleepRequest(
    val age: Int,
    val gender: Int,
    val sleepDuration: Float,
    val awakenings: Float,
    val stayAwake: Float,
    val caffeine: Float,
    val alcohol: Float,
    val smoking: Int,
    val exercise: Float
)
