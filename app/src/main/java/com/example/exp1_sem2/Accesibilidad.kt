package com.example.exp1_sem2

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.exp1_sem2.viewmodel.AccesibilidadViewModel

@Composable
fun Accesibilidad(viewModel: AccesibilidadViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { viewModel.disminuirFuente() }) {
            Icon(Icons.Default.Remove, "Disminuir texto")
        }

        Text("A", style = MaterialTheme.typography.titleMedium)

        IconButton(onClick = { viewModel.aumentarFuente() }) {
            Icon(Icons.Default.Add, "Aumentar texto")
        }

        Spacer(Modifier.width(16.dp))

        IconButton(onClick = { viewModel.cambiarModo() }) {
            Icon(
                if (viewModel.modoOscuro) Icons.Default.LightMode
                else Icons.Default.DarkMode,
                "Cambiar modo"
            )
        }
    }
}