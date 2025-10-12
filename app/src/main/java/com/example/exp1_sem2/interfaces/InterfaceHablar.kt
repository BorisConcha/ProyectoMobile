package com.example.exp1_sem2.interfaces

import com.example.exp1_sem2.model.Grabacion

interface InterfaceHablar {

    fun agregarGrabacion(grabacion: Grabacion): Boolean

    fun obtenerGrabacionPorId(idGrabacion: String): Grabacion?

    fun obtenerGrabacionesPorUsuario(userId: String): List<Grabacion>

    fun eliminarGrabacion(idGrabacion: String): Boolean

    fun validarContenido(contenido: String): Boolean
}