package com.jorgeramos.thyroidsupport.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.presentation.onboarding.OnboardingStep2ViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingStep2Screen(
    onFinish: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingStep2ViewModel = hiltViewModel()
) {
    val treatment by viewModel.treatment.observeAsState()
    val isValid by viewModel.isValid.observeAsState(false)

    var medicationName by remember { mutableStateOf(treatment?.medicationName ?: "") }
    var dose by remember { mutableStateOf(treatment?.dose ?: "") }
    var time by remember { mutableStateOf(treatment?.time ?: "7:00 am") }
    var dailyReminder by remember { mutableStateOf(treatment?.dailyReminder == true) }
    var tshMin by remember { mutableStateOf(treatment?.tshMin ?: "") }
    var tshMax by remember { mutableStateOf(treatment?.tshMax ?: "") }
    var nextLabDate by remember { mutableStateOf(treatment?.nextLabDate ?: "") }
    var nextMedicalAppointment by remember { mutableStateOf(treatment?.nextMedicalAppointment ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopAppBar(
            title = { Text("Tu tratamiento") },
            navigationIcon = {
                IconButton(onClick = onBack) {
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
            text = "Paso 2 de 2",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )

        Text(
            text = "MEDICACIÓN PRINCIPAL",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )

        OutlinedTextField(
            value = medicationName,
            onValueChange = {
                medicationName = it
                viewModel.updateMedicationName(it)
            },
            label = { Text("Medicamento") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = dose,
                onValueChange = {
                    dose = it
                    viewModel.updateDose(it)
                },
                label = { Text("Dosis") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = time,
                onValueChange = {
                    time = it
                    viewModel.updateTime(it)
                },
                label = { Text("Hora") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Switch(
                checked = dailyReminder,
                onCheckedChange = { dailyReminder = it; viewModel.toggleDailyReminder(it) }
            )
            Text(
                text = "Recordatorio diario",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = "METAS QUE INDICÓ TU MÉDICO",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = tshMin,
                onValueChange = {
                    tshMin = it
                    viewModel.updateTshMin(it)
                },
                label = { Text("TSH mínima") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = tshMax,
                onValueChange = {
                    tshMax = it
                    viewModel.updateTshMax(it)
                },
                label = { Text("TSH máxima") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Text(
            text = "Escríbelas tal como te las dio tu médico. La app no sugiere metas ni interpreta tus resultados.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "PRÓXIMOS CONTROLES",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = nextLabDate,
                onValueChange = {
                    nextLabDate = it
                    viewModel.updateNextLabDate(it)
                },
                label = { Text("Laboratorio") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = nextMedicalAppointment,
                onValueChange = {
                    nextMedicalAppointment = it
                    viewModel.updateNextMedicalAppointment(it)
                },
                label = { Text("Cita médica") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (isValid) {
                    onFinish()
                }
            },
            enabled = isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(text = "Guardar y empezar")
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Guardar",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
