package com.jorgeramos.thyroidsupport.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.presentation.onboarding.OnboardingStep2ViewModel
import com.jorgeramos.thyroidsupport.ui.theme.*

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
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Barra de progreso (ambos segmentos en accent)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .background(MaterialTheme.colorScheme.accent, shape = RoundedCornerShape(3.dp))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .background(MaterialTheme.colorScheme.accent, shape = RoundedCornerShape(3.dp))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Encabezado con flecha de volver y título "Tu tratamiento" en Fraunces
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp)
                    .background(MaterialTheme.colorScheme.card, shape = RoundedCornerShape(24.dp))
                    .border(1.dp, MaterialTheme.colorScheme.accent, shape = RoundedCornerShape(24.dp))
            ) {
                IconButton(onClick = onBack) {
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
                text = "Paso 3 de 3",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight(600)
                ),
                color = MaterialTheme.colorScheme.accent
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Text(
            text = "Tu tratamiento",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.ink
        )

        // MEDICACIÓN PRINCIPAL
        Text(
            text = "MEDICACIÓN PRINCIPAL",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 0.14.em,
                fontWeight = FontWeight(600)
            ),
            color = MaterialTheme.colorScheme.accent,
            modifier = Modifier.padding(top = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.card
            ),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Medicamento",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inkSecondary
                )
                OutlinedTextField(
                    value = medicationName,
                    onValueChange = {
                        medicationName = it
                        viewModel.updateMedicationName(it)
                    },
                    label = { Text("Levotiroxina") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.accent,
                        unfocusedBorderColor = MaterialTheme.colorScheme.line
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    ExpandedTextField(
                        value = dose,
                        onValueChange = {
                            dose = it
                            viewModel.updateDose(it)
                        },
                        label = "Dosis",
                        modifier = Modifier.weight(1f)
                    )
                    ExpandedTextField(
                        value = time,
                        onValueChange = {
                            time = it
                            viewModel.updateTime(it)
                        },
                        label = "Hora",
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        )
                    )
                }
            }
        }

        // Recordatorio diario
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recordatorio diario",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.ink
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = dailyReminder,
                onCheckedChange = { dailyReminder = it; viewModel.toggleDailyReminder(it) },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.accent,
                    checkedThumbColor = Color(0xFFFFFFFF),
                    uncheckedTrackColor = MaterialTheme.colorScheme.line,
                    uncheckedThumbColor = MaterialTheme.colorScheme.line
                )
            )
        }

        // METAS QUE INDICÓ TU MÉDICO
        Text(
            text = "METAS QUE INDICÓ TU MÉDICO",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 0.14.em,
                fontWeight = FontWeight(600)
            ),
            color = MaterialTheme.colorScheme.accent,
            modifier = Modifier.padding(top = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ExpandedTextField(
                value = tshMin,
                onValueChange = {
                    tshMin = it
                    viewModel.updateTshMin(it)
                },
                label = "TSH mínima",
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            ExpandedTextField(
                value = tshMax,
                onValueChange = {
                    tshMax = it
                    viewModel.updateTshMax(it)
                },
                label = "TSH máxima",
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
        }

        Text(
            text = "Escríbelas tal como te las dio tu médico. La app no sugiere metas ni interpreta tus resultados.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.inkSecondary,
            modifier = Modifier.padding(top = 8.dp)
        )

        // PRÓXIMOS CONTROLES
        Text(
            text = "PRÓXIMOS CONTROLES",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 0.14.em,
                fontWeight = FontWeight(600)
            ),
            color = MaterialTheme.colorScheme.accent,
            modifier = Modifier.padding(top = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ExpandedTextField(
                value = nextLabDate,
                onValueChange = {
                    nextLabDate = it
                    viewModel.updateNextLabDate(it)
                },
                label = "Laboratorio",
                modifier = Modifier.weight(1f)
            )
            ExpandedTextField(
                value = nextMedicalAppointment,
                onValueChange = {
                    nextMedicalAppointment = it
                    viewModel.updateNextMedicalAppointment(it)
                },
                label = "Cita médica",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Guardar y empezar
        Button(
            onClick = {
                if (isValid) {
                    onFinish()
                }
            },
            enabled = isValid,
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
                Text(text = "Guardar y empezar")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Guardar",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ExpandedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions? = null
) {
    Column(
        modifier = modifier
    ) {
        val colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.tint,
            unfocusedContainerColor = MaterialTheme.colorScheme.tint,
            disabledContainerColor = MaterialTheme.colorScheme.tint,
            focusedIndicatorColor = MaterialTheme.colorScheme.accent,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.line,
            focusedTextColor = MaterialTheme.colorScheme.ink,
            unfocusedTextColor = MaterialTheme.colorScheme.ink,
            focusedLabelColor = MaterialTheme.colorScheme.inkSecondary,
            unfocusedLabelColor = MaterialTheme.colorScheme.inkSecondary
        )
        if (keyboardOptions != null) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(label) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = colors,
                keyboardOptions = keyboardOptions
            )
        } else {
            TextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(label) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = colors
            )
        }
    }
}
