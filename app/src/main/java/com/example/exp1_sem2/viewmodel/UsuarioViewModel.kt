package com.example.exp1_sem2.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.exp1_sem2.model.Usuario
import com.example.exp1_sem2.interfaces.InterfaceUsuario

class UsuarioViewModel: ViewModel(), InterfaceUsuario {

    companion object {
        private val _usuariosGlobales = mutableStateListOf<Usuario>()
        private var inicializado = false
    }
    private val _usuarios = _usuariosGlobales

    val usuarios: List<Usuario> get() = _usuarios

    init {
        if (!inicializado) {
            listaUsuarios()
            inicializado = true
        }
    }

    private fun listaUsuarios() {
        val usuarioslist = mutableListOf(
            Usuario(
                nombreUsuario = "juanito555",
                nombre = "Juan",
                apellidoP = "Perez",
                apellidoM = "Gallego",
                correo = "juanitoperez@gmail.com",
                telefono = "913254687",
                direccion = "Avenida siempre viva 123",
                password = "123456"
            ),
            Usuario(
                nombreUsuario = "mari123",
                nombre = "Maria",
                apellidoP = "Garcia",
                apellidoM = "Soto",
                correo = "mariagarcia@gmail.com",
                telefono = "913254687",
                direccion = "Avenida siempre viva 123",
                password = "maria18"
            ),
            Usuario(
                nombreUsuario = "pepito400",
                nombre = "Pedro",
                apellidoP = "Gonzales",
                apellidoM = "Sanchez",
                correo = "tupedrito14@gmail.com",
                telefono = "913254687",
                direccion = "Avenida siempre viva 123",
                password = "pepe123"
            ),
            Usuario(
                nombreUsuario = "rodrigo531",
                nombre = "Rodrigo",
                apellidoP = "Jimenez",
                apellidoM = "Jimenez",
                correo = "rodrigojj@gmail.com",
                telefono = "913254687",
                direccion = "Avenida siempre viva 123",
                password = "jeyjey531"
            ),
            Usuario(
                nombreUsuario = "manuel700",
                nombre = "Manuel",
                apellidoP = "Zapata",
                apellidoM = "Morales",
                correo = "manuelzapata@gmail.com",
                telefono = "913254687",
                direccion = "Avenida siempre viva 123",
                password = "manu798"
            )
        )

        _usuarios.addAll(usuarioslist)
    }

    override fun agregarUsuario(usuario: Usuario): Boolean {
        return agregarNewUsuario(usuario)
    }

    override fun obtenerUsuarioPorNombre(nombreUsuario: String): Usuario? {
        return getUsuarioPorNombreUsuario(nombreUsuario)
    }

    override fun obtenerUsuarioPorCorreo(correo: String): String? {
        val user = getUsuarioPorCorreo(correo)

        return user?.password
    }

    override fun validarUsuarioPorCorreo(correo: String): Boolean {
        return _usuarios.any { it.correo.equals(correo, ignoreCase = true) }
    }

    override fun validarUsuarioPorNombre(nombreUsuario: String): Boolean {
        return _usuarios.any { it.nombreUsuario.equals(nombreUsuario, ignoreCase = true) }
    }

    fun agregarNewUsuario(usuario: Usuario): Boolean {

        _usuarios.add(usuario)
        return true
    }

    fun getUsuarioPorNombreUsuario(nombreUsuario: String): Usuario? {
        return _usuarios.find {
            it.nombreUsuario.equals(nombreUsuario, ignoreCase = true)
        }
    }

    fun getUsuarioPorCorreo(correo: String): Usuario? {
        return _usuarios.find {
            it.correo.equals(correo, ignoreCase = true)
        }
    }


}