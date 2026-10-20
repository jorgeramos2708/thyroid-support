package com.jorgeramos.thyroidsupport.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.domain.ExerciseEntry
import com.jorgeramos.thyroidsupport.domain.ExerciseType
import com.jorgeramos.thyroidsupport.presentation.ExerciseViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExerciseScreen(
    patientId: Long = 1,
    onNavigateBack: () -> Unit,
    viewModel: ExerciseViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val entries by viewModel.entries.observeAsState()
    val loading by viewModel.loading.observeAsState(false)
    val error by viewModel.error.observeAsState()

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val entriesByDate = entries?.groupBy { it.date } ?: emptyMap()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopAppBar(
            title = { Text("Ejercicio") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )

        Text(
            text = "Registra tu actividad física. En hipotiroidismo se recomienda ejercicio aeróbico moderado; en hipertiroidismo no tratado, evitar intensidad excesiva.",
            style = MaterialTheme.typography.bodyMedium
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Nuevo registro de ejercicio", style = MaterialTheme.typography.titleMedium)

                var exerciseType by remember { mutableStateOf(ExerciseType.CAMINATA) }
                var duration by remember { mutableStateOf("") }
                var intensity by remember { mutableStateOf(5) }
                var notes by remember { mutableStateOf("") }

                DropdownMenu(
                    expanded = true,
                    onDismissRequest = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ExerciseType.values().forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = { exerciseType = type }
                        )
                    }
                }
                Text(text = "Tipo: ${exerciseType.name}", style = MaterialTheme.typography.bodyMedium)

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duración (minutos)") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.foundation.text.KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Slider(
                    value = intensity.toFloat(),
                    onValueChange = { intensity = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 9,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(text = "Intensidad: $intensity / 10", style = MaterialTheme.typography.bodyMedium)

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notas") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        scope.launch {
                            viewModel.addEntry(
                                ExerciseEntry(
                                    date = System.currentTimeMillis(),
                                    type = exerciseType,
                                    durationMinutes = duration.toIntOrNull() ?: 0,
                                    intensity = intensity,
                                    notes = notes.takeIf { it.isNotEmpty() }.orNull()
                                )
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar ejercicio")
                }
            }
        }

        if (entriesByDate.isNotEmpty()) {
            Text(text = "Historial", style = MaterialTheme.typography.titleMedium)
            entriesByDate.entries.sortedByDescending { it.key }.forEach { (date, dayEntries) ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = dateFormat.format(Date(date)),
                            style = MaterialTheme.typography.titleMedium
                        )
                        dayEntries.forEach { entry ->
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "${entry.type.name}: ${entry.durationMinutes} min",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Intensidad: ${entry.intensity}/10",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }

        error?.let { err ->
            Text(text = "Error: $err", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error))
        }

        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}
