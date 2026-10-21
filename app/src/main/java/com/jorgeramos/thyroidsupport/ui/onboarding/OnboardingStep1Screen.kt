package com.jorgeramos.thyroidsupport.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.presentation.onboarding.ConditionType
import com.jorgeramos.thyroidsupport.presentation.onboarding.OnboardingStep1ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingStep1Screen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    onNavigateToSubtype: () -> Unit,
    viewModel: OnboardingStep1ViewModel = hiltViewModel()
) {
    val profile by viewModel.profile.observeAsState()
    val conditionType by viewModel.selectedConditionType.observeAsState()
    val isValid by viewModel.isValid.observeAsState(false)

    var name by remember { mutableStateOf(profile?.name ?: "") }
    var birthYear by remember { mutableStateOf(profile?.birthYear?.toString() ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, top = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Barra de progreso (dos segmentos: primero en accent)
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
                    .background(MaterialTheme.colorScheme.tint, shape = RoundedCornerShape(3.dp))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .background(MaterialTheme.colorScheme.tint, shape = RoundedCornerShape(3.dp))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Encabezado con flecha de volver en círculo y título "Paso 1 de 3"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp)
                    .background(MaterialTheme.colorScheme.card, shape = RoundedCornerShape(24.dp))
                    .border(1.dp, MaterialTheme.colorScheme.accent, shape = RoundedCornerShape(24.dp))
            ) {
                IconButton(
                    onClick = onBack,
                    enabled = false
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.ink
                    )
                }
            }
            Text(
                text = "Paso 1 de 3",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight(600)
                ),
                color = MaterialTheme.colorScheme.accent
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        // Título "Tu perfil" en Fraunces
        Text(
            text = "Tu perfil",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.ink
        )

        // Dos campos en fila con fondo lleno (tint)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            FilledTextField(
                value = name,
                onValueChange = {
                    name = it
                    viewModel.updateName(it)
                },
                label = "Nombre",
                modifier = Modifier.weight(1f)
            )
            FilledTextField(
                value = birthYear,
                onValueChange = {
                    birthYear = it
                    viewModel.updateBirthYear(it.toIntOrNull() ?: 0)
                },
                label = "Año de nacimiento",
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
        }

        // Etiqueta CONDICIÓN - Roboto, no Fraunces
        Text(
            text = "CONDICIÓN",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 0.14.em,
                fontWeight = FontWeight(600)
            ),
            color = MaterialTheme.colorScheme.accent,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Tres tarjetas de condición
        ConditionTypeCard(
            conditionType = ConditionType.HYPOTHYROIDISM,
            isSelected = conditionType == ConditionType.HYPOTHYROIDISM,
            onClick = {
                viewModel.selectConditionType(ConditionType.HYPOTHYROIDISM)
                onNavigateToSubtype()
            }
        )

        ConditionTypeCard(
            conditionType = ConditionType.HYPERTHYROIDISM,
            isSelected = conditionType == ConditionType.HYPERTHYROIDISM,
            onClick = {
                viewModel.selectConditionType(ConditionType.HYPERTHYROIDISM)
                onNavigateToSubtype()
            }
        )

        ConditionTypeCard(
            conditionType = ConditionType.OTHER,
            isSelected = conditionType == ConditionType.OTHER,
            onClick = { viewModel.selectConditionType(ConditionType.OTHER) }
        )

        // Nota al pie
        Text(
            text = "Podrás cambiar estos datos en Ajustes.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.inkSecondary,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón "Continuar" verde anclado al fondo
        Button(
            onClick = {
                if (isValid) {
                    onNext()
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
                Text(text = "Continuar")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Siguiente",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ConditionTypeCard(
    conditionType: ConditionType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.tint else MaterialTheme.colorScheme.card
        ),
        shape = RoundedCornerShape(22.dp),
        elevation = 0.dp,
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.accent) else BorderStroke(1.dp, MaterialTheme.colorScheme.line)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = { onClick() },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.accent,
                    unselectedColor = MaterialTheme.colorScheme.inkSecondary
                )
            )
            Column(
                modifier = Modifier.weight(1f).padding(start = 12.dp)
            ) {
                Text(
                    text = conditionType.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.ink
                )
                Text(
                    text = conditionType.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inkSecondary
                )
            }
        }
    }
}

@Composable
fun FilledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions? = null
) {
    Column(
        modifier = modifier
    ) {
        androidx.compose.material3.TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colorScheme.tint,
                focusedBackgroundColor = MaterialTheme.colorScheme.tint,
                unfocusedBackgroundColor = MaterialTheme.colorScheme.tint,
                focusedLineColor = MaterialTheme.colorScheme.accent,
                unfocusedLineColor = MaterialTheme.colorScheme.line,
                textColor = MaterialTheme.colorScheme.ink,
                labelColor = MaterialTheme.colorScheme.inkSecondary
            ),
            keyboardOptions = keyboardOptions
        )
    }
}
