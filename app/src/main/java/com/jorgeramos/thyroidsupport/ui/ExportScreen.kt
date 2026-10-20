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
import com.jorgeramos.thyroidsupport.presentation.ExportViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen(
    patientId: Long = 1,
    onNavigateBack: () -> Unit,
    viewModel: ExportViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val exportedFile by viewModel.exportedFile.observeAsState()
    val error by viewModel.error.observeAsState()
    val loading by viewModel.loading.observeAsState(false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exportar Datos") },
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
            Text(text = "Seleccione qué datos exportar", style = MaterialTheme.typography.titleMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        scope.launch { viewModel.exportSymptomsCsv(patientId) }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Síntomas")
                }
                Button(
                    onClick = {
                        scope.launch { viewModel.exportLabsCsv(patientId) }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Laboratorio")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        scope.launch { viewModel.exportMedicationsCsv(patientId) }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Medicación")
                }
                Button(
                    onClick = {
                        scope.launch { viewModel.exportSupplementsCsv(patientId) }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Suplementos")
                }
            }

            Button(
                onClick = {
                    scope.launch { viewModel.exportAllZip(patientId) }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Exportar todo (ZIP)")
            }

            exportedFile?.let { file ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Archivo exportado", style = MaterialTheme.typography.titleMedium)
                        Text(text = file.name, style = MaterialTheme.typography.bodyMedium)
                        Text(text = file.absolutePath, style = MaterialTheme.typography.bodySmall)
                        Button(
                            onClick = {
                                viewModel.shareFile(file)
                            }
                        ) {
                            Text("Compartir archivo")
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
