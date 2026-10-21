package com.jorgeramos.thyroidsupport.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.domain.SymptomAdviceData
import com.jorgeramos.thyroidsupport.presentation.AdviceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdviceScreen(
    onNavigateBack: () -> Unit,
    viewModel: AdviceViewModel = hiltViewModel()
) {
    val selectedSymptoms by viewModel.selectedSymptoms.observeAsState(emptyList())
    val activeAdvices by viewModel.activeAdvices.observeAsState(emptyList())
    val warningAdvice by viewModel.warningAdvice.observeAsState()
    val hasWarning by viewModel.hasWarning.observeAsState(false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, top = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado con flecha de volver y título "Consejos"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp)
                    .background(MaterialTheme.colorScheme.card, shape = RoundedCornerShape(24.dp))
                    .border(1.dp, MaterialTheme.colorScheme.accent, shape = RoundedCornerShape(24.dp))
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.ink
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Consejos",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.ink
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Text(
            text = "Según tus síntomas de hoy",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.inkSecondary
        )

        // Chips de síntomas activos
        if (selectedSymptoms.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedSymptoms.forEach { symptomId ->
                    val advice = SymptomAdviceData.allAdvices.firstOrNull { it.symptomId == symptomId }
                    if (advice != null) {
                        Chip(
                            selected = true,
                            onClick = { viewModel.toggleSymptom(symptomId) },
                            colors = ChipDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.tint,
                                selectedContainerColor = MaterialTheme.colorScheme.tint,
                                unselectedContainerColor = MaterialTheme.colorScheme.tint,
                                selectedContentColor = MaterialTheme.colorScheme.accent,
                                unselectedContentColor = MaterialTheme.colorScheme.accent
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = advice.symptomName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.accent
                                )
                                Text(
                                    text = "${advice.severity}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight(700)
                                    ),
                                    color = MaterialTheme.colorScheme.accent
                                )
                            }
                        }
                    }
                }
            }
        }

        // Tarjetas de consejos
        if (activeAdvices.isNotEmpty()) {
            activeAdvices.forEach { advice ->
                AdviceCard(
                    title = advice.title,
                    tips = advice.tips,
                    severity = advice.severity
                )
            }
        } else {
            // Estado vacío
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.card
                ),
                shape = RoundedCornerShape(22.dp),
                elevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SentimentVerySatisfied,
                        contentDescription = "Sin síntomas",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.accent
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Hoy no marcaste síntomas intensos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.inkSecondary
                    )
                }
            }
        }

        // Tarjeta de advertencia si hay síntomas severos
        if (hasWarning && warningAdvice != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tint
                ),
                shape = RoundedCornerShape(18.dp),
                elevation = 0.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.accent)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Advertencia",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.accent
                    )
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = warningAdvice.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight(700)
                            ),
                            color = MaterialTheme.colorScheme.accent
                        )
                        Text(
                            text = warningAdvice.tips.joinToString(" "),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.ink
                        )
                        Text(
                            text = "Estos consejos no sustituyen una valoración médica.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontStyle = androidx.compose.ui.text.FontStyle.Italic
                            ),
                            color = MaterialTheme.colorScheme.inkSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón "Añadir al resumen para mi médico"
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.accent,
                contentColor = Color(0xFFFFFFFF)
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Compartir",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Añadir al resumen para mi médico")
            }
        }
    }
}

@Composable
fun AdviceCard(
    title: String,
    tips: List<String>,
    severity: Int
) {
    val severityColor = when {
        severity <= 3 -> MaterialTheme.colorScheme.primary
        severity <= 6 -> Color(0xFFF9A825)
        else -> MaterialTheme.colorScheme.error
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.card
        ),
        shape = RoundedCornerShape(18.dp),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Science,
                    contentDescription = "Consejo",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.accent
                )
            }
            Column(
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight(700)
                    ),
                    color = MaterialTheme.colorScheme.ink
                )
                tips.forEach { tip ->
                    Text(
                        text = "• $tip",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.inkSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
