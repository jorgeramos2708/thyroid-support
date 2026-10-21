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
import com.jorgeramos.thyroidsupport.presentation.MedicationViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationScreen(
    onNavigateBack: () -> Unit,
    viewModel: MedicationViewModel = hiltViewModel()
) {
    val state by viewModel.medicationState.observeAsState()
    val loading by viewModel.loading.observeAsState(false)
    val error by viewModel.error.observeAsState(false)

    val medication = state?.medication
    val logs = state?.logs ?: emptyList()
    val consistency = state?.consistencyPercentage ?: 0

    val daysOfWeek = viewModel.getDaysOfWeekLogs(logs)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, top = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Encabezado
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack
            ) {
                Box(
                    modifier = Modifier.size(48.dp)
                        .background(MaterialTheme.colorScheme.card, shape = RoundedCornerShape(24.dp))
                        .border(1.dp, MaterialTheme.colorScheme.accent, shape = RoundedCornerShape(24.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.ink
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Recordatorio",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.accent
                )
            }
        }

        Text(
            text = "Tratamiento actual",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.accent
        )

        Text(
            text = "Medicación",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.ink
        )

        // Tarjeta hero de medicación
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.hero
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = 0.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = medication?.name ?: "Levotiroxina",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFDDEBE2),
                            contentColor = MaterialTheme.colorScheme.accent
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Recordatorio",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Recordatorio",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.accent
                            )
                        }
                    }
                }

                Text(
                    text = "${medication?.dose ?: "75 mcg"}",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight(500)
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = "${medication?.frequency ?: "1 vez al día"} · ${medication?.startDate?.let { "en ayunas" } ?: "en ayunas"} · ${medication?.startDate?.let { "7:00 am" } ?: "7:00 am"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                )

                Text(
                    text = "Marca actual: Marca A (ejemplo)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        }

        // Checkmarks semanales
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CONSTANCIA",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 0.14.em
                    ),
                    color = MaterialTheme.colorScheme.accent
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$consistency% de ${logs.size} días",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.ink
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                daysOfWeek.forEach { day ->
                    WeeklyDayCheckbox(
                        day = day.dayLetter,
                        number = day.dayNumber,
                        isTaken = day.isTaken,
                        isToday = day.isToday,
                        onClick = { /* TODO: Implementar toggle */ }
                    )
                }
            }
        }

        // Otros suplementos
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "OTROS SUPLEMENTOS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 0.14.em
                    ),
                    color = MaterialTheme.colorScheme.accent
                )
                TextButton(
                    onClick = {}
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "+ Agregar",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.accent
                        )
                    }
                }
            }

            SupplementRow(
                name = "Calcio",
                subtitle = "Separado de la dosis",
                time = "1:00 pm"
            )

            SupplementRow(
                name = "Hierro",
                subtitle = "Separado de la dosis",
                time = "—"
            )
        }

        // Aviso
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
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Aviso",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.accent
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Antes de tu laboratorio: pregunta a tu médico si debes pausar la biotina u otros suplementos, porque pueden alterar algunos resultados.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.ink
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón Registrar cambio de dosis o marca
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
                Text(text = "Registrar cambio de dosis o marca")
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
fun WeeklyDayCheckbox(
    day: String,
    number: Int,
    isTaken: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.size(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    if (isTaken) MaterialTheme.colorScheme.gold else MaterialTheme.colorScheme.card,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    if (isToday) 2.dp else 1.dp,
                    MaterialTheme.colorScheme.accent,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            if (isTaken) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Tomado",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF2A2110)
                )
            }
        }

        Text(
            text = day,
            style = MaterialTheme.typography.bodySmall,
            color = if (isToday) MaterialTheme.colorScheme.accent else MaterialTheme.colorScheme.inkSecondary
        )
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.inkSecondary
        )
    }
}

@Composable
fun SupplementRow(
    name: String,
    subtitle: String,
    time: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Attachment,
                contentDescription = name,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.accent
            )
        }
        Column(
            modifier = Modifier.weight(1f).padding(start = 12.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.ink
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.inkSecondary
            )
        }
        Text(
            text = time,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.ink
        )
    }
}
