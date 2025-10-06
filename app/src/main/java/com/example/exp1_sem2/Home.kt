package com.example.exp1_sem2

import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exp1_sem2.ui.theme.Exp1_Sem2Theme
import com.example.exp1_sem2.ui.theme.*
import com.example.exp1_sem2.viewmodel.AccesibilidadViewModel
import com.example.exp1_sem2.viewmodel.UsuarioViewModel
import kotlin.getValue
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.material.icons.filled.*
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.DisposableEffect
import androidx.core.content.ContextCompat

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userName = intent.getStringExtra("nombreUsuario") ?: "Usuario"
        setContent {
            val usuarioViewModel = UsuarioViewModel()
            val accesibilidadVM: AccesibilidadViewModel by viewModels()
            var mostrarMenu by remember { mutableStateOf(false) }
            Exp1_Sem2Theme(darkTheme = accesibilidadVM.modoOscuro,
                tamanoFuente = accesibilidadVM.tamanoFuente) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        HomeView(usuarioViewModel = usuarioViewModel,userName)
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

@Composable
fun HomeView(usuarioViewModel: UsuarioViewModel, userName: String) {
    val context = LocalContext.current

    val vozHelper = remember { VozHelper(context) }

    var textoReconocido by remember { mutableStateOf("") }
    var mostrarDialogoVoz by remember { mutableStateOf(false) }

    var ubicacionActual by remember { mutableStateOf<Location?>(null) }
    var mostrarDialogoGPS by remember { mutableStateOf(false) }
    var mensajeGPS by remember { mutableStateOf("") }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val requestAudioPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora...")
            }
            try {
                (context as? Activity)?.startActivityForResult(intent, 100)
            } catch (e: Exception) {
            }
        }
    }

    val requestLocationPermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            obtenerUbicacionReal(
                fusedLocationClient,
                context,
                onSuccess = { location ->
                    ubicacionActual = location
                    mensajeGPS = "Dispositivo encontrado"
                    vozHelper.hablar("Dispositivo encontrado en su ubicación actual")
                    mostrarDialogoGPS = true
                },
                onError = { error ->
                    mensajeGPS = error
                    vozHelper.hablar(error)
                }
            )
        }
    }

    val speechLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val matches = result.data?.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS
            )
            textoReconocido = matches?.firstOrNull() ?: ""
            if (textoReconocido.isNotEmpty()) {
                vozHelper.hablar("Dijiste: $textoReconocido")
                mostrarDialogoVoz = true
            }
        }
    }

    fun iniciarReconocimientoVoz() {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora...")
                }
                speechLauncher.launch(intent)
            }
            else -> {
                requestAudioPermission.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    fun obtenerUbicacion() {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                obtenerUbicacionReal(
                    fusedLocationClient,
                    context,
                    onSuccess = { location ->
                        ubicacionActual = location
                        mensajeGPS = "Dispositivo encontrado"
                        vozHelper.hablar("Dispositivo encontrado en su ubicación actual")
                        mostrarDialogoGPS = true
                    },
                    onError = { error ->
                        mensajeGPS = error
                        vozHelper.hablar(error)
                    }
                )
            }
            else -> {
                requestLocationPermissions.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            vozHelper.liberar()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Hola,",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Normal
                )
            )
            Text(
                text = userName,
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ),
                modifier = Modifier.semantics {
                    contentDescription = "Bienvenida a $userName"
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Bienvenido de nuevo",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCard(
            onClick = {
                vozHelper.hablar("Buscando dispositivo")
                obtenerUbicacion()
            },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.elevatedCardColors(
                containerColor = PurpleSoft,
                contentColor = White60
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .semantics {
                    contentDescription = "Card Buscar dispositivo"
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Icono de buscar dispositivo",
                        modifier = Modifier.size(40.dp),
                        tint = White60
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Buscar Dispositivo",
                        style = MaterialTheme.typography.titleMedium,
                        color = White60
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Buscar",
                    modifier = Modifier.size(32.dp),
                    tint = White60
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCard(
            onClick = {
                vozHelper.hablar("Iniciando reconocimiento de voz")
                iniciarReconocimientoVoz()
            },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.elevatedCardColors(
                containerColor = Greener,
                contentColor = White60
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .semantics {
                    contentDescription = "Card Reconocimiento de Voz"
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Icono Micrófono",
                        modifier = Modifier.size(40.dp),
                        tint = White60
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Hablar",
                        style = MaterialTheme.typography.titleMedium,
                        color = White60
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Activar micrófono",
                    modifier = Modifier.size(32.dp),
                    tint = White60
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCard(
            onClick = {
                vozHelper.hablar("Hola $userName, bienvenido a la aplicación Brujula. Esta función lee texto en voz alta.")
            },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.elevatedCardColors(
                containerColor = OrangeSoft,
                contentColor = White60
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .semantics {
                    contentDescription = "Card Texto a Voz"
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Icono Texto a Voz",
                        modifier = Modifier.size(40.dp),
                        tint = White60
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Escuchar",
                        style = MaterialTheme.typography.titleMedium,
                        color = White60
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Reproducir audio",
                    modifier = Modifier.size(32.dp),
                    tint = White60
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                vozHelper.hablar("Cerrando sesión")
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    (context as? Activity)?.finish()
                }, 1000)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .semantics {
                    contentDescription = "Boton para cerrar sesion"
                    role = Role.Button
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
        ) {
            Text(
                "Cerrar Sesión",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White
            )
        }
    }

    if (mostrarDialogoVoz) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoVoz = false },
            title = {
                Text(
                    "Texto Reconocido",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column {
                    Text(
                        text = "Dijiste:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = PrimaryBlue.copy(alpha = 0.1f)
                        )
                    ) {
                        Text(
                            text = textoReconocido,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoVoz = false
                    textoReconocido = ""
                }) {
                    Text("Cerrar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    vozHelper.hablar(textoReconocido)
                }) {
                    Text("Repetir")
                }
            }
        )
    }

    if (mostrarDialogoGPS) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoGPS = false },
            title = {
                Text(
                    "Ubicación del Dispositivo",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column {
                    if (ubicacionActual != null) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Dispositivo Encontrado",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4CAF50)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Latitud: ${String.format("%.6f", ubicacionActual?.latitude)}")
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Longitud: ${String.format("%.6f", ubicacionActual?.longitude)}")
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Precisión: ${ubicacionActual?.accuracy?.toInt()} metros")
                            }
                        }
                    } else {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Red.copy(alpha = 0.1f)
                            )
                        ) {
                            Text(
                                text = mensajeGPS,
                                color = Color.Red,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoGPS = false
                }) {
                    Text("Cerrar")
                }
            },
            dismissButton = {
                if (ubicacionActual != null) {
                    TextButton(onClick = {
                        val uri = "geo:${ubicacionActual?.latitude},${ubicacionActual?.longitude}?q=${ubicacionActual?.latitude},${ubicacionActual?.longitude}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        context.startActivity(intent)
                    }) {
                        Text("Ver en Mapa")
                    }
                }
            }
        )
    }
}

private fun obtenerUbicacionReal(
    fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient,
    context: android.content.Context,
    onSuccess: (Location) -> Unit,
    onError: (String) -> Unit
) {
    try {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                onSuccess(location)
            } else {
                onError("No se pudo obtener la ubicación")
            }
        }.addOnFailureListener {
            onError("Error al obtener ubicación")
        }
    } catch (e: SecurityException) {
        onError("Error de permisos")
    }
}

