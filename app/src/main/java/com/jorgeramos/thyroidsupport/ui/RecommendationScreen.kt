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
import com.jorgeramos.thyroidsupport.domain.LabResultAnalysis
import com.jorgeramos.thyroidsupport.domain.Recommendation
import com.jorgeramos.thyroidsupport.domain.ThyroidStatus
import com.jorgeramos.thyroidsupport.presentation.RecommendationViewModel
import kotlinx.coroutines.launch

@Composable
fun RecommendationScreen(
    patientId: Long = 1, // TODO: reemplazar con id real del paciente
    onNavigateBack: () -> Unit,
    viewModel: RecommendationViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val recommendation by viewModel.recommendation.observeAsState()
    val analysis by viewModel.analysis.observeAsState()
    val patient by viewModel.patient.observeAsState()
    val loading by viewModel.loading.observeAsState(false)

    if (loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopAppBar(
            title = { Text("Recomendaciones") },
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

        patient?.let { p ->
            Text(text = "Paciente: ${p.name}", style = MaterialTheme.typography.titleMedium)
        }

        analysis?.let { a ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Análisis de laboratorio", style = MaterialTheme.typography.titleMedium)
                    Text(text = a.overallInterpretation, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Confianza: ${(a.confidence * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall
                    )
                    if (a.tpoAbStatus != null) {
                        Text(text = "Anti-TPO: ${a.tpoAbStatus}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        recommendation?.let { rec ->
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "Recomendaciones", style = MaterialTheme.typography.headlineSmall)

                rec.medication?.let { med ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Medicación", style = MaterialTheme.typography.titleMedium)
                            Text(text = med.name, style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Dosis: ${med.dose}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Frecuencia: ${med.frequency}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = med.rationale, style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = "Evidencia: ${med.evidenceLevel}",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }
                }

                rec.supplement?.let { sup ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Suplemento", style = MaterialTheme.typography.titleMedium)
                            Text(text = sup.name, style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Dosis: ${sup.dose}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = sup.rationale, style = MaterialTheme.typography.bodySmall)
                            sup.contraindications.forEach { contra ->
                                Text(text = "⚠️ $contra", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error))
                            }
                            Text(
                                text = "Evidencia: ${sup.evidenceLevel}",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }
                }

                rec.diet?.let { diet ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Alimentación", style = MaterialTheme.typography.titleMedium)
                            Text(text = diet.focus, style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = "Incluir: ${diet.foodsToInclude.joinToString(", ")}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Limitar: ${diet.foodsToLimit.joinToString(", ")}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(text = diet.rationale, style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = "Evidencia: ${diet.evidenceLevel}",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }
                }

                rec.exercise?.let { ex ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Ejercicio", style = MaterialTheme.typography.titleMedium)
                            Text(text = ex.type, style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Frecuencia: ${ex.frequency}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "Duración: ${ex.duration}", style = MaterialTheme.typography.bodySmall)
                            Text(text = ex.rationale, style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = "Evidencia: ${ex.evidenceLevel}",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }
                }

                rec.warning?.let { warning ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "⚠️ Advertencias",
                                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onErrorContainer)
                            )
                            Text(
                                text = warning,
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onErrorContainer)
                            )
                        }
                    }
                }

                rec.sources.forEach { source ->
                    Text(
                        text = "Fuente: $source",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontStyle = androidx.compose.ui.text.FontStyle.Italic
                        )
                    )
                }
            }
        }

        if (recommendation == null && analysis == null) {
            Text(text = "No hay datos de laboratorio para generar recomendaciones.")
        }
    }
}
