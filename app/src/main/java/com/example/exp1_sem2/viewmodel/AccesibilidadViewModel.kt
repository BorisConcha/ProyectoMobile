package com.example.exp1_sem2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AccesibilidadViewModel : ViewModel() {
    var tamanoFuente by mutableStateOf(16f)
    var modoOscuro by mutableStateOf(false)

    fun aumentarFuente() {
        if (tamanoFuente < 24f) tamanoFuente += 2f
    }

    fun disminuirFuente() {
        if (tamanoFuente > 12f) tamanoFuente -= 2f
    }

    fun cambiarModo() {
        modoOscuro = !modoOscuro
    }
}