package com.jorgeramos.thyroidsupport.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.domain.Symptom
import com.jorgeramos.thyroidsupport.presentation.SymptomViewModel
import kotlinx.coroutines.launch

@Composable
fun SymptomLogScreen(
    onNavigateBack: () -> Unit,
    viewModel: SymptomViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    var symptomType by remember { mutableStateOf("") }
    var severity by remember { mutableStateOf(0) }
    var notes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Registro de Síntoma",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = symptomType,
            onValueChange = { symptomType = it },
            label = { Text("Tipo de síntoma") },
            modifier = Modifier.fillMaxWidth()
        )

        Slider(
            value = severity.toFloat(),
            onValueChange = { severity = it.toInt() },
            valueRange = 0f..10f,
            steps = 10,
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = "Gravedad: $severity", style = MaterialTheme.typography.bodyMedium)

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notas") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    onNavigateBack()
                }
            ) {
                Text("Cancelar")
            }

            Button(
                onClick = {
                    scope.launch {
                        viewModel.saveSymptom(
                            Symptom(
                                patientId = 1,
                                recordedAt = System.currentTimeMillis(),
                                type = symptomType,
                                severity = severity,
                                notes = notes.takeIf { it.isNotEmpty() }
                            )
                        )
                    }
                    onNavigateBack()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Guardar")
            }
        }
    }
}
