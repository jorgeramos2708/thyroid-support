package com.jorgeramos.thyroidsupport.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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

            // Botones de acceso rápido - 2 columnas, 5 filas
            QuickActionsGrid(
                buttons = listOf(
                    QuickAction("Síntomas", Icons.Default.Report, onNavigateToSymptomLog),
                    QuickAction("Laboratorio", Icons.Default.Science, onNavigateToLab),
                    QuickAction("Medicación", Icons.Default.Medication, onNavigateToMedication),
                    QuickAction("Recomendaciones", Icons.Default.Lightbulb, onNavigateToRecommendation),
                    QuickAction("Exportar", Icons.Default.Share, onNavigateToExport),
                    QuickAction("Alimentación", Icons.Default.FoodBank, onNavigateToMealPlan),
                    QuickAction("Ejercicio", Icons.Default.FitnessCenter, onNavigateToExercise),
                    QuickAction("Diario", Icons.Default.Edit, onNavigateToDiary),
                    QuickAction("Asistente IA", Icons.Default.SmartScreen, onNavigateToOpenAI),
                    QuickAction("Ajustes", Icons.Default.Settings, onNavigateToSettings)
                )
            )

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

data class QuickAction(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val onClick: () -> Unit
)

@Composable
fun QuickActionsGrid(buttons: List<QuickAction>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(buttons) { action ->
            QuickActionButton(
                label = action.label,
                icon = action.icon,
                onClick = action.onClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun QuickActionButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().height(64.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
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
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}
