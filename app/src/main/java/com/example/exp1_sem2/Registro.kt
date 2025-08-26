package com.example.exp1_sem2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
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

    val context = LocalContext.current
    var nombreUsuario by remember { mutableStateOf("")}
    var nombre by remember { mutableStateOf("")}
    var apellidoP by remember { mutableStateOf("")}
    var apellidoM by remember { mutableStateOf("")}
    var correo by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}

    Spacer(modifier = Modifier.height(16.dp))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = "Registro de Usuario",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp),
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = nombreUsuario,
            onValueChange = { nombreUsuario = it },
            label = {Text("Nombre de Usuario")},
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = {Text("Nombre")},
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = apellidoP,
            onValueChange = { apellidoP = it },
            label = {Text("Apellido Paterno")},
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = apellidoM,
            onValueChange = { apellidoM = it },
            label = {Text("Apellido Materno")},
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = {Text("Correo Electronico")},
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {Text("Contraseña")},
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (nombreUsuario.isNotBlank() && nombre.isNotBlank() &&
                    apellidoP.isNotBlank() && apellidoM.isNotBlank() &&
                    correo.isNotBlank() && password.isNotBlank()) {

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
                } else {
                    Toast.makeText(context, "Por favor completa todos los campos requeridos", Toast.LENGTH_SHORT).show()
                }

            },
            modifier = Modifier.fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCC10C)),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("Registrarse", fontSize = 16.sp, color = Color.White)
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