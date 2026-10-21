package com.jorgeramos.thyroidsupport.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.presentation.home.*

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
    val nextDose by viewModel.nextDose.observeAsState()
    val todayItems by viewModel.todayItems.observeAsState(emptyList())
    val nextControl by viewModel.nextControl.observeAsState()
    val quickActions by viewModel.quickActions.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi semana") },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ajustes"
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with month and week title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Octubre 2026",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Mi semana",
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            // Week calendar
            WeekCalendar(days = weekDays, onDaySelected = { viewModel.selectDay(it) })

            // Next dose card
            NextDoseCard(dose = nextDose, onMarkTaken = { viewModel.toggleDoseTaken() })

            // Today section
            TodaySection(items = todayItems)

            // Next control card
            NextControlCard(control = nextControl)

            // Quick actions grid
            QuickActionsGrid(actions = quickActions)
        }
    }
}

@Composable
fun WeekCalendar(
    days: List<WeekDay>,
    onDaySelected: (WeekDay) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        lazyItems(days) { day: WeekDay ->
            WeekDayItem(
                day = day,
                isSelected = day.isSelected,
                isToday = day.isToday,
                onClick = { onDaySelected(day) }
            )
        }
    }
}

@Composable
fun WeekDayItem(
    day: WeekDay,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .size(56.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = day.name,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected || isToday) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.large
                )
        ) {
            Text(
                text = day.number.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = if (isSelected || isToday) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun NextDoseCard(
    dose: NextDose?,
    onMarkTaken: () -> Unit
) {
    dose?.let { d ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PRÓXIMA DOSIS",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                        )
                    )
                }

                Text(
                    text = d.time,
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${d.medication} ${d.dose} · ${d.instructions}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }

                Button(
                    onClick = onMarkTaken,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Tomada",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (d.isTaken) "Tomada" else "Marcar como tomada",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TodaySection(items: List<TodayItem>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "HOY",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )

        items.forEach { item ->
            TodayItemRow(item = item)
        }
    }
}

@Composable
fun TodayItemRow(item: TodayItem) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (item.icon) {
                    "leaf" -> Icons.Default.FoodBank
                    "attachment" -> Icons.Default.Attachment
                    else -> Icons.Default.Schedule
                },
                contentDescription = item.title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column(
                modifier = Modifier.weight(1f).padding(start = 12.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            Text(
                text = item.time,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Divider(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun NextControlCard(
    control: NextControl?
) {
    control?.let { c ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = "Próximo control",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "PRÓXIMO CONTROL",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    )
                }

                Text(
                    text = c.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = c.subtitle,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun QuickActionsGrid(actions: List<QuickAction>) {
    Text(
        text = "TODO LO DEMÁS",
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(bottom = 8.dp)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxWidth().height(180.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        gridItems(actions) { action: QuickAction ->
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
                }
            )
        }
    }
}

@Composable
fun QuickActionItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
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
