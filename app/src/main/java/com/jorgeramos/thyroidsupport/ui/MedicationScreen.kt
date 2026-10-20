package com.jorgeramos.thyroidsupport.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.domain.Medication
import com.jorgeramos.thyroidsupport.presentation.MedicationViewModel
import kotlinx.coroutines.launch

@Composable
fun MedicationScreen(
    patientId: Long = 1,
    onNavigateBack: () -> Unit,
    viewModel: MedicationViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val medications by viewModel.medications.observeAsState()

    var name by remember { mutableStateOf("") }
    var dose by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("OD") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Registro de Medicación", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre (Levotiroxina, Eutirox…)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = dose,
            onValueChange = { dose = it },
            label = { Text("Dosis (ej. 0.05 mg)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = frequency,
            onValueChange = { frequency = it },
            label = { Text("Frecuencia (OD, BID, TID)") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onNavigateBack) {
                Text("Cancelar")
            }

            Button(
                onClick = {
                    scope.launch {
                        viewModel.addMedication(
                            Medication(
                                patientId = patientId,
                                name = name,
                                dose = dose,
                                frequency = frequency,
                                startDate = System.currentTimeMillis()
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

        medications?.let { list ->
            if (list.isNotEmpty()) {
                Text(text = "Medicamentos registrados", style = MaterialTheme.typography.titleMedium)
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(list) { med ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = med.name, style = MaterialTheme.typography.bodyLarge)
                                Text(text = "Dosis: ${med.dose} · ${med.frequency}", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
