package com.example.exp1_sem2.interfaces

import com.example.exp1_sem2.model.Usuario

interface InterfaceUsuario {

    fun agregarUsuario(usuario: Usuario): Boolean

    fun obtenerUsuarioPorNombre(nombreUsuario: String): Usuario?

    fun obtenerUsuarioPorCorreo(correo: String): String?

    fun validarUsuarioPorCorreo(correo: String): Boolean

    fun validarUsuarioPorNombre(nombreUsuario: String): Boolean
}