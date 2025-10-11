package com.example.exp1_sem2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exp1_sem2.ui.theme.Exp1_Sem2Theme
import com.example.exp1_sem2.ui.theme.Greener
import com.example.exp1_sem2.viewmodel.AccesibilidadViewModel
import com.example.exp1_sem2.viewmodel.UsuarioViewModel
import kotlin.getValue

class Hablar : ComponentActivity() {
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
                        HablarView(usuarioViewModel = usuarioViewModel, onBackPressed = { finish() })
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

data class Recording(
    val id: Int,
    val duration: String,
    val date: String,
    val time: String,
    val preview: String,
    val isPlaying: Boolean = false
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HablarView(usuarioViewModel: UsuarioViewModel, onBackPressed: () -> Unit){
    val context = LocalContext.current

    var isRecording by remember { mutableStateOf(false) }
    var recordingDuration by remember { mutableStateOf("00:00") }

    val sampleRecordings = remember {
        mutableStateListOf(
            Recording(
                id = 1,
                duration = "2:34",
                date = "10 Oct",
                time = "14:30",
                preview = "Nota de voz sobre la reunión de equipo..."
            ),
            Recording(
                id = 2,
                duration = "1:15",
                date = "10 Oct",
                time = "09:15",
                preview = "Recordatorio para comprar leche y pan..."
            ),
            Recording(
                id = 3,
                duration = "0:45",
                date = "9 Oct",
                time = "16:45",
                preview = "Idea para el nuevo proyecto de app..."
            ),
            Recording(
                id = 4,
                duration = "3:12",
                date = "8 Oct",
                time = "11:20",
                preview = "Notas de la reunión con el cliente..."
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Hablar",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
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
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Greener.copy(alpha = 0.05f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.45f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    RecordButton(
                        isRecording = isRecording,
                        onClick = {
                            isRecording = !isRecording
                            // TODO: Implementar lógica de grabación
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = if (isRecording) "Grabando..." else "Mantén presionado para grabar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isRecording) Greener else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        fontWeight = if (isRecording) FontWeight.SemiBold else FontWeight.Normal
                    )

                    if (isRecording) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = recordingDuration,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Greener
                            )
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.55f)
                    .padding(horizontal = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Historial",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )

                    if (sampleRecordings.isNotEmpty()) {
                        TextButton(onClick = { /* TODO: Ver todas */ }) {
                            Text(
                                text = "Ver todas",
                                fontSize = 12.sp,
                                color = Greener
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (sampleRecordings.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No hay grabaciones",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "Graba tu primera nota de voz",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(sampleRecordings.take(3)) { recording ->
                            RecordingCard(
                                recording = recording,
                                onPlay = {
                                    // TODO: Implementar reproducción
                                },
                                onDelete = {
                                    // TODO: Implementar eliminación
                                    sampleRecordings.remove(recording)
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
fun RecordButton(
    isRecording: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        contentAlignment = Alignment.Center
    ) {
        if (isRecording) {
            Surface(
                modifier = Modifier
                    .size(160.dp)
                    .scale(scale),
                shape = CircleShape,
                color = Greener.copy(alpha = 0.2f)
            ) {}
        }

        Surface(
            onClick = onClick,
            modifier = Modifier.size(128.dp),
            shape = CircleShape,
            color = if (isRecording) Color.Red else Greener,
            shadowElevation = 12.dp,
            tonalElevation = 4.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = if (isRecording) "Detener grabación" else "Iniciar grabación",
                    modifier = Modifier.size(56.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun RecordingCard(
    recording: Recording,
    onPlay: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "${recording.date} • ${recording.time}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Greener.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = recording.duration,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Greener,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = recording.preview,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onPlay,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Greener.copy(alpha = 0.1f),
                        contentColor = Greener
                    ),
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Reproducir",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Reproducir",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Más opciones",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Compartir") },
                            onClick = {
                                showMenu = false
                                // TODO: Implementar compartir
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Share, contentDescription = null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar") },
                            onClick = {
                                showMenu = false
                                onDelete()
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = Color.Red
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}