package com.example.exp1_sem2.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.exp1_sem2.model.Usuario
import com.example.exp1_sem2.interfaces.InterfaceUsuario

class UsuarioViewModel: ViewModel(), InterfaceUsuario {
    private val _usuarios = mutableStateListOf<Usuario>()

    val usuarios: List<Usuario> get() = _usuarios

    var mensajeLogin by mutableStateOf("")
        private set

    var loginExitoso by mutableStateOf(false)
        private set

    init {
        listaUsuarios()
    }

    private fun listaUsuarios() {
        val usuarioslist = listOf(
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

    override fun obtenerUsuarioPorCorreo(correo: String): Usuario? {
        return getUsuarioPorCorreo(correo)
    }

    override fun validarUsuarioPorCorreo(correo: String): Boolean {
        return _usuarios.any { it.correo.equals(correo, ignoreCase = true) }
    }

    override fun validarUsuarioPorNombre(nombreUsuario: String): Boolean {
        return _usuarios.any { it.nombreUsuario.equals(nombreUsuario, ignoreCase = true) }
    }

    fun agregarNewUsuario(usuario: Usuario): Boolean {
        if (existeUsuario(usuario.nombreUsuario, usuario.correo)) {
            return false
        }

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

    fun validarLogin(nombreUsuario: String, password: String) {
        val usuario = getUsuarioPorNombreUsuario(nombreUsuario)

        if (usuario != null && usuario.password == password) {
            loginExitoso = true
            mensajeLogin = "Se inicio sesion correctamente"
        } else {
            loginExitoso = false
            mensajeLogin = "Credenciales incorrectas"
        }
    }

    fun buscarUsuariobyCorreo(correo: String): String? {
        return getUsuarioPorCorreo(correo)?.password
    }

    fun recuperarPassword(correo: String): String {
        val usuario = getUsuarioPorCorreo(correo)
        return if (usuario != null) {
            "Tu contraseña es: ${usuario.password}"
        } else {
            "No se encontró un usuario con ese correo"
        }
    }

    fun limpiarMensajes() {
        mensajeLogin = ""
        loginExitoso = false
    }

    private fun existeUsuario(nombreUsuario: String, correo: String): Boolean {
        return _usuarios.any {
            it.nombreUsuario.equals(nombreUsuario, ignoreCase = true) ||
                    it.correo.equals(correo, ignoreCase = true)
        }
    }

    fun getTotalUsuarios(): Int = _usuarios.size

}