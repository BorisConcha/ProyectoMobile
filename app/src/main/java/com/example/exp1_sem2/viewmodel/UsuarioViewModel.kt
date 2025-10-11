package com.example.exp1_sem2.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.exp1_sem2.model.Usuario
import com.example.exp1_sem2.interfaces.InterfaceUsuario
import com.google.firebase.firestore.FirebaseFirestore


class UsuarioViewModel: ViewModel(), InterfaceUsuario {
    companion object {
        private val _usuariosGlobales = mutableStateListOf<Usuario>()
        private var inicializado = false
    }

    private val _usuarios = _usuariosGlobales
    val db = FirebaseFirestore.getInstance()

    val usuarios: List<Usuario> get() = _usuarios

    init {
        if (!inicializado) {
            cargarUsuariosDeFirebase()
            inicializado = true
        }
    }

    private fun cargarUsuariosDeFirebase() {
        db.collection("usuarios")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("Error al cargar usuarios: ${error.message}")
                    return@addSnapshotListener
                }

                _usuarios.clear()
                snapshot?.documents?.forEach { document ->
                    val userId = try {
                        document.getLong("id")?.toInt() ?: 0
                    } catch (e: Exception) {
                        document.getString("id")?.toIntOrNull() ?: 0
                    }

                    val usuario = Usuario(
                        id = userId,
                        nombreUsuario = document.getString("nombreUsuario") ?: "",
                        nombre = document.getString("nombre") ?: "",
                        apellidoP = document.getString("apellidoP") ?: "",
                        apellidoM = document.getString("apellidoM") ?: "",
                        correo = document.getString("correo") ?: "",
                        telefono = document.getString("telefono") ?: "",
                        direccion = document.getString("direccion") ?: "",
                        password = document.getString("password") ?: ""
                    )
                    _usuarios.add(usuario)
                }
            }
    }

    private fun obtenerSiguienteId(callback: (Int) -> Unit) {
        val contadorRef = db.collection("contadores").document("usuarios")

        db.runTransaction { transaction ->
            val snapshot = transaction.get(contadorRef)
            val ultimoId = snapshot.getLong("ultimo")?.toInt() ?: 0
            val nuevoId = ultimoId + 1

            transaction.update(contadorRef, "ultimo", nuevoId)

            nuevoId
        }.addOnSuccessListener { nuevoId ->
            callback(nuevoId)
        }.addOnFailureListener { e ->
            println("Error al obtener siguiente ID: ${e.message}")
            callback(0)
        }
    }

    override fun agregarUsuario(usuario: Usuario): Boolean {
        throw UnsupportedOperationException("Usa agregarNewUsuario con callbacks")
    }

    override fun obtenerUsuarioPorNombre(nombreUsuario: String): Usuario? {
        return getUsuarioPorNombreUsuario(nombreUsuario)
    }

    override fun obtenerUsuarioPorCorreo(correo: String): String? {
        val user = getUsuarioPorCorreo(correo)
        return user?.password
    }

    override fun validarUsuarioPorNombre(nombreUsuario: String): Boolean {
        return _usuarios.none { it.nombreUsuario.equals(nombreUsuario, ignoreCase = true) }
    }

    override fun validarUsuarioPorCorreo(correo: String): Boolean {
        return _usuarios.none { it.correo.equals(correo, ignoreCase = true) }
    }

    fun agregarNewUsuario(
        usuario: Usuario,
        onSuccess: (Int) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!validarUsuarioPorNombre(usuario.nombreUsuario)) {
            onError("El nombre de usuario ya existe")
            return
        }

        if (!validarUsuarioPorCorreo(usuario.correo)) {
            onError("El correo ya está registrado")
            return
        }

        obtenerSiguienteId { nuevoId ->
            if (nuevoId == 0) {
                onError("Error al generar ID de usuario")
                return@obtenerSiguienteId
            }

            val datosUsuario = hashMapOf(
                "id" to nuevoId,
                "nombreUsuario" to usuario.nombreUsuario,
                "nombre" to usuario.nombre,
                "apellidoP" to usuario.apellidoP,
                "apellidoM" to usuario.apellidoM,
                "correo" to usuario.correo,
                "telefono" to usuario.telefono,
                "direccion" to usuario.direccion,
                "password" to usuario.password
            )

            db.collection("usuarios")
                .add(datosUsuario)
                .addOnSuccessListener {
                    onSuccess(nuevoId)
                }
                .addOnFailureListener { e ->
                    onError(e.message ?: "Error al agregar usuario")
                }
        }
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

