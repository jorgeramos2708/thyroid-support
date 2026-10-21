package com.jorgeramos.thyroidsupport.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.presentation.home.*
import com.jorgeramos.thyroidsupport.ui.theme.card
import com.jorgeramos.thyroidsupport.ui.theme.hero
import com.jorgeramos.thyroidsupport.ui.theme.ink
import com.jorgeramos.thyroidsupport.ui.theme.line
import com.jorgeramos.thyroidsupport.ui.theme.tint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSymptoms: () -> Unit,
    onNavigateToEvolution: () -> Unit,
    onNavigateToExercise: () -> Unit,
    onNavigateToAdvice: () -> Unit,
    onNavigateToExport: () -> Unit,
    onNavigateToAssistant: () -> Unit,
    onNavigateToLab: () -> Unit,
    onNavigateToMedication: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val weekDays by viewModel.currentWeek.observeAsState(emptyList())
    val nextDose by viewModel.nextDose.observeAsState(null)
    val todayItems by viewModel.todayItems.observeAsState(emptyList())
    val nextControl by viewModel.nextControl.observeAsState(null)
    val quickActions by viewModel.quickActions.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 24.dp, end = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Encabezado
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Octubre 2026",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Mi semana",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.ink
                )
            }
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Ajustes",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.ink
                )
            }
        }

        // 2. Tira semanal
        WeekCalendar(
            days = weekDays,
            onDayClick = { day -> viewModel.selectDay(day) }
        )

        // 3. Tarjeta de próxima dosis (hero)
        NextDoseCard(
            dose = nextDose,
            onToggleTaken = { viewModel.toggleDoseTaken() }
        )

        // 4. Tarjeta HOY
        TodayCard(items = todayItems)

        // 5. Tarjeta PRÓXIMO CONTROL
        NextControlCard(control = nextControl)

        // 6. TODO LO DEMÁS
        QuickActionsGrid(
            actions = quickActions,
            onActionClick = { action ->
                when (action.label) {
                    "Síntomas" -> onNavigateToSymptoms()
                    "Mi evolución" -> onNavigateToEvolution()
                    "Ejercicio" -> onNavigateToExercise()
                    "Consejos" -> onNavigateToAdvice()
                    "Exportar" -> onNavigateToExport()
                    "Asistente" -> onNavigateToAssistant()
                }
            }
        )
    }
}

@Composable
fun WeekCalendar(
    days: List<WeekDay>,
    onDayClick: (WeekDay) -> Unit = {}
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(days) { day ->
            WeekDayItem(
                day = day,
                isSelected = day.isSelected,
                isToday = day.isToday,
                onClick = { onDayClick(day) }
            )
        }
    }
}

@Composable
fun WeekDayItem(
    day: WeekDay,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .size(48.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = day.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(40.dp, 52.dp)
                .background(
                    if (isToday || isSelected) MaterialTheme.colorScheme.tertiary
                    else if (day.number < 20) MaterialTheme.colorScheme.tint
                    else MaterialTheme.colorScheme.card,
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.number.toString(),
                style = MaterialTheme.typography.labelLarge,
                color = if (isToday || isSelected) Color(0xFFFFFFFF) else MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight(700)
            )
        }
    }
}

@Composable
fun NextDoseCard(
    dose: NextDose?,
    onToggleTaken: () -> Unit = {}
) {
    dose?.let { d ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.hero
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "PRÓXIMA DOSIS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 0.14.em
                        ),
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                    Text(
                        text = d.time,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight(500)
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "${d.medication} ${d.dose} · ${d.instructions}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onToggleTaken,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE3C58B),
                            contentColor = Color(0xFF2A2110)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Tomada",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = if (d.isTaken) "Tomada" else "Marcar tomada")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TodayCard(items: List<TodayItem>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "HOY",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 0.14.em
            ),
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
        )
        items.forEachIndexed { index, item ->
            TodayItemRow(item = item, isLast = index == items.size - 1)
        }
    }
}

@Composable
fun TodayItemRow(item: TodayItem, isLast: Boolean) {
    val iconVector = when (item.icon) {
        "leaf" -> Icons.Default.FoodBank
        "attachment" -> Icons.Default.Attachment
        else -> Icons.Default.Schedule
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.card
        ),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(36.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = item.title,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.ink
                )
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = item.time,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight(700)
                ),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        if (!isLast) {
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.line
            )
        }
    }
}

@Composable
fun NextControlCard(control: NextControl?) {
    control?.let { c ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.card
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = "Próximo control",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = c.title,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight(700)
                        ),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = c.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.ink
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionsGrid(
    actions: List<QuickAction>,
    onActionClick: (QuickAction) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TODO LO DEMÁS",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 0.14.em
                ),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            color = MaterialTheme.colorScheme.line
        )

        val rows = actions.chunked(2)
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            rows.forEach { rowActions ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    rowActions.forEach { action ->
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            QuickActionItem(
                                label = action.label,
                                icon = when (action.icon) {
                                    "alert_circle" -> Icons.Default.Report
                                    "science" -> Icons.Default.Science
                                    "fitness_center" -> Icons.Default.FitnessCenter
                                    "lightbulb" -> Icons.Default.Lightbulb
                                    "share" -> Icons.Default.Share
                                    "smart_toy" -> Icons.Default.SmartScreen
                                    else -> Icons.Default.Help
                                },
                                isDisabled = action.label == "Asistente",
                                onClick = { onActionClick(action) }
                            )
                        }
                    }
                    if (rowActions.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isDisabled: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isDisabled) { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    if (isDisabled) MaterialTheme.colorScheme.tint else MaterialTheme.colorScheme.card,
                    shape = CircleShape
                )
                .border(
                    if (isDisabled) 0.dp else 1.dp,
                    MaterialTheme.colorScheme.tertiary,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(20.dp),
                tint = if (isDisabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.tertiary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isDisabled) "$label\nPróximamente" else label,
            style = MaterialTheme.typography.labelLarge,
            color = if (isDisabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.ink,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}
