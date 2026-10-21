package com.jorgeramos.thyroidsupport.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.presentation.DailySymptomViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailySymptomScreen(
    onNavigateBack: () -> Unit,
    viewModel: DailySymptomViewModel = hiltViewModel()
) {
    val latestEntry by viewModel.entry.observeAsState()
    val loading by viewModel.loading.observeAsState(false)
    val error by viewModel.error.observeAsState()

    var fatigue by remember { mutableIntStateOf(latestEntry?.fatigue ?: 0) }
    var coldSensitivity by remember { mutableIntStateOf(latestEntry?.coldSensitivity ?: 0) }
    var palpitations by remember { mutableIntStateOf(latestEntry?.palpitations ?: 0) }
    var lowMood by remember { mutableIntStateOf(latestEntry?.lowMood ?: 0) }
    var sleepProblems by remember { mutableIntStateOf(latestEntry?.sleepProblems ?: 0) }
    var weightKg by remember { mutableStateOf(latestEntry?.weightKg?.toString() ?: "") }
    var heartRate by remember { mutableStateOf(latestEntry?.heartRate?.toString() ?: "") }
    var doctorNote by remember { mutableStateOf(latestEntry?.doctorNote ?: "") }

    val dateFormat = SimpleDateFormat("EEEE, dd 'de' MMMM", Locale("es", "MX"))
    val today = dateFormat.format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopAppBar(
            title = { Text("Síntomas de hoy") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        Text(
            text = today.capitalize(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "¿CÓMO TE SIENTES?",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )

        Text(
            text = "0 nada · 10 muy intenso",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        )

        SymptomSliderRow(
            label = "Cansancio",
            value = fatigue,
            onValueChange = { fatigue = it }
        )

        SymptomSliderRow(
            label = "Sensibilidad al frío",
            value = coldSensitivity,
            onValueChange = { coldSensitivity = it }
        )

        SymptomSliderRow(
            label = "Palpitaciones",
            value = palpitations,
            onValueChange = { palpitations = it }
        )

        SymptomSliderRow(
            label = "Ánimo bajo",
            value = lowMood,
            onValueChange = { lowMood = it }
        )

        SymptomSliderRow(
            label = "Problemas de sueño",
            value = sleepProblems,
            onValueChange = { sleepProblems = it }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExpandedTextField(
                value = weightKg,
                onValueChange = { weightKg = it },
                label = "Peso",
                unit = "kg"
            )
            ExpandedTextField(
                value = heartRate,
                onValueChange = { heartRate = it },
                label = "Pulso",
                unit = "lpm"
            )
        }

        OutlinedTextField(
            value = doctorNote,
            onValueChange = { doctorNote = it },
            label = { Text("Nota para tu médico (opcional)") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val weight = weightKg.toDoubleOrNull() ?: 0.0
                val pulse = heartRate.toIntOrNull() ?: 0
                viewModel.saveEntry(
                    fatigue = fatigue,
                    coldSensitivity = coldSensitivity,
                    palpitations = palpitations,
                    lowMood = lowMood,
                    sleepProblems = sleepProblems,
                    weightKg = weight,
                    heartRate = pulse,
                    doctorNote = doctorNote
                )
            },
            modifier = Modifier.fillMaxWidth(),
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
                    imageVector = Icons.Default.Check,
                    contentDescription = "Guardar",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Guardar registro")
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
fun SymptomSliderRow(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = androidx.compose.ui.text.FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }

        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 0f..10f,
            steps = 10,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                thumbColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
fun ExpandedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    unit: String
) {
    Column(
        modifier = Modifier.weight(1f)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = androidx.compose.foundation.text.KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = unit,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.align(Alignment.End)
        )
    }
}
