package com.example.exp1_sem2

import android.content.Intent
import android.Manifest
import android.net.Uri
import android.provider.Settings
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.exp1_sem2.model.Grabacion
import com.example.exp1_sem2.ui.theme.Exp1_Sem2Theme
import com.example.exp1_sem2.ui.theme.Greener
import com.example.exp1_sem2.viewmodel.AccesibilidadViewModel
import com.example.exp1_sem2.viewmodel.HablarViewModel
import com.example.exp1_sem2.viewmodel.UsuarioViewModel
import kotlinx.coroutines.delay
import kotlin.getValue

class Hablar : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userName = intent.getStringExtra("nombreUsuario") ?: "Usuario"
        setContent {
            val usuarioViewModel = UsuarioViewModel()
            val hablarViewModel = remember { HablarViewModel(userName) }
            val accesibilidadVM: AccesibilidadViewModel by viewModels()
            var mostrarMenu by remember { mutableStateOf(false) }
            Exp1_Sem2Theme(darkTheme = accesibilidadVM.modoOscuro,
                tamanoFuente = accesibilidadVM.tamanoFuente) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        HablarView(hablarViewModel = hablarViewModel,
                            onBackPressed = { finish() })
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
fun HablarView(hablarViewModel: HablarViewModel, onBackPressed: () -> Unit){
    val context = LocalContext.current

    var isRecording by remember { mutableStateOf(false) }
    var recordingDuration by remember { mutableStateOf("00:00") }
    var currentText by remember { mutableStateOf("") }
    var showSaveDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showPermissionDialog by remember { mutableStateOf(false) }

    var startTime by remember { mutableLongStateOf(0L) }

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (!isGranted) {
            showPermissionDialog = true
        }
    }

    val vozHelper = remember {
        VozHelper(context)
    }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            startTime = System.currentTimeMillis()
            while (isRecording) {
                delay(1000)
                val elapsed = (System.currentTimeMillis() - startTime) / 1000
                val minutes = elapsed / 60
                val seconds = elapsed % 60
                recordingDuration = "%02d:%02d".format(minutes, seconds)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            vozHelper.liberar()
        }
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    tint = Greener,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    "Permiso necesario",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Para grabar notas de voz necesitamos acceso al micrófono. " +
                            "Por favor, activa el permiso en la configuración de la aplicación.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPermissionDialog = false
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    }
                ) {
                    Text("Ir a configuración", color = Greener)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo para guardar
    if (showSaveDialog && currentText.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Guardar mensaje de voz") },
            text = {
                Column {
                    Text("Texto reconocido:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                            .heightIn(max = 200.dp)
                            .verticalScroll(rememberScrollState())
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        hablarViewModel.guardarGrabacion(
                            contenido = currentText,
                            onSuccess = {
                                Toast.makeText(context, "Mensaje guardado correctamente", Toast.LENGTH_SHORT).show()
                                currentText = ""
                                recordingDuration = "00:00"
                                showSaveDialog = false
                            },
                            onError = { error ->
                                errorMessage = error
                                showSaveDialog = false
                            }
                        )
                    }
                ) {
                    Text("Guardar", color = Greener)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showSaveDialog = false
                    currentText = ""
                }) {
                    Text("Descartar")
                }
            }
        )
    }

    // Error message
    errorMessage?.let { error ->
        LaunchedEffect(error) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            delay(3000)
            errorMessage = null
        }
    }

    val grabaciones = hablarViewModel.grabaciones

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
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    RecordButton(
                        isRecording = isRecording,
                        onStartRecording = {
                            if (hasPermission) {
                                currentText = ""
                                vozHelper.iniciarEscucha(
                                    onResult = { texto ->
                                        currentText = texto
                                        isRecording = false
                                        showSaveDialog = true
                                    },
                                    onError = { error ->
                                        errorMessage = error
                                        isRecording = false
                                    },
                                    onPartialResult = { parcial ->
                                        currentText = parcial
                                    }
                                )
                                isRecording = true
                            } else {
                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        },
                        onStopRecording = {
                            vozHelper.detenerEscucha()
                            isRecording = false
                            if (currentText.isNotEmpty()) {
                                showSaveDialog = true
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = if (isRecording) "Escuchando... suelta para detener" else "Mantén presionado para grabar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isRecording) Greener else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        fontWeight = if (isRecording) FontWeight.SemiBold else FontWeight.Normal,
                        textAlign = TextAlign.Center
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

                        if (currentText.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ) {
                                Text(
                                    text = currentText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
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

                }

                Spacer(modifier = Modifier.height(8.dp))

                if (grabaciones.isEmpty()) {
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
                            text = "Graba tu primer mensaje de voz",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(grabaciones.take(3)) { grabacion ->
                            RecordingCard(
                                recording = Grabacion(
                                    id = grabacion.id,
                                    duracion = grabacion.duracion,
                                    fecha = grabacion.fecha,
                                    hora = grabacion.hora,
                                    contenido = grabacion.contenido.take(50) + if (grabacion.contenido.length > 50) "..." else ""
                                ),
                                contenidoCompleto = grabacion.contenido,
                                onPlay = {
                                    vozHelper.hablar(grabacion.contenido)
                                },
                                onDelete = {
                                    hablarViewModel.eliminarGrabacionAsync(
                                        idGrabacion = grabacion.id,
                                        onSuccess = {
                                            Toast.makeText(context, "Grabación eliminada", Toast.LENGTH_SHORT).show()
                                        },
                                        onError = { error ->
                                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                        }
                                    )
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
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit
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

    var isPressing by remember { mutableStateOf(false) }

    val buttonScale by animateFloatAsState(
        targetValue = if (isPressing) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "buttonScale"
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

            Surface(
                modifier = Modifier
                    .size(180.dp)
                    .scale(scale * 0.9f),
                shape = CircleShape,
                color = Greener.copy(alpha = 0.1f)
            ) {}
        }

        Surface(
            modifier = Modifier
                .size(128.dp)
                .scale(buttonScale)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isPressing = true
                            onStartRecording()
                            tryAwaitRelease()
                            isPressing = false
                            onStopRecording()
                        }
                    )
                },
            shape = CircleShape,
            color = if (isRecording) Color.Red else Greener,
            shadowElevation = if (isRecording) 16.dp else 12.dp,
            tonalElevation = 4.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = if (isRecording) "Suelta para detener" else "Mantén presionado para grabar",
                    modifier = Modifier
                        .size(56.dp)
                        .graphicsLayer {
                            scaleX = if (isRecording) scale * 0.95f else 1f
                            scaleY = if (isRecording) scale * 0.95f else 1f
                        },
                    tint = Color.White
                )
            }
        }

        if (isPressing && !isRecording) {
            Surface(
                modifier = Modifier.size(140.dp),
                shape = CircleShape,
                color = Greener.copy(alpha = 0.3f)
            ) {}
        }
    }
}

@Composable
fun RecordingCard(
    recording: Grabacion,
    contenidoCompleto: String,
    onPlay: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showFullContent by remember { mutableStateOf(false) }

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
                        text = "${recording.fecha} • ${recording.hora}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                if (recording.duracion.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Greener.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = recording.duracion,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Greener,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column {
                Text(
                    text = if (showFullContent) contenidoCompleto else recording.contenido,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp,
                    maxLines = if (showFullContent) Int.MAX_VALUE else 2
                )

                if (contenidoCompleto.length > 50) {
                    TextButton(
                        onClick = { showFullContent = !showFullContent },
                        modifier = Modifier.padding(0.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = if (showFullContent) "Ver menos" else "Ver más",
                            fontSize = 12.sp,
                            color = Greener
                        )
                    }
                }
            }

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