package com.brydev.sleepwell.model

data class Question(
    val text: String,
    val key: String,
    val inputType: InputType,
    val options: List<String>? = null
)

enum class InputType {
    NUMBER, RADIO
}
