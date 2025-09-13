package com.example.exp1_sem2

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exp1_sem2.ui.theme.Exp1_Sem2Theme



class RecuperarPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Exp1_Sem2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFE8E5DA)
                ) {
                    RecuperarPasswordView()
                }
            }
        }
    }
}
@Preview
@Composable
fun RecuperarPasswordView(){

    val context = LocalContext.current
    var correo by remember { mutableStateOf("")}
    var mensajeRecuperarPassword by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Icono de seguridad para recuperación de contraseña",
            tint = Color(0xFF1565C0),
            modifier = Modifier
                .size(64.dp)
                .padding(bottom = 16.dp)
                .semantics {
                    contentDescription = "Icono de candado que representa seguridad"
                }
        )

        Text(
            text = "Recuperar Contraseña",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color(0xFF212121),
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .semantics {
                    contentDescription = "Título de la pantalla: Recuperar Contraseña"
                },
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .semantics {
                    contentDescription = "Información importante sobre el proceso de recuperación"
                },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            ),
            border = BorderStroke(1.dp, Color(0xFF1565C0).copy(alpha = 0.3f)),
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
                    tint = Color(0xFF1565C0),
                    modifier = Modifier
                        .padding(end = 12.dp, top = 2.dp)
                        .size(24.dp)
                )
                Text(
                    text = "Te ayudaremos a recuperar el acceso a tu cuenta. Ingresa tu correo electrónico registrado para comenzar el proceso de recuperación de contraseña.",
                    fontSize = 16.sp,
                    color = Color(0xFF212121),
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
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Icono de correo electrónico",
                    tint = if (correo.isEmpty()) Color(0xFF616161) else Color(0xFF1565C0)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .semantics {
                    contentDescription = "Campo para ingresar tu correo electrónico registrado"
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
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        //Boton de enviar correo
        Button(
            onClick = {
                cargando = true
                val password = UsuarioRepositorio.buscarUsuarioPorCorreo(correo)
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
                        "Botón para enviar solicitud de recuperación de contraseña al correo ingresado"
                    role = Role.Button
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1565C0),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFF616161).copy(alpha = 0.3f)
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
                        imageVector = Icons.Default.Send,
                        contentDescription = "Icono de envío",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Enviar Correo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
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
                        Color(0xFF388E3C).copy(alpha = 0.1f)
                    else
                        Color(0xFFD32F2F).copy(alpha = 0.1f)
                ),
                border = BorderStroke(
                    1.dp,
                    if (mensajeRecuperarPassword.contains(":")) Color(0xFF388E3C) else Color(0xFFD32F2F)
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
                        tint = if (mensajeRecuperarPassword.contains(":")) Color(0xFF388E3C) else Color(0xFFD32F2F),
                        modifier = Modifier
                            .padding(end = 12.dp, top = 2.dp)
                            .size(24.dp)
                    )
                    Text(
                        text = mensajeRecuperarPassword,
                        color = if (mensajeRecuperarPassword.contains(":")) Color(0xFF388E3C) else Color(0xFFD32F2F),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
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
                    text = "Volver al Login",
                    color = Color(0xFF1565C0),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
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
                containerColor = Color(0xFFFFFFFF)
            ),
            border = BorderStroke(1.dp, Color(0xFF616161).copy(alpha = 0.2f)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "¿Tienes problemas? Contacta nuestro soporte técnico para obtener ayuda adicional.",
                fontSize = 14.sp,
                color = Color(0xFF616161),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(12.dp)
                    .semantics {
                        contentDescription = "Información de contacto para soporte técnico en caso de problemas"
                    }
            )
        }
    }
}