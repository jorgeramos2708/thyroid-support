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
import com.jorgeramos.thyroidsupport.domain.DiaryEntry
import com.jorgeramos.thyroidsupport.presentation.DiaryViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DiaryScreen(
    patientId: Long = 1,
    onNavigateBack: () -> Unit,
    viewModel: DiaryViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val entries by viewModel.entries.observeAsState()
    val loading by viewModel.loading.observeAsState(false)
    val error by viewModel.error.observeAsState()

    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )

        Text(
            text = "Registra cómo te sientes cada día. Esto ayuda a identificar patrones con tus resultados de laboratorio.",
            style = MaterialTheme.typography.bodyMedium
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Nueva entrada", style = MaterialTheme.typography.titleMedium)

                var mood by remember { mutableStateOf(5) }
                var energy by remember { mutableStateOf(5) }
                var symptoms by remember { mutableStateOf("") }
                var notes by remember { mutableStateOf("") }
                var medicationTaken by remember { mutableStateOf(true) }
                var labsDone by remember { mutableStateOf(false) }

                Slider(
                    value = mood.toFloat(),
                    onValueChange = { mood = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 9,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(text = "Estado de ánimo: $mood / 10", style = MaterialTheme.typography.bodyMedium)

                Slider(
                    value = energy.toFloat(),
                    onValueChange = { energy = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 9,
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

                Checkbox(
                    checked = medicationTaken,
                    onCheckedChange = { medicationTaken = it }
                )
                Text(text = "Tomé mi medicación hoy")

                Checkbox(
                    checked = labsDone,
                    onCheckedChange = { labsDone = it }
                )
                Text(text = "Me hice análisis de laboratorio hoy")

                Button(
                    onClick = {
                        scope.launch {
                            viewModel.addEntry(
                                DiaryEntry(
                                    date = System.currentTimeMillis(),
                                    mood = mood,
                                    energyLevel = energy,
                                    symptoms = symptoms.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                                    notes = notes.takeIf { it.isNotEmpty() }.orNull(),
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
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = dateFormat.format(java.util.Date(entry.date)),
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
                        entry.symptoms.forEach { symptom ->
                            Chip(
                                onClick = {},
                                label = { Text(symptom) }
                            )
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
