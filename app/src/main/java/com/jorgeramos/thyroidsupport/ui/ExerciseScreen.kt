package com.jorgeramos.thyroidsupport.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.domain.ExerciseEntry
import com.jorgeramos.thyroidsupport.domain.ExerciseType
import com.jorgeramos.thyroidsupport.presentation.ExerciseViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreen(
    patientId: Long = 1,
    onNavigateBack: () -> Unit,
    viewModel: ExerciseViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val entries by viewModel.entries.observeAsState(emptyList())
    val loading by viewModel.loading.observeAsState(false)
    val error by viewModel.error.observeAsState()

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val entriesByDate = entries.groupBy { it.date }

    Scaffold(
        topBar = {
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Registra tu actividad física. En hipotiroidismo se recomienda ejercicio aeróbico moderado; en hipertiroidismo no tratado, evitar intensidad excesiva.",
                style = MaterialTheme.typography.bodyMedium
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Nuevo registro de ejercicio", style = MaterialTheme.typography.titleMedium)

                    var exerciseType by remember { mutableStateOf(ExerciseType.CAMINATA) }
                    var expandedDropdown by remember { mutableStateOf(false) }
                    var duration by remember { mutableStateOf("") }
                    var intensity by remember { mutableIntStateOf(5) }
                    var notes by remember { mutableStateOf("") }

                    ExposedDropdownMenuBox(
                        expanded = expandedDropdown,
                        onExpandedChange = { expandedDropdown = !expandedDropdown }
                    ) {
                        OutlinedTextField(
                            value = exerciseType.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo de ejercicio") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedDropdown,
                            onDismissRequest = { expandedDropdown = false }
                        ) {
                            ExerciseType.entries.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.name) },
                                    onClick = {
                                        exerciseType = type
                                        expandedDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it },
                        label = { Text("Duración (minutos)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Slider(
                        value = intensity.toFloat(),
                        onValueChange = { intensity = it.toInt() },
                        valueRange = 1f..10f,
                        steps = 8,
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
                                        notes = notes.ifEmpty { null }
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
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = dateFormat.format(Date(date)),
                                style = MaterialTheme.typography.titleMedium
                            )
                            dayEntries.forEach { entry ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
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
}
