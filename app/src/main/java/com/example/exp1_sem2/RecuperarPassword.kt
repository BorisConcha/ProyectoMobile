package com.example.exp1_sem2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
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
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(
            text = "Recuperar Contraseña",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp),
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(
                text = "Te ayudaremos a recuperar el acceso a tu cuenta. Ingresa tu correo electrónico para comenzar el proceso de recuperación.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .semantics {
                        contentDescription = "Información: Te ayudaremos a recuperar el acceso a tu cuenta"
                    }
            )
        }

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = {Text("Correo Electronico")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

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
            modifier = Modifier.fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCC10C)),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("Enviar Correo", fontSize = 16.sp, color = Color.White)
        }

        if (mensajeRecuperarPassword.isNotEmpty()) {
            Text(
                text = mensajeRecuperarPassword,
                color = if (mensajeRecuperarPassword.contains(":"))
                    MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(8.dp)
                    .semantics { contentDescription = "$mensajeRecuperarPassword" }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Volver al login",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable{

                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)

            }
        )
    }
}