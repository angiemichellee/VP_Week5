package com.example.week5.Soal1.Model

import java.util.UUID

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val amount: Long
)