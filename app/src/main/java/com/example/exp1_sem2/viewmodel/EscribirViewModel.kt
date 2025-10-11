package com.example.exp1_sem2.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.exp1_sem2.interfaces.InterfaceEscribir
import com.example.exp1_sem2.model.Nota
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*

class EscribirViewModel(private val nombreUsuario: String = ""): ViewModel(), InterfaceEscribir {
    companion object {
        private val _notasGlobales = mutableStateListOf<Nota>()
        private var inicializado = false
        private var usuarioActual: String = ""
    }

    private val _notas = _notasGlobales
    val notas: List<Nota> get() = _notas

    val db = FirebaseFirestore.getInstance()

    init {
        usuarioActual = nombreUsuario
        if (!inicializado || _notasGlobales.isEmpty()) {
            cargarNotasDeFirebase()
            inicializado = true
        }
    }

    private fun cargarNotasDeFirebase() {
        if (usuarioActual.isEmpty()) {
            println("Error: nombreUsuario está vacío")
            return
        }

        println("Cargando notas para usuario: $usuarioActual")

        db.collection("notes")
            .whereEqualTo("userId", usuarioActual)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("Error al cargar notas: ${error.message}")
                    return@addSnapshotListener
                }

                _notas.clear()
                snapshot?.documents?.forEach { document ->
                    println("Documento encontrado: ${document.id}")
                    val nota = Nota(
                        id = document.id,
                        userId = document.getString("userId") ?: "",
                        fecha = document.getString("fecha") ?: "",
                        hora = document.getString("hora") ?: "",
                        contenido = document.getString("contenido") ?: "",
                        vista = document.getString("vista") ?: "",
                        palabras = document.getLong("palabras")?.toInt() ?: 0,
                        timestamp = document.getLong("timestamp") ?: 0L
                    )
                    _notas.add(nota)
                }
                println("Total de notas cargadas: ${_notas.size}")
            }
    }

    override fun agregarNota(nota: Nota): Boolean {
        return validarContenido(nota.contenido)
    }

    override fun obtenerNotaPorId(idNota: String): Nota? {
        return _notas.find { it.id == idNota }
    }

    override fun obtenerNotasPorUsuario(userId: String): List<Nota> {
        return _notas.filter { it.userId == userId }
    }

    override fun actualizarNota(idNota: String, contenido: String): Boolean {
        return validarContenido(contenido)
    }

    override fun eliminarNota(idNota: String): Boolean {
        return idNota.isNotEmpty()
    }

    override fun validarContenido(contenido: String): Boolean {
        return contenido.isNotBlank() && contenido.trim().isNotEmpty()
    }

    fun guardarNota(
        contenido: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!validarContenido(contenido)) {
            onError("El contenido no puede estar vacío")
            return
        }

        if (usuarioActual.isEmpty()) {
            onError("Error: Usuario no identificado")
            return
        }

        val fechaActual = Calendar.getInstance()
        val formatoFecha = SimpleDateFormat("d MMM", Locale("es", "ES"))
        val formatoHora = SimpleDateFormat("HH:mm", Locale.getDefault())

        val contadorPalabras = contenido.trim().split("\\s+".toRegex()).size
        val vistaPrevia = if (contenido.length > 100) {
            contenido.substring(0, 100) + "..."
        } else {
            contenido
        }

        val datosNota = hashMapOf(
            "userId" to usuarioActual,
            "fecha" to formatoFecha.format(fechaActual.time),
            "hora" to formatoHora.format(fechaActual.time),
            "contenido" to contenido,
            "vista" to vistaPrevia,
            "palabras" to contadorPalabras,
            "timestamp" to System.currentTimeMillis()
        )

        println("Guardando nota para usuario: $usuarioActual")

        db.collection("notes").add(datosNota)
            .addOnSuccessListener { documentReference ->
                documentReference.update("id", documentReference.id)
                println("Nota guardada exitosamente con ID: ${documentReference.id}")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                println("Error al guardar nota: ${exception.message}")
                onError(exception.message ?: "Error al guardar la nota")
            }
    }

    fun actualizarNotaAsync(
        idNota: String,
        contenido: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!validarContenido(contenido)) {
            onError("El contenido no puede estar vacío")
            return
        }

        val contadorPalabras = contenido.trim().split("\\s+".toRegex()).size
        val vistaPrevia = if (contenido.length > 100) {
            contenido.substring(0, 100) + "..."
        } else {
            contenido
        }

        val actualizaciones = hashMapOf<String, Any>(
            "contenido" to contenido,
            "vista" to vistaPrevia,
            "palabras" to contadorPalabras,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("notes").document(idNota).update(actualizaciones)
            .addOnSuccessListener {
                println("Nota actualizada exitosamente")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                println("Error al actualizar nota: ${exception.message}")
                onError(exception.message ?: "Error al actualizar la nota")
            }
    }

    fun eliminarNotaAsync(
        idNota: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("notes").document(idNota).delete()
            .addOnSuccessListener {
                println("Nota eliminada exitosamente")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                println("Error al eliminar nota: ${exception.message}")
                onError(exception.message ?: "Error al eliminar la nota")
            }
    }
}