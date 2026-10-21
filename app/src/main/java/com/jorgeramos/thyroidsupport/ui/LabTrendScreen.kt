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
import com.jorgeramos.thyroidsupport.presentation.LabTrendViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabTrendScreen(
    onNavigateBack: () -> Unit,
    viewModel: LabTrendViewModel = hiltViewModel()
) {
    val trends by viewModel.trends.observeAsState(emptyList())
    val loading by viewModel.loading.observeAsState(false)
    val error by viewModel.error.observeAsState(false)

    val selectedTestType by remember { mutableStateOf("TSH") }

    val filteredTrends = trends.filter { it.testType == selectedTestType }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, top = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top bar con flecha y título
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
            Text(
                text = "Mi evolución",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.ink
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        // Selector de laboratorio (chips)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val labTypes = listOf("TSH", "FT4 libre", "FT3 libre", "Anti-TPO")
            labTypes.forEach { labType ->
                Chip(
                    selected = selectedTestType == labType,
                    onClick = { selectedTestType = labType },
                    colors = ChipDefaults.colors(
                        containerColor = if (selectedTestType == labType)
                            MaterialTheme.colorScheme.accent
                        else MaterialTheme.colorScheme.tint,
                        selectedContainerColor = MaterialTheme.colorScheme.accent,
                        unselectedContainerColor = MaterialTheme.colorScheme.tint,
                        selectedContentColor = Color(0xFFFFFFFF),
                        unselectedContentColor = MaterialTheme.colorScheme.ink
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = labType, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // Tarjeta de gráfico
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.card
            ),
            shape = RoundedCornerShape(22.dp),
            elevation = 0.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedTestType,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.ink
                    )
                    Text(
                        text = "Datos de ejemplo",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.inkSecondary
                    )
                }

                // Gráfico de línea (Canvas simplificado)
                LabTrendChart(
                    points = filteredTrends,
                    testType = selectedTestType
                )

                Text(
                    text = "Meta indicada por tu médico (0.5–2.5)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inkSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Último resultado
        if (filteredTrends.isNotEmpty()) {
            val latest = filteredTrends.maxByOrNull { it.testDate }
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "ÚLTIMO RESULTADO",
                            style = MaterialTheme.typography.labelSmall.copy(
                                letterSpacing = 0.14.em,
                                fontWeight = FontWeight(600)
                            ),
                            color = MaterialTheme.colorScheme.accent
                        )
                        Text(
                            text = latest.value.toString(),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight(600)
                            ),
                            color = MaterialTheme.colorScheme.ink
                        )
                    }
                    Column(
                        modifier = Modifier.alignment(Alignment.End)
                    ) {
                        val dateFormat = SimpleDateFormat("dd MMM", java.util.Locale("es", "MX"))
                        Text(
                            text = dateFormat.format(java.util.Date(latest.testDate)),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.ink
                        )
                        Text(
                            text = "Dentro de la meta",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.accent
                        )
                    }
                }
            }
        } else {
            Text(
                text = "Aún no hay resultados. Agrega el primero.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.inkSecondary,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }

        // Cambios de dosis
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "CAMBIOS DE DOSIS",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 0.14.em,
                    fontWeight = FontWeight(600)
                ),
                color = MaterialTheme.colorScheme.accent
            )

            Text(
                text = "50 → 75 mcg",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.ink
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Misma marca",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inkSecondary
                )
                Text(
                    text = "14 ago",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inkSecondary
                )
            }
            Divider(modifier = Modifier.fillMaxWidth())
            Text(
                text = "Inicio 50 mcg",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.ink
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Primera dosis registrada",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inkSecondary
                )
                Text(
                    text = "2 abr",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inkSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Agregar resultado
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
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Agregar resultado")
            }
        }

        error?.let { err ->
            Text(
                text = "Error: $err",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.error
                )
            )
        }

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun LabTrendChart(
    points: List<com.jorgeramos.thyroidsupport.domain.LabTrendEntry>,
    testType: String
) {
    // Valores de referencia según el tipo de prueba
    val refLow = when (testType) {
        "TSH" -> 0.4
        "FT4 libre" -> 9.0
        "FT3 libre" -> 3.5
        else -> 0.0
    }
    val refHigh = when (testType) {
        "TSH" -> 4.0
        "FT4 libre" -> 19.0
        "FT3 libre" -> 6.5
        else -> 0.0
    }

    val unit = when (testType) {
        "TSH" -> "mUI/L"
        "FT4 libre" -> "pmol/L"
        "FT3 libre" -> "pmol/L"
        else -> ""
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        onDraw = {
            val width = size.width
            val height = size.height
            val margin = 24.dp.toPx()
            val plotWidth = width - margin * 2
            val plotHeight = height - 60.dp.toPx() // espacio para título y eje X

            if (points.isEmpty()) return

            // Calcular min y max de valores
            val allValues = points.map { it.value } + listOf(refLow, refHigh)
            val minValue = allValues.minOrNull() ?: 0.0
            val maxValue = allValues.maxOrNull() ?: 1.0
            val valueRange = if (maxValue - minValue == 0f) 1f else maxValue - minValue

            val firstDate = points.minByOrNull { it.testDate }?.testDate ?: 0L
            val lastDate = points.maxByOrNull { it.testDate }?.testDate ?: 0L
            val dateRange = if (lastDate - firstDate == 0L) 1L else lastDate - firstDate

            // Dibujar línea de referencia (zona sombreada)
            val refTopY = height - 30.dp.toPx() - ((refHigh - minValue) / valueRange) * plotHeight
            val refBottomY = height - 30.dp.toPx() - ((refLow - minValue) / valueRange) * plotHeight
            drawRect(
                color = MaterialTheme.colorScheme.tint,
                top = refTopY,
                left = margin,
                right = width - margin,
                bottom = refBottomY
            )

            // Dibujar líneas guía horizontales
            drawLine(
                color = MaterialTheme.colorScheme.line,
                start = Offset(margin, refTopY),
                end = Offset(width - margin, refTopY),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = MaterialTheme.colorScheme.line,
                start = Offset(margin, refBottomY),
                end = Offset(width - margin, refBottomY),
                strokeWidth = 1.dp.toPx()
            )

            // Dibujar línea de tendencia
            if (points.size >= 2) {
                val paint = Paint()
                paint.color = MaterialTheme.colorScheme.accent
                paint.style = Paint.Style.Stroke
                paint.strokeWidth = 2.5f.dp.toPx()
                paint.strokeCap = Paint.Cap.Round
                paint.strokeJoin = Paint.Join.Round
                paint.isAntiAlias = true

                val path = Path()
                points.sortedBy { it.testDate }.forEachIndexed { index, point ->
                    val x = margin + ((point.testDate - firstDate).toFloat() / dateRange) * plotWidth
                    val y = height - 30.dp.toPx() - ((point.value - minValue) / valueRange) * plotHeight
                    if (index == 0) {
                        path.moveTo(x, y)
                    } else {
                        path.lineTo(x, y)
                    }
                }
                drawPath(path = path, paint = paint)

                // Dibujar puntos
                points.sortedBy { it.testDate }.forEach { point ->
                    val x = margin + ((point.testDate - firstDate).toFloat() / dateRange) * plotWidth
                    val y = height - 30.dp.toPx() - ((point.value - minValue) / valueRange) * plotHeight

                    // Punto
                    drawCircle(
                        color = MaterialTheme.colorScheme.accent,
                        center = Offset(x, y),
                        radius = 4.dp.toPx()
                    )
                    // Borde blanco
                    drawCircle(
                        color = Color(0xFFFFFFFF),
                        center = Offset(x, y),
                        radius = 3.dp.toPx()
                    )

                    // Etiqueta de valor
                    drawText(
                        text = point.value.toString(),
                        top = y - 16.dp.toPx(),
                        left = x - 20.dp.toPx(),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight(700),
                            color = MaterialTheme.colorScheme.accent
                        )
                    )

                    // Etiqueta de fecha
                    val dateFormat = SimpleDateFormat("MMM", java.util.Locale("es", "MX"))
                    drawText(
                        text = dateFormat.format(java.util.Date(point.testDate)),
                        top = y + 8.dp.toPx(),
                        left = x - 20.dp.toPx(),
                        style = TextStyle(
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.inkSecondary
                        )
                    )
                }
            }
        }
    )
}
