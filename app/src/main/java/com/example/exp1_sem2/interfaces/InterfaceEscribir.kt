package com.example.exp1_sem2.interfaces

import com.example.exp1_sem2.model.Nota

interface InterfaceEscribir {

    fun agregarNota(nota: Nota): Boolean

    fun obtenerNotaPorId(idNota: String): Nota?

    fun obtenerNotasPorUsuario(userId: String): List<Nota>

    fun actualizarNota(idNota: String, contenido: String): Boolean

    fun eliminarNota(idNota: String): Boolean

    fun validarContenido(contenido: String): Boolean
}