package com.jorgeramos.thyroidsupport.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.presentation.DashboardViewModel
import kotlinx.coroutines.flow.collectAsState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DashboardScreen(
    onNavigateToSymptomLog: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val patient by viewModel.currentPatient.observeAsState()
    val symptoms by viewModel.symptoms.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thyroid Support") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = patient?.name?.let { "Bienvenido, $it" } ?: "Sin paciente",
                style = MaterialTheme.typography.headlineMedium
            )

            Button(
                onClick = onNavigateToSymptomLog,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Síntoma")
            }

            symptoms?.let { list ->
                if (list.isEmpty()) {
                    Text("Sin síntomas registrados")
                } else {
                    Text("Síntomas recientes:")
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(list) { symptom ->
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = symptom.type,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "Gravedad: ${symptom.severity}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    symptom.notes?.let { note ->
                                        Text(text = note, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
