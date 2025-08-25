package com.example.exp1_sem2

import androidx.compose.runtime.mutableStateOf

data class Usuarios (
    val nombreUsuario: String,
    val nombre: String,
    val apellidoP: String,
    val apellidoM: String,
    val correo: String,
    val password: String,
)

object UsuarioRepositorio {

    val usuarioslist = mutableListOf<Usuarios>()

    fun agregarUsuario(usuario : Usuarios){
        usuarioslist.add(usuario)
    }

    fun getUsuario() : List<Usuarios>{
        return usuarioslist.toList()
    }
}