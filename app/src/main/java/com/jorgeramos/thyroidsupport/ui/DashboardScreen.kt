package com.jorgeramos.thyroidsupport.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.presentation.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToSymptomLog: () -> Unit,
    onNavigateToLab: () -> Unit,
    onNavigateToMedication: () -> Unit,
    onNavigateToRecommendation: () -> Unit,
    onNavigateToExport: () -> Unit,
    onNavigateToMealPlan: () -> Unit,
    onNavigateToExercise: () -> Unit,
    onNavigateToDiary: () -> Unit,
    onNavigateToOpenAI: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val patient by viewModel.currentPatient.observeAsState()
    val symptoms by viewModel.symptoms.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thyroid Support") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = patient?.name?.let { "Bienvenido, $it" } ?: "Sin paciente",
                style = MaterialTheme.typography.headlineMedium
            )

            // Quick action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExpandedButton(
                    onClick = onNavigateToSymptomLog,
                    label = "Síntomas",
                    icon = Icons.Default.Report
                )
                ExpandedButton(
                    onClick = onNavigateToLab,
                    label = "Laboratorio",
                    icon = Icons.Default.Science
                )
                ExpandedButton(
                    onClick = onNavigateToMedication,
                    label = "Medicación",
                    icon = Icons.Default.Medication
                )
                ExpandedButton(
                    onClick = onNavigateToRecommendation,
                    label = "Recomendaciones",
                    icon = Icons.Default.Lightbulb
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExpandedButton(
                    onClick = onNavigateToMealPlan,
                    label = "Alimentación",
                    icon = Icons.Default.FoodBank
                )
                ExpandedButton(
                    onClick = onNavigateToExercise,
                    label = "Ejercicio",
                    icon = Icons.Default.FitnessCenter
                )
                ExpandedButton(
                    onClick = onNavigateToDiary,
                    label = "Diario",
                    icon = Icons.Default.Edit
                )
                ExpandedButton(
                    onClick = onNavigateToOpenAI,
                    label = "Asistente IA",
                    icon = Icons.Default.SmartScreen
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExpandedButton(
                    onClick = onNavigateToExport,
                    label = "Exportar",
                    icon = Icons.Default.Share
                )
                ExpandedButton(
                    onClick = onNavigateToSettings,
                    label = "Ajustes",
                    icon = Icons.Default.Settings
                )
            }

            Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp))

            // Síntomas recientes
            Text(text = "Síntomas recientes", style = MaterialTheme.typography.titleMedium)

            symptoms?.let { list ->
                if (list.isEmpty()) {
                    Text("Sin síntomas registrados")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
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

@Composable
fun ExpandedButton(
    onClick: () -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = label, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
