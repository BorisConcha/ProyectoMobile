package com.example.exp1_sem2.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.exp1_sem2.Recording

class HablarViewModel : ViewModel() {
    val grabaciones = mutableStateListOf<Recording>()
    val estaGrabando = mutableStateOf(false)
    val duracionGrabacion = mutableStateOf("00:00")

    fun iniciarGrabacion() {
        estaGrabando.value = true
        // TODO: Iniciar grabación de audio
    }

    fun detenerGrabacion() {
        estaGrabando.value = false
        // TODO: Guardar grabación
    }

    fun reproducirGrabacion(recording: Recording) {
        // TODO: Reproducir audio
    }

    fun eliminarGrabacion(recording: Recording) {
        grabaciones.remove(recording)
    }

    private fun obtenerFechaActual(): String {
        val formato = java.text.SimpleDateFormat("dd MMM", java.util.Locale("es"))
        return formato.format(java.util.Date())
    }

    private fun obtenerHoraActual(): String {
        val formato = java.text.SimpleDateFormat("HH:mm", java.util.Locale("es"))
        return formato.format(java.util.Date())
    }
}