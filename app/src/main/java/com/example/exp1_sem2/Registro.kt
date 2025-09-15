package com.example.exp1_sem2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exp1_sem2.model.Usuario
import com.example.exp1_sem2.ui.theme.Exp1_Sem2Theme
import com.example.exp1_sem2.ui.theme.*
import com.example.exp1_sem2.viewmodel.UsuarioViewModel

class Registro : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val usuarioViewModel = UsuarioViewModel()
            Exp1_Sem2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = White60
                ) {
                    RegistroView(usuarioViewModel = usuarioViewModel)
                }
            }
        }
    }
}

@Composable
fun RegistroView(usuarioViewModel: UsuarioViewModel) {

    //Variables formulario registro
    val context = LocalContext.current
    var nombreUsuario by remember { mutableStateOf("")}
    var nombre by remember { mutableStateOf("")}
    var apellidoP by remember { mutableStateOf("")}
    var apellidoM by remember { mutableStateOf("")}
    var correo by remember { mutableStateOf("")}
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    var cargando by remember { mutableStateOf(false) }
    var mensajeValidacion by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        //Icono del titulo
        item {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Icono de registro de nuevo usuario",
                tint = PrimaryBlue,
                modifier = Modifier
                    .size(64.dp)
                    .semantics {
                        contentDescription = "Icono que representa el registro de un nuevo usuario"
                    }
            )
        }

        //Titulo
        item {
            Text(
                text = "Registro de Usuario",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color(0xFF212121),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.semantics {
                    contentDescription = "Título de la pagina de registrar usuario"
                },
                textAlign = TextAlign.Center
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = "Información sobre el proceso de registro"
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE3F2FD)
                ),
                border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Icono de información",
                        tint = PrimaryBlue,
                        modifier = Modifier
                            .padding(end = 12.dp, top = 2.dp)
                            .size(24.dp)
                    )
                    Text(
                        text = "Completa todos los campos para crear tu cuenta. Asegúrate de usar un correo válido y una contraseña segura.",
                        fontSize = 16.sp,
                        color = Color(0xFF212121),
                        lineHeight = 24.sp
                    )
                }
            }
        }

        //Nombre Usuario
        item {
            OutlinedTextField(
                value = nombreUsuario,
                onValueChange = { nombreUsuario = it },
                label = {
                    Text(
                        "Nombre de Usuario",
                        color = Grey40,
                        fontSize = 16.sp
                    )
                },
                placeholder = {
                    Text(
                        "usuario123",
                        color = Grey40.copy(alpha = 0.7f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .semantics {
                        contentDescription = "Campo para ingresar nombre de usuario único"
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = Grey40,
                    focusedTextColor = Black40,
                    unfocusedTextColor = Black40,
                    focusedContainerColor = WhiteSurface,
                    unfocusedContainerColor = WhiteSurface,
                    cursorColor = PrimaryBlue,
                    focusedLabelColor = PrimaryBlue,
                    unfocusedLabelColor = Grey40
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp)
            )
        }

        //Nombre
        item {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = {
                    Text(
                        "Nombre",
                        color = Grey40,
                        fontSize = 16.sp
                    )
                },
                placeholder = {
                    Text(
                        "Tu nombre",
                        color = Grey40.copy(alpha = 0.7f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .semantics {
                        contentDescription = "Campo del nombre"
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = Grey40,
                    focusedTextColor = Black40,
                    unfocusedTextColor = Black40,
                    focusedContainerColor = WhiteSurface,
                    unfocusedContainerColor = WhiteSurface,
                    cursorColor = PrimaryBlue,
                    focusedLabelColor = PrimaryBlue,
                    unfocusedLabelColor = Grey40
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words
                )
            )
        }

        //Apellido paterno
        item {
            OutlinedTextField(
                value = apellidoP,
                onValueChange = { apellidoP = it },
                label = {
                    Text(
                        "Apellido Paterno",
                        color = Grey40,
                        fontSize = 16.sp
                    )
                },
                placeholder = {
                    Text(
                        "Tu apellido paterno",
                        color = Grey40.copy(alpha = 0.7f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .semantics {
                        contentDescription = "Campo del apellido paterno"
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = Grey40,
                    focusedTextColor = Black40,
                    unfocusedTextColor = Black40,
                    focusedContainerColor = WhiteSurface,
                    unfocusedContainerColor = WhiteSurface,
                    cursorColor = PrimaryBlue,
                    focusedLabelColor = PrimaryBlue,
                    unfocusedLabelColor = Grey40
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                )
            )
        }

        //Apellido materno
        item {
            OutlinedTextField(
                value = apellidoM,
                onValueChange = { apellidoM = it },
                label = {
                    Text(
                        "Apellido Materno",
                        color = Grey40,
                        fontSize = 16.sp
                    )
                },
                placeholder = {
                    Text(
                        "Tu apellido materno",
                        color = Grey40.copy(alpha = 0.7f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .semantics {
                        contentDescription = "Campo del apellido materno"
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = Grey40,
                    focusedTextColor = Black40,
                    unfocusedTextColor = Black40,
                    focusedContainerColor = WhiteSurface,
                    unfocusedContainerColor = WhiteSurface,
                    cursorColor = PrimaryBlue,
                    focusedLabelColor = PrimaryBlue,
                    unfocusedLabelColor = Grey40
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                )
            )
        }

        //Correo
        item {
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = {
                    Text(
                        "Correo Electrónico",
                        color = Grey40,
                        fontSize = 16.sp
                    )
                },
                placeholder = {
                    Text(
                        "ejemplo@correo.com",
                        color = Grey40.copy(alpha = 0.7f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .semantics {
                        contentDescription = "Campo del correo electronico"
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = Grey40,
                    focusedTextColor = Black40,
                    unfocusedTextColor = Black40,
                    focusedContainerColor = WhiteSurface,
                    unfocusedContainerColor = WhiteSurface,
                    cursorColor = PrimaryBlue,
                    focusedLabelColor = PrimaryBlue,
                    unfocusedLabelColor = Grey40
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )
        }

        item {
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it},
                label = {
                    Text(
                        "Telefono",
                        color = Grey40,
                        fontSize = 16.sp
                    )
                },
                placeholder = {
                    Text(
                        "Exactamente 9 digitos",
                        color = Grey40.copy(alpha = 0.7f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .semantics {
                        contentDescription = "Campo del telefono"
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = Grey40,
                    focusedTextColor = Black40,
                    unfocusedTextColor = Black40,
                    focusedContainerColor = WhiteSurface,
                    unfocusedContainerColor = WhiteSurface,
                    cursorColor = PrimaryBlue,
                    focusedLabelColor = PrimaryBlue,
                    unfocusedLabelColor = Grey40
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                )
            )
        }

        item {
            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = {
                    Text(
                        "Direccion",
                        color = Grey40,
                        fontSize = 16.sp
                    )
                },
                placeholder = {
                    Text(
                        "Tu direccion",
                        color = Grey40.copy(alpha = 0.7f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .semantics {
                        contentDescription = "Campo direccion"
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = Grey40,
                    focusedTextColor = Black40,
                    unfocusedTextColor = Black40,
                    focusedContainerColor = WhiteSurface,
                    unfocusedContainerColor = WhiteSurface,
                    cursorColor = PrimaryBlue,
                    focusedLabelColor = PrimaryBlue,
                    unfocusedLabelColor = Grey40
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                )
            )
        }

        //Contraseña
        item {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(
                        "Contraseña",
                        color = Grey40,
                        fontSize = 16.sp
                    )
                },
                placeholder = {
                    Text(
                        "Mínimo 6 caracteres",
                        color = Grey40.copy(alpha = 0.7f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .semantics {
                        contentDescription = "Campo de la contraseña"
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = Grey40,
                    focusedTextColor = Black40,
                    unfocusedTextColor = Black40,
                    focusedContainerColor = WhiteSurface,
                    unfocusedContainerColor = WhiteSurface,
                    cursorColor = PrimaryBlue,
                    focusedLabelColor = PrimaryBlue,
                    unfocusedLabelColor = Grey40
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp),
                visualTransformation = PasswordVisualTransformation()
            )
        }

        if (mensajeValidacion.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = "Mensaje de validación: $mensajeValidacion"
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Error.copy(alpha = 0.1f)
                    ),
                    border = BorderStroke(1.dp, Error),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Icono de advertencia",
                            tint = Error,
                            modifier = Modifier
                                .padding(end = 12.dp, top = 2.dp)
                                .size(24.dp)
                        )
                        Text(
                            text = mensajeValidacion,
                            color = Error,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 24.sp
                        )
                    }
                }
            }
        }

        //Boton agregar usuario
        item {
            Button(
                onClick = {
                    if (nombreUsuario.isNotBlank() && nombre.isNotBlank() &&
                        apellidoP.isNotBlank() && apellidoM.isNotBlank() &&
                        correo.isNotBlank() && telefono.isNotBlank() &&
                        direccion.isNotBlank() && password.isNotBlank()) {

                        if (password.length < 6) {
                            mensajeValidacion = "La contraseña debe tener al menos 6 caracteres"
                            return@Button
                        }

                        if (telefono.length != 9) {
                            mensajeValidacion = "El telefono debe tener exactamente 9 digitos"
                            return@Button
                        }

                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                            mensajeValidacion = "El formato del correo electrónico no es válido"
                            return@Button
                        }

                        if (usuarioViewModel.validarUsuarioPorNombre(nombreUsuario)) {
                            mensajeValidacion = "El nombre de usuario ya existe"
                            return@Button
                        }

                        if (usuarioViewModel.validarUsuarioPorCorreo(correo)) {
                            mensajeValidacion = "El correo electrónico ya está registrado"
                            return@Button
                        }

                        mensajeValidacion = ""
                        cargando = true

                        val nuevoUsuario = Usuario(
                            nombreUsuario = nombreUsuario,
                            nombre = nombre,
                            apellidoP = apellidoP,
                            apellidoM = apellidoM,
                            correo = correo,
                            telefono = telefono,
                            direccion = direccion,
                            password = password
                        )

                        val usuarioAgregado = usuarioViewModel.agregarUsuario(nuevoUsuario)

                        if (usuarioAgregado) {
                            Toast.makeText(context, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        } else {
                            mensajeValidacion = "Error al crear el usuario"
                        }

                        cargando = false
                    } else {
                        mensajeValidacion = "Por favor completa todos los campos requeridos"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .semantics {
                        contentDescription = "Botón para crear cuenta"
                        role = Role.Button
                    },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White,
                    disabledContainerColor = Grey40.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                enabled = !cargando
            ) {
                if (cargando) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Creando cuenta...",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Icono de registro",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Crear Cuenta",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        //Boton volver al login
        item {
            TextButton(
                onClick = {
                    if (context is Activity) {
                        context.finish()
                    }
                },
                modifier = Modifier.semantics {
                    contentDescription = "Retornar al inicio de sesion"
                    role = Role.Button
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Icono de regresar",
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "¿Ya tienes cuenta? Inicia Sesión",
                        color = PrimaryBlue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroPreview() {
    Exp1_Sem2Theme {
        RegistroView(usuarioViewModel = UsuarioViewModel())
    }
}