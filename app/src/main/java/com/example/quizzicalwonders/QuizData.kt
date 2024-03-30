package com.example.quizzicalwonders

data class QuizData(
    val question: String = "",
    val options: Map<String, String> = emptyMap(), // Map of options (option key to option value)
    val correctAnswer: String = ""
)


