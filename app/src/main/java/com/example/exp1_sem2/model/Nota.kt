package com.example.exp1_sem2.model

data class Nota(
    val id: String = "",
    val userId: String = "",
    val fecha: String = "",
    val hora: String = "",
    val contenido: String = "",
    val vista: String = "",
    val palabras: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
