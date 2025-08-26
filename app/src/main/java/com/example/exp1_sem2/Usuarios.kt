package com.example.exp1_sem2

import android.R.id


data class Usuarios (
    val nombreUsuario: String,
    val nombre: String,
    val apellidoP: String,
    val apellidoM: String,
    val correo: String,
    val password: String,
)

object UsuarioRepositorio {

    private val usuarioslist = mutableListOf(
        Usuarios(
            nombreUsuario = "juanito555",
            nombre = "Juan",
            apellidoP = "Perez",
            apellidoM = "Gallego",
            correo = "juanitoperez@gmail.com",
            password = "123456"
        ),
        Usuarios(
            nombreUsuario = "mari123",
            nombre = "Maria",
            apellidoP = "Garcia",
            apellidoM = "Soto",
            correo = "mariagarcia@gmail.com",
            password = "maria18"
        ),
        Usuarios(
            nombreUsuario = "pepito400",
            nombre = "Pedro",
            apellidoP = "Gonzales",
            apellidoM = "Sanchez",
            correo = "tupedrito14@gmail.com",
            password = "pepe123"
        ),
        Usuarios(
            nombreUsuario = "rodrigo531",
            nombre = "Rodrigo",
            apellidoP = "Jimenez",
            apellidoM = "Jimenez",
            correo = "rodrigojj@gmail.com",
            password = "jeyjey531"
        ),
        Usuarios(
            nombreUsuario = "manuel700",
            nombre = "Manuel",
            apellidoP = "Zapata",
            apellidoM = "Morales",
            correo = "manuelzapata@gmail.com",
            password = "manu798"
        ),
    )

    fun agregarUsuario(usuario : Usuarios){
        usuarioslist.add(usuario)
    }

    fun getUsuario() : List<Usuarios>{
        return usuarioslist.toList()
    }

    fun getUsuarioPorNombreUsuario(nombreUsuario: String): Usuarios? {
        return usuarioslist.find { it.nombreUsuario == nombreUsuario }
    }

    fun getUsuarioPorCorreo(correo: String): Usuarios? {
        return usuarioslist.find { it.correo == correo }
    }

    fun validarLogin(nombreUsuario: String, password: String): Boolean {
        val usuario = getUsuarioPorNombreUsuario(nombreUsuario)
        return usuario?.password == password
    }

    fun buscarUsuarioPorCorreo(correo: String): String? {
        val usuario = getUsuarioPorCorreo(correo)
        return usuario?.password
    }
}