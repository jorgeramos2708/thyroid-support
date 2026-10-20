package com.jorgeramos.thyroidsupport.ui

import android.net.Uri
import androidx.core.content.FileProvider
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
import com.jorgeramos.thyroidsupport.presentation.ExportViewModel
import kotlinx.coroutines.launch

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

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopAppBar(
            title = { Text("Exportar Datos") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = androidx.compose.material3.Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            },
            colors = androidx.compose.material3.TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )

        Text(text = "Seleccione qué datos exportar", style = MaterialTheme.typography.headlineSmall)

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
                Column(modifier = Modifier.padding(16.dp)) {
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
