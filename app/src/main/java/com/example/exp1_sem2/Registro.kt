package com.example.exp1_sem2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.material3.Scaffold
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
import com.example.exp1_sem2.ui.theme.Exp1_Sem2Theme

class Registro : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Exp1_Sem2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFE8E5DA)
                ) {
                    RegistroView()
                }
            }
        }
    }
}

@Preview
@Composable
fun RegistroView(){

    //Variables formulario registro
    val context = LocalContext.current
    var nombreUsuario by remember { mutableStateOf("")}
    var nombre by remember { mutableStateOf("")}
    var apellidoP by remember { mutableStateOf("")}
    var apellidoM by remember { mutableStateOf("")}
    var correo by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    var cargando by remember { mutableStateOf(false) }
    var mensajeValidacion by remember { mutableStateOf("") }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
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
                tint = Color(0xFF1565C0),
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
                    contentDescription = "Título de la pantalla: Registro de Usuario"
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
                border = BorderStroke(1.dp, Color(0xFF1565C0).copy(alpha = 0.3f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Icono de información",
                        tint = Color(0xFF1565C0),
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
                        color = Color(0xFF616161),
                        fontSize = 16.sp
                    )
                },
                placeholder = {
                    Text(
                        "usuario123",
                        color = Color(0xFF616161).copy(alpha = 0.7f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .semantics {
                        contentDescription = "Campo para ingresar nombre de usuario único"
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1565C0),
                    unfocusedBorderColor = Color(0xFF616161),
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedContainerColor = Color(0xFFFFFFFF),
                    unfocusedContainerColor = Color(0xFFFFFFFF),
                    cursorColor = Color(0xFF1565C0),
                    focusedLabelColor = Color(0xFF1565C0),
                    unfocusedLabelColor = Color(0xFF616161)
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
                        color = Color(0xFF616161),
                        fontSize = 16.sp
                    )
                },
                placeholder = {
                    Text(
                        "Tu nombre",
                        color = Color(0xFF616161).copy(alpha = 0.7f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .semantics {
                        contentDescription = "Campo para ingresar tu nombre"
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1565C0),
                    unfocusedBorderColor = Color(0xFF616161),
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedContainerColor = Color(0xFFFFFFFF),
                    unfocusedContainerColor = Color(0xFFFFFFFF),
                    cursorColor = Color(0xFF1565C0),
                    focusedLabelColor = Color(0xFF1565C0),
                    unfocusedLabelColor = Color(0xFF616161)
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
                        color = Color(0xFF616161),
                        fontSize = 16.sp
                    )
                },
                placeholder = {
                    Text(
                        "Tu apellido paterno",
                        color = Color(0xFF616161).copy(alpha = 0.7f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .semantics {
                        contentDescription = "Campo para ingresar tu apellido paterno"
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1565C0),
                    unfocusedBorderColor = Color(0xFF616161),
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedContainerColor = Color(0xFFFFFFFF),
                    unfocusedContainerColor = Color(0xFFFFFFFF),
                    cursorColor = Color(0xFF1565C0),
                    focusedLabelColor = Color(0xFF1565C0),
                    unfocusedLabelColor = Color(0xFF616161)
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
                        color = Color(0xFF616161),
                        fontSize = 16.sp
                    )
                },
                placeholder = {
                    Text(
                        "Tu apellido materno",
                        color = Color(0xFF616161).copy(alpha = 0.7f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .semantics {
                        contentDescription = "Campo para ingresar tu apellido materno"
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1565C0),
                    unfocusedBorderColor = Color(0xFF616161),
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedContainerColor = Color(0xFFFFFFFF),
                    unfocusedContainerColor = Color(0xFFFFFFFF),
                    cursorColor = Color(0xFF1565C0),
                    focusedLabelColor = Color(0xFF1565C0),
                    unfocusedLabelColor = Color(0xFF616161)
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
                        color = Color(0xFF616161),
                        fontSize = 16.sp
                    )
                },
                placeholder = {
                    Text(
                        "ejemplo@correo.com",
                        color = Color(0xFF616161).copy(alpha = 0.7f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .semantics {
                        contentDescription = "Campo para ingresar tu correo electrónico"
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1565C0),
                    unfocusedBorderColor = Color(0xFF616161),
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedContainerColor = Color(0xFFFFFFFF),
                    unfocusedContainerColor = Color(0xFFFFFFFF),
                    cursorColor = Color(0xFF1565C0),
                    focusedLabelColor = Color(0xFF1565C0),
                    unfocusedLabelColor = Color(0xFF616161)
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
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
                        color = Color(0xFF616161),
                        fontSize = 16.sp
                    )
                },
                placeholder = {
                    Text(
                        "Mínimo 6 caracteres",
                        color = Color(0xFF616161).copy(alpha = 0.7f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .semantics {
                        contentDescription = "Campo para crear tu contraseña"
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1565C0),
                    unfocusedBorderColor = Color(0xFF616161),
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    focusedContainerColor = Color(0xFFFFFFFF),
                    unfocusedContainerColor = Color(0xFFFFFFFF),
                    cursorColor = Color(0xFF1565C0),
                    focusedLabelColor = Color(0xFF1565C0),
                    unfocusedLabelColor = Color(0xFF616161)
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
                        containerColor = Color(0xFFD32F2F).copy(alpha = 0.1f)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Icono de advertencia",
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier
                                .padding(end = 12.dp, top = 2.dp)
                                .size(24.dp)
                        )
                        Text(
                            text = mensajeValidacion,
                            color = Color(0xFFD32F2F),
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
                        correo.isNotBlank() && password.isNotBlank()) {

                        if (password.length < 6) {
                            mensajeValidacion = "La contraseña debe tener al menos 6 caracteres"
                            return@Button
                        }

                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                            mensajeValidacion = "El formato del correo electrónico no es válido"
                            return@Button
                        }

                        mensajeValidacion = ""
                        cargando = true

                        val nuevoUsuario = Usuarios(
                            nombreUsuario,
                            nombre,
                            apellidoP,
                            apellidoM,
                            correo,
                            password
                        )

                        UsuarioRepositorio.agregarUsuario(nuevoUsuario)

                        Toast.makeText(context, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()

                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)

                        cargando = false
                    } else {
                        mensajeValidacion = "Por favor completa todos los campos requeridos"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .semantics {
                        contentDescription = "Botón para registrar nueva cuenta de usuario con todos los datos ingresados"
                        role = Role.Button
                    },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1565C0),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF616161).copy(alpha = 0.3f)
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
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.semantics {
                    contentDescription = "Enlace para regresar a la pantalla de inicio de sesión"
                    role = Role.Button
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Icono de regresar",
                        tint = Color(0xFF1565C0),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "¿Ya tienes cuenta? Inicia Sesión",
                        color = Color(0xFF1565C0),
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