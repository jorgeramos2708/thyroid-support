package com.jorgeramos.thyroidsupport.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.domain.Lab
import com.jorgeramos.thyroidsupport.presentation.LabViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LabScreen(
    patientId: Long = 1,
    onNavigateBack: () -> Unit,
    viewModel: LabViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val labs by viewModel.labs.observeAsState()

    var testType by remember { mutableStateOf("TSH") }
    var value by remember { mutableStateOf("") }
    var refLow by remember { mutableStateOf("") }
    var refHigh by remember { mutableStateOf("") }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Registro de Laboratorio", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = testType,
            onValueChange = { testType = it },
            label = { Text("Tipo de prueba (TSH, FT4, FT3…)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            label = { Text("Valor") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = refLow,
            onValueChange = { refLow = it },
            label = { Text("Rango bajo") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = refHigh,
            onValueChange = { refHigh = it },
            label = { Text("Rango alto") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                        val v = value.toDoubleOrNull() ?: 0.0
                        val rl = refLow.toDoubleOrNull() ?: 0.0
                        val rh = refHigh.toDoubleOrNull() ?: 0.0
                        viewModel.addLab(
                            Lab(
                                patientId = patientId,
                                testDate = System.currentTimeMillis(),
                                testType = testType,
                                value = v,
                                referenceLow = rl,
                                referenceHigh = rh
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

        labs?.let { list ->
            if (list.isNotEmpty()) {
                Text(text = "Últimos resultados", style = MaterialTheme.typography.titleMedium)
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(list) { lab ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = lab.testType, style = MaterialTheme.typography.bodyLarge)
                                Text(text = "Valor: ${lab.value}", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    text = "Rango: ${lab.referenceLow} - ${lab.referenceHigh}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = dateFormat.format(Date(lab.testDate)),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
