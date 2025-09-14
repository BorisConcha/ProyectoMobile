package com.example.exp1_sem2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exp1_sem2.ui.theme.Exp1_Sem2Theme

import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.example.exp1_sem2.ui.theme.*
import com.example.exp1_sem2.viewmodel.UsuarioViewModel

class MainActivity : ComponentActivity() {
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
                    Login(usuarioViewModel = usuarioViewModel)
                }
            }
        }
    }
}

@Composable
fun Login(usuarioViewModel: UsuarioViewModel) {

    //Variables genericas de la vista
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensajeLogin1 by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val image by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.dog))
        val animacion by animateLottieCompositionAsState(
            image,
            iterations = LottieConstants.IterateForever
        )

        //animacion
        LottieAnimation(
            composition = image,
            progress = { animacion },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .semantics {
                    contentDescription = "Animación de una persona leyendo"
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Brujula",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Black40,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.semantics {
                contentDescription = "Titulo del login de la app"
            },
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        //Usuario
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = {
                Text(
                    "Nombre de Usuario",
                    color = Grey40,
                    fontSize = 16.sp
                )
            },
            placeholder = {
                Text(
                    "Ingresa tu usuario",
                    color = Grey40.copy(alpha = 0.7f)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .semantics {
                    contentDescription = "Campo del ingresar nombre de usuario"
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

        Spacer(modifier = Modifier.height(16.dp))

        //Contraseña
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
                    "Ingresa tu contraseña",
                    color = Grey40.copy(alpha = 0.7f)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .semantics {
                    contentDescription = "Campo para ingresar contraseña"
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
            visualTransformation = PasswordVisualTransformation(),
            textStyle = TextStyle(fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de Login
        Button(
            onClick = {
                cargando = true
                // Cambiar esta línea - usar el ViewModel en lugar del repositorio
                val usuario = usuarioViewModel.getUsuarioPorNombreUsuario(username)
                if (usuario != null && usuario.password == password) {
                    mensajeLogin1 = "Se inicio la sesion correctamente"
                } else {
                    mensajeLogin1 = "Credenciales incorrectas"
                }
                cargando = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .semantics {
                    contentDescription = "Boton para iniciar sesion"
                    role = Role.Button
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    "Iniciar Sesión",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        // Mensaje de resultado
        if (mensajeLogin1.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .semantics {
                        contentDescription = "Mensaje del inicio de sesión: $mensajeLogin1"
                    },
                colors = CardDefaults.cardColors(
                    containerColor = if (mensajeLogin1.contains("correctamente"))
                        Success.copy(alpha = 0.1f)
                    else
                        Error.copy(alpha = 0.1f)
                ),
                border = BorderStroke(
                    1.dp,
                    if (mensajeLogin1.contains("correctamente")) Success else Error
                )
            ) {
                Text(
                    text = mensajeLogin1,
                    color = if (mensajeLogin1.contains("correctamente")) Success else Error,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        //Boton recuperar contraseña
        TextButton(
            onClick = {
                val intent = Intent(context, RecuperarPassword::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.semantics {
                contentDescription = "Enlace para poder recuperar contraseña"
                role = Role.Button
            }
        ) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = Color(0xFF1565C0),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textDecoration = TextDecoration.Underline
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        //Boton registrarse
        TextButton(
            onClick = {
                val intent = Intent(context, Registro::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.semantics {
                contentDescription = "Enlace para crear una nueva cuenta"
                role = Role.Button
            }
        ) {
            Text(
                text = "¿No tienes cuenta? Regístrate",
                color = PrimaryBlue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textDecoration = TextDecoration.Underline
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}