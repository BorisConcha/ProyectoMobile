package com.example.exp1_sem2.model

data class Grabacion(
    val id: String = "",
    val userId: String = "",
    val duracion: String = "",
    val fecha: String = "",
    val hora: String = "",
    val contenido: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
