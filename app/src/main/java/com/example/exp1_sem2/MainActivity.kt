package com.example.exp1_sem2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exp1_sem2.ui.theme.Exp1_Sem2Theme

import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.animateLottieCompositionAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Exp1_Sem2Theme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFE8E5DA)
                ) {
                    Login()
                }
            }
        }
    }
}


@Preview
@Composable
fun Login(){

    val context = LocalContext.current
    var username by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}


    Column(
        modifier = Modifier.fillMaxSize().padding( horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val image by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.readingboy))
        val animacion by animateLottieCompositionAsState(image,
            iterations = LottieConstants.IterateForever)

        LottieAnimation(
            composition = image,
            progress = {animacion},
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "DUOC UC",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Inicia sesion para continuar",
            fontSize = 14.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(24.dp))


        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = {
                Text("Nombre de Usuario",
                    color = Color.Gray,
                    style = TextStyle(textAlign = TextAlign.Center),
                    modifier = Modifier.fillMaxWidth())
            },
            modifier = Modifier.fillMaxWidth()
                .height(54.dp)
                .background(Color(0xFFE6E3D8), shape = RoundedCornerShape(4.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFCC10C),
                unfocusedTextColor = Color(0xFFFCC10C),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            shape = RoundedCornerShape(4.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = {
                Text("Contraseña",
                    color = Color.Gray,
                    style = TextStyle(textAlign = TextAlign.Center),
                    modifier = Modifier.fillMaxWidth())
            },
            modifier = Modifier.fillMaxWidth()
                .height(54.dp)
                .background(Color(0xFFE6E3D8), shape = RoundedCornerShape(4.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFCC10C),
                unfocusedTextColor = Color(0xFFFCC10C),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            shape = RoundedCornerShape(4.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {/*TODO*/},
            modifier = Modifier.fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCC10C)),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("Iniciar Sesion", fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recuperar Contraseña",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable{

            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Registrarse",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable{

                val intent = Intent(context, Registro::class.java)
                context.startActivity(intent)
            }
        )


    }
}