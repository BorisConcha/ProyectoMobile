package com.example.exp1_sem2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exp1_sem2.ui.theme.Exp1_Sem2Theme
import com.example.exp1_sem2.ui.theme.*
import com.example.exp1_sem2.viewmodel.AccesibilidadViewModel
import com.example.exp1_sem2.viewmodel.UsuarioViewModel
import kotlin.getValue


class RecuperarPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val usuarioViewModel = UsuarioViewModel()
            val accesibilidadVM: AccesibilidadViewModel by viewModels()
            var mostrarMenu by remember { mutableStateOf(false) }
            Exp1_Sem2Theme(darkTheme = accesibilidadVM.modoOscuro,
                tamanoFuente = accesibilidadVM.tamanoFuente) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            RecuperarPasswordView(usuarioViewModel = usuarioViewModel)
                        }

                        IconButton(
                            onClick = { mostrarMenu = true },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 60.dp, end = 16.dp)
                        ) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = "Configuración",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        if (mostrarMenu) {
                            AlertDialog(
                                onDismissRequest = { mostrarMenu = false },
                                title = { Text("Accesibilidad") },
                                text = {
                                    Accesibilidad(viewModel = accesibilidadVM)
                                },
                                confirmButton = {
                                    TextButton(onClick = { mostrarMenu = false }) {
                                        Text("Cerrar")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun RecuperarPasswordView(usuarioViewModel: UsuarioViewModel){

    val context = LocalContext.current
    var correo by remember { mutableStateOf("")}
    var mensajeRecuperarPassword by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Icono de candado del recuperar contraseña",
            tint = PrimaryBlue,
            modifier = Modifier
                .size(64.dp)
                .padding(bottom = 16.dp)
                .semantics {
                    contentDescription = "Icono de candado"
                }
        )

        Text(
            text = "Recuperar Contraseña",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .semantics {
                    contentDescription = "Título pagina de recuperar contraseña"
                },
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .semantics {
                    contentDescription = "Información sobre recuperar contraseña"
                },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            ),
            border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    text = "Te ayudaremos a recuperar tu cuenta. Ingresa tu correo electrónico registrado en el sistema para poder recuperar tus datos.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Black40,
                    lineHeight = 24.sp,
                    modifier = Modifier.semantics {
                        contentDescription =
                            "Instrucciones: Ingresa tu correo electrónico para recuperar tu contraseña"
                    }
                )
            }
        }

        //Correo Electronico
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = {
                Text(
                    "Correo Electrónico",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            placeholder = {
                Text(
                    "ejemplo@correo.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Icono de correo electrónico",
                    tint = if (correo.isEmpty()) Grey40 else PrimaryBlue
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .semantics {
                    contentDescription = "Campo para ingresar tu correo electrónico"
                },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                cursorColor = PrimaryBlue,
                focusedLabelColor = PrimaryBlue,
                unfocusedLabelColor = Grey40
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            textStyle = TextStyle(fontSize = 16.sp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        //Boton de enviar correo
        Button(
            onClick = {
                cargando = true
                val password = usuarioViewModel.obtenerUsuarioPorCorreo(correo)
                if (password != null) {
                    mensajeRecuperarPassword = "Tu contraseña es: $password"
                } else {
                    mensajeRecuperarPassword = "Correo incorrecto o no existe en el sistema"
                }
                cargando = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .semantics {
                    contentDescription =
                        "Botón para recuperar contraseña"
                    role = Role.Button
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                disabledContainerColor = Grey40.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
            enabled = !cargando && correo.isNotBlank()
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
                        "Procesando...",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color.White
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Icono de envío",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Enviar Correo",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color.White
                    )
                }
            }
        }

        if (mensajeRecuperarPassword.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = "Resultado de la solicitud: $mensajeRecuperarPassword"
                    },
                colors = CardDefaults.cardColors(
                    containerColor = if (mensajeRecuperarPassword.contains(":"))
                        Success.copy(alpha = 0.1f)
                    else
                        Error.copy(alpha = 0.1f)
                ),
                border = BorderStroke(
                    1.dp,
                    if (mensajeRecuperarPassword.contains(":")) Success else Error
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = if (mensajeRecuperarPassword.contains(":"))
                            Icons.Default.CheckCircle
                        else
                            Icons.Default.Warning,
                        contentDescription = if (mensajeRecuperarPassword.contains(":"))
                            "Icono de éxito"
                        else
                            "Icono de error",
                        tint = if (mensajeRecuperarPassword.contains(":")) Success else Error,
                        modifier = Modifier
                            .padding(end = 12.dp, top = 2.dp)
                            .size(24.dp)
                    )
                    Text(
                        text = mensajeRecuperarPassword,
                        color = if (mensajeRecuperarPassword.contains(":")) Success else Error,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        lineHeight = 24.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        //Boton de regresar al login
        TextButton(
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .semantics {
                    contentDescription = "Regresar a la pagina de inicio de sesion"
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
                    text = "Volver al Login",
                    color = PrimaryBlue,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    textDecoration = TextDecoration.Underline
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Mensaje de ayuda adicional
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Información de ayuda adicional"
                },
            colors = CardDefaults.cardColors(
                containerColor = WhiteSurface
            ),
            border = BorderStroke(1.dp, Grey40.copy(alpha = 0.2f)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "¿Tienes problemas? Contacta nuestro soporte técnico para obtener ayuda adicional.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Grey40,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(12.dp)
                    .semantics {
                        contentDescription = "Información de contacto para soporte técnico"
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecuperarPassPreview() {
    Exp1_Sem2Theme {
        RecuperarPasswordView(usuarioViewModel = UsuarioViewModel())
    }
}