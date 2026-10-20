package com.jorgeramos.thyroidsupport.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.domain.DiaryEntry
import com.jorgeramos.thyroidsupport.presentation.DiaryViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryScreen(
    patientId: Long = 1,
    onNavigateBack: () -> Unit,
    viewModel: DiaryViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val entries by viewModel.entries.observeAsState(emptyList())
    val loading by viewModel.loading.observeAsState(false)
    val error by viewModel.error.observeAsState()

    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diario de Síntomas") },
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
                text = "Registra cómo te sientes cada día. Esto ayuda a identificar patrones con tus resultados de laboratorio.",
                style = MaterialTheme.typography.bodyMedium
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Nueva entrada", style = MaterialTheme.typography.titleMedium)

                    var mood by remember { mutableIntStateOf(5) }
                    var energy by remember { mutableIntStateOf(5) }
                    var symptoms by remember { mutableStateOf("") }
                    var notes by remember { mutableStateOf("") }
                    var medicationTaken by remember { mutableStateOf(true) }
                    var labsDone by remember { mutableStateOf(false) }

                    Slider(
                        value = mood.toFloat(),
                        onValueChange = { mood = it.toInt() },
                        valueRange = 1f..10f,
                        steps = 8,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text = "Estado de ánimo: $mood / 10", style = MaterialTheme.typography.bodyMedium)

                    Slider(
                        value = energy.toFloat(),
                        onValueChange = { energy = it.toInt() },
                        valueRange = 1f..10f,
                        steps = 8,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text = "Nivel de energía: $energy / 10", style = MaterialTheme.typography.bodyMedium)

                    OutlinedTextField(
                        value = symptoms,
                        onValueChange = { symptoms = it },
                        label = { Text("Síntomas (separados por coma)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notas adicionales") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = medicationTaken,
                            onCheckedChange = { medicationTaken = it }
                        )
                        Text(text = "Tomé mi medicación hoy")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = labsDone,
                            onCheckedChange = { labsDone = it }
                        )
                        Text(text = "Me hice análisis de laboratorio hoy")
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.addEntry(
                                    DiaryEntry(
                                        date = System.currentTimeMillis(),
                                        mood = mood,
                                        energyLevel = energy,
                                        symptoms = symptoms.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                                        notes = notes.ifEmpty { null },
                                        medicationTaken = medicationTaken,
                                        labsDone = labsDone
                                    )
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar entrada")
                    }
                }
            }

            if (entries.isNotEmpty()) {
                Text(text = "Historial", style = MaterialTheme.typography.titleMedium)
                entries.forEach { entry ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = dateFormat.format(Date(entry.date)),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "Ánimo: ${entry.mood}/10",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Energía: ${entry.energyLevel}/10",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                entry.symptoms.forEach { symptom ->
                                    SuggestionChip(
                                        onClick = {},
                                        label = { Text(symptom) }
                                    )
                                }
                            }
                            entry.notes?.let { note ->
                                Text(text = note, style = MaterialTheme.typography.bodySmall)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = if (entry.medicationTaken) "✓ Medicación" else "✗ Medicación",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (entry.medicationTaken) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                Text(
                                    text = if (entry.labsDone) "✓ Labs" else "✗ Labs",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (entry.labsDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
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
