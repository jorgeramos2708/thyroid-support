package com.jorgeramos.thyroidsupport.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.FontWeight
import androidx.compose.ui.text.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.presentation.home.HomeViewModel
import kotlinx.coroutines.flow.collectAsState

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
    val weekDays by viewModel.currentWeek.collectAsState()
    val nextDose by viewModel.nextDose.collectAsState()
    val todayItems by viewModel.todayItems.collectAsState()
    val nextControl by viewModel.nextControl.collectAsState()
    val quickActions by viewModel.quickActions.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, top = 24.dp)
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
        WeekCalendar(days = weekDays)

        // 3. Tarjeta de próxima dosis (hero)
        NextDoseCard(dose = nextDose)

        // 4. Tarjeta HOY
        TodayCard(items = todayItems)

        // 5. Tarjeta PRÓXIMO CONTROL
        NextControlCard(control = nextControl)

        // 6. TODO LO DEMÁS
        QuickActionsGrid(actions = quickActions)
    }
}

@Composable
fun WeekCalendar(days: List<HomeViewModel.WeekDay>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(days) { day ->
            WeekDayItem(
                day = day,
                isSelected = day.isSelected,
                isToday = day.isToday
            )
        }
    }
}

@Composable
fun WeekDayItem(
    day: HomeViewModel.WeekDay,
    isSelected: Boolean,
    isToday: Boolean
) {
    Column(
        modifier = Modifier.size(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = day.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.inkSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(40.dp, 52.dp)
                .background(
                    if (isToday || isSelected) MaterialTheme.colorScheme.accent
                    else if (day.number < 20) MaterialTheme.colorScheme.tint
                    else MaterialTheme.colorScheme.card,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Text(
                text = day.number.toString(),
                style = MaterialTheme.typography.labelLarge,
                color = if (isToday || isSelected) Color(0xFFFFFFFF) else MaterialTheme.colorScheme.accent,
                fontWeight = FontWeight(700)
            )
        }
    }
}

@Composable
fun NextDoseCard(dose: HomeViewModel.NextDose?) {
    dose?.let { d ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.hero
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = 0.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
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
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE3C58B),
                        contentColor = Color(0xFF2A2110)
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Tomada",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Tomada")
                    }
                }
            }
        }
    }
}

@Composable
fun TodayCard(items: List<HomeViewModel.TodayItem>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "HOY",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 0.14.em
            ),
            color = MaterialTheme.colorScheme.accent,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
        )
        items.forEachIndexed { index, item ->
            TodayItemRow(item = item, isLast = index == items.size - 1)
        }
    }
}

@Composable
fun TodayItemRow(item: HomeViewModel.TodayItem, isLast: Boolean) {
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
        elevation = 0.dp
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
                    tint = MaterialTheme.colorScheme.accent
                )
            }
            Column(
                modifier = Modifier.weight(1f).padding(start = 12.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.ink
                )
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inkSecondary
                )
            }
            Text(
                text = item.time,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight(700)
                ),
                color = MaterialTheme.colorScheme.accent
            )
        }
        if (!isLast) {
            Divider(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.line)
        }
    }
}

@Composable
fun NextControlCard(control: HomeViewModel.NextControl?) {
    control?.let { c ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.card
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = 0.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.accent)
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
                    tint = MaterialTheme.colorScheme.accent
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = c.title,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight(700)
                        ),
                        color = MaterialTheme.colorScheme.accent
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
fun QuickActionsGrid(actions: List<HomeViewModel.QuickAction>) {
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
                color = MaterialTheme.colorScheme.accent
            )
        }
        Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), color = MaterialTheme.colorScheme.line)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(actions) { action ->
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
                    isDisabled = action.label == "Asistente"
                )
            }
        }
    }
}

@Composable
fun QuickActionItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isDisabled: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    if (isDisabled) MaterialTheme.colorScheme.tint else MaterialTheme.colorScheme.card,
                    shape = androidx.compose.foundation.shape.CircleShape
                )
                .border(if (isDisabled) 0.dp else 1.dp, MaterialTheme.colorScheme.accent, shape = androidx.compose.foundation.shape.CircleShape)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(20.dp),
                tint = if (isDisabled) MaterialTheme.colorScheme.inkSecondary else MaterialTheme.colorScheme.accent
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isDisabled) "$label\nPróximamente" else label,
            style = MaterialTheme.typography.labelLarge,
            color = if (isDisabled) MaterialTheme.colorScheme.inkSecondary else MaterialTheme.colorScheme.ink,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}
