package com.example.exp1_sem2.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.exp1_sem2.model.Grabacion
import com.example.exp1_sem2.interfaces.InterfaceHablar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HablarViewModel(private val nombreUsuario: String = "") : ViewModel(), InterfaceHablar {
    private val _grabaciones = mutableStateListOf<Grabacion>()
    val grabaciones: List<Grabacion> get() = _grabaciones

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var usuarioActual: String = ""

    private var grabacionesListener: ListenerRegistration? = null

    init {
        usuarioActual = if (nombreUsuario.isNotEmpty()) {
            nombreUsuario
        } else {
            auth.currentUser?.uid ?: ""
        }

        cargarGrabacionesDeFirebase()
    }

    private fun cargarGrabacionesDeFirebase() {
        if (usuarioActual.isEmpty()) {
            return
        }

        grabacionesListener?.remove()

        grabacionesListener = db.collection("grabaciones")
            .whereEqualTo("userId", usuarioActual)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                _grabaciones.clear()
                snapshot?.documents?.forEach { document ->
                    println("📄 Documento encontrado: ${document.id} - userId: ${document.getString("userId")}")
                    val grabacion = Grabacion(
                        id = document.id,
                        userId = document.getString("userId") ?: "",
                        duracion = document.getString("duracion") ?: "",
                        fecha = document.getString("fecha") ?: "",
                        hora = document.getString("hora") ?: "",
                        contenido = document.getString("contenido") ?: "",
                        timestamp = document.getLong("timestamp") ?: 0L
                    )
                    _grabaciones.add(grabacion)
                }
            }
    }

    override fun agregarGrabacion(grabacion: Grabacion): Boolean {
        return validarContenido(grabacion.contenido)
    }

    override fun obtenerGrabacionPorId(idGrabacion: String): Grabacion? {
        return _grabaciones.find { it.id == idGrabacion }
    }

    override fun obtenerGrabacionesPorUsuario(userId: String): List<Grabacion> {
        return _grabaciones.filter { it.userId == userId }
    }

    override fun eliminarGrabacion(idGrabacion: String): Boolean {
        return idGrabacion.isNotEmpty()
    }

    override fun validarContenido(contenido: String): Boolean {
        return contenido.isNotBlank() && contenido.trim().isNotEmpty()
    }

    fun guardarGrabacion(
        contenido: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!validarContenido(contenido)) {
            onError("El contenido no puede estar vacío")
            return
        }

        if (usuarioActual.isEmpty()) {
            onError("Error: Usuario no identificado. Por favor, inicia sesión.")
            return
        }

        val fechaActual = Calendar.getInstance()
        val formatoFecha = SimpleDateFormat("d MMM", Locale("es", "ES"))
        val formatoHora = SimpleDateFormat("HH:mm", Locale.getDefault())

        val palabras = contenido.trim().split("\\s+".toRegex()).size
        val minutosEstimados = palabras / 150
        val segundosEstimados = ((palabras % 150) * 60) / 150
        val duracionEstimada = String.format("%d:%02d", minutosEstimados, segundosEstimados)

        val datosGrabacion = hashMapOf(
            "userId" to usuarioActual,
            "fecha" to formatoFecha.format(fechaActual.time),
            "hora" to formatoHora.format(fechaActual.time),
            "contenido" to contenido,
            "duracion" to duracionEstimada,
            "timestamp" to System.currentTimeMillis()
        )


        db.collection("grabaciones").add(datosGrabacion)
            .addOnSuccessListener { documentReference ->
                documentReference.update("id", documentReference.id)
                onSuccess()
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                onError(exception.message ?: "Error al guardar la grabación")
            }
    }

    fun eliminarGrabacionAsync(
        idGrabacion: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (idGrabacion.isEmpty()) {
            onError("ID de grabación inválido")
            return
        }

        db.collection("grabaciones").document(idGrabacion).delete()
            .addOnSuccessListener {
                _grabaciones.removeAll { it.id == idGrabacion }
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error al eliminar la grabación")
            }
    }

    override fun onCleared() {
        super.onCleared()
        grabacionesListener?.remove()
    }

}