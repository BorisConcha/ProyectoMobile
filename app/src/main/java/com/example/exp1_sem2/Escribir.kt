package com.example.exp1_sem2

import android.os.Handler
import android.os.Looper
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exp1_sem2.model.Nota
import com.example.exp1_sem2.ui.theme.Exp1_Sem2Theme
import com.example.exp1_sem2.viewmodel.AccesibilidadViewModel
import com.example.exp1_sem2.viewmodel.EscribirViewModel
import com.example.exp1_sem2.viewmodel.UsuarioViewModel
import androidx.compose.runtime.DisposableEffect

class Escribir : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val userName = intent.getStringExtra("nombreUsuario") ?: ""

        setContent {
            val usuarioViewModel = UsuarioViewModel()
            val accesibilidadVM: AccesibilidadViewModel by viewModels()
            val escribirViewModel = remember { EscribirViewModel(userName) }
            var mostrarMenu by remember { mutableStateOf(false) }

            Exp1_Sem2Theme(
                darkTheme = accesibilidadVM.modoOscuro,
                tamanoFuente = accesibilidadVM.tamanoFuente
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        EscribirView(
                            usuarioViewModel = usuarioViewModel,
                            escribirViewModel = escribirViewModel,
                            nombreUsuario = userName,
                            onBackPressed = { finish() }
                        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EscribirView(
    usuarioViewModel: UsuarioViewModel,
    escribirViewModel: EscribirViewModel,
    nombreUsuario: String,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    var valorTexto by remember { mutableStateOf("") }
    var estaEditando by remember { mutableStateOf(false) }
    var idNotaEditando by remember { mutableStateOf<String?>(null) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    var notaAEliminar by remember { mutableStateOf<Nota?>(null) }

    val vozHelper = remember { VozHelper(context) }

    DisposableEffect(Unit) {
        onDispose {
            vozHelper.liberar()
        }
    }

    if (mostrarDialogoEliminar && notaAEliminar != null) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoEliminar = false
                notaAEliminar = null
            },
            title = { Text("Eliminar nota") },
            text = { Text("¿Estás seguro de querer eliminar esta nota?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoEliminar = false
                        notaAEliminar?.let { nota ->
                            escribirViewModel.eliminarNotaAsync(
                                idNota = nota.id,
                                onSuccess = {
                                    Toast.makeText(context, "Nota eliminada", Toast.LENGTH_SHORT).show()
                                    notaAEliminar = null
                                },
                                onError = { error ->
                                    Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarDialogoEliminar = false
                    notaAEliminar = null
                }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (estaEditando) "Editar Nota" else "Escribir",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        vozHelper.detenerHablar()
                        if (estaEditando) {
                            estaEditando = false
                            idNotaEditando = null
                            valorTexto = ""
                        } else {
                            onBackPressed()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                OutlinedTextField(
                    value = valorTexto,
                    onValueChange = { valorTexto = it },
                    placeholder = {
                        Text(
                            text = "Escribe lo que quieras aquí",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                        focusedBorderColor = Color(0xFFF97316),
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        cursorColor = Color(0xFFF97316)
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val contadorPalabras = if (valorTexto.isBlank()) 0
                    else valorTexto.trim().split("\\s+".toRegex()).size

                    Text(
                        text = "$contadorPalabras palabras",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (valorTexto.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    vozHelper.hablar(valorTexto)
                                },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Escuchar texto",
                                    tint = Color(0xFFF97316),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Button(
                            onClick = {
                                if (valorTexto.isNotBlank()) {
                                    vozHelper.hablar(valorTexto)

                                    Handler(Looper.getMainLooper()).postDelayed({
                                        if (estaEditando && idNotaEditando != null) {
                                            escribirViewModel.actualizarNotaAsync(
                                                idNota = idNotaEditando!!,
                                                contenido = valorTexto,
                                                onSuccess = {
                                                    Toast.makeText(context, "Nota actualizada", Toast.LENGTH_SHORT).show()
                                                    valorTexto = ""
                                                    estaEditando = false
                                                    idNotaEditando = null
                                                },
                                                onError = { error ->
                                                    Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
                                                }
                                            )
                                        } else {
                                            escribirViewModel.guardarNota(
                                                contenido = valorTexto,
                                                onSuccess = {
                                                    Toast.makeText(context, "Nota guardada", Toast.LENGTH_SHORT).show()
                                                    valorTexto = ""
                                                },
                                                onError = { error ->
                                                    Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
                                                }
                                            )
                                        }
                                    }, 1000)
                                }
                            },
                            enabled = valorTexto.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF97316),
                                disabledContainerColor = Color(0xFFF97316).copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = if (estaEditando) "Actualizar" else "Guardar",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = if (estaEditando) "Actualizar" else "Guardar")
                        }
                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(24.dp)
            ) {
                Text(
                    text = "TUS NOTAS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (escribirViewModel.notas.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "📝",
                            fontSize = 64.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No existen notas en el sistema",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "Escribe tu primera nota arriba",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(escribirViewModel.notas) { nota ->
                            TarjetaNota(
                                nota = nota,
                                onEditar = {
                                    vozHelper.detenerHablar()
                                    valorTexto = nota.contenido
                                    estaEditando = true
                                    idNotaEditando = nota.id
                                },
                                onEliminar = {
                                    vozHelper.detenerHablar()
                                    notaAEliminar = nota
                                    mostrarDialogoEliminar = true
                                },
                                onLeer = {
                                    vozHelper.hablar(nota.contenido)
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
fun TarjetaNota(
    nota: Nota,
    onEditar: () -> Unit,
    onEliminar: () -> Unit,
    onLeer: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${nota.fecha} • ${nota.hora}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onLeer,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Leer nota",
                            tint = Color(0xFFF97316),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onEditar,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = onEliminar,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = nota.vista,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFF97316).copy(alpha = 0.1f),
                modifier = Modifier.wrapContentSize()
            ) {
                Text(
                    text = "${nota.palabras} palabras",
                    fontSize = 12.sp,
                    color = Color(0xFFF97316),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}