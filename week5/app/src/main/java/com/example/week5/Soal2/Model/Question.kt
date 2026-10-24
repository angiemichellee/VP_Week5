package com.example.week5.Soal2.Model

data class Question(
    val id: Int,
    val text: String,
    val options: List<String>,
    val correctAnswer: String
)

data class AnswerRecord(
    val questionNumber: Int,
    val questionText: String,
    val selectedAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean
)