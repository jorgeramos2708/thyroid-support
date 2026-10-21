package com.jorgeramos.thyroidsupport.ui.onboarding

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jorgeramos.thyroidsupport.presentation.onboarding.PatientProfile
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.presentation.onboarding.ConditionType
import com.jorgeramos.thyroidsupport.presentation.onboarding.OnboardingStep1ViewModel
import com.jorgeramos.thyroidsupport.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingStep1Screen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    onNavigateToSubtype: () -> Unit,
    viewModel: OnboardingStep1ViewModel = hiltViewModel()
) {
    // Use observeAsState with an initial value to avoid the delegation issue
    val profileState by viewModel.profile.observeAsState(initial = PatientProfile())
    val profile = profileState ?: PatientProfile()
    val conditionType by viewModel.selectedConditionType.observeAsState()
    val isValid by viewModel.isValid.observeAsState(false)

    var name: String by remember { mutableStateOf(profile.name) }
    var birthYear: String by remember { mutableStateOf(profile.birthYear?.toString() ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Barra de progreso (dos segmentos: accent y tint)
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
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Encabezado con flecha de volver en círculo y título "Paso 1 de 2"
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
                text = "Paso 1 de 2",
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
                textValue = name,
                onValueChange = {
                    name = it
                    viewModel.updateName(it)
                },
                label = "Nombre",
                modifier = Modifier.weight(1f)
            )
            FilledTextField(
                textValue = birthYear,
                onValueChange = { it: String ->
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

        // Etiqueta CONDICIÓN
        Text(
            text = "CONDICIÓN",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 0.14.em
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

        // Etiqueta ¿ALGO DE ESTO APLICA?
        Text(
            text = "¿ALGO DE ESTO APLICA?",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 0.14.em
            ),
            color = MaterialTheme.colorScheme.accent,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Chips con forma de píldora
        PillChip(
            label = "Embarazo o planificación",
            checked = profile.hasPregnancy,
            onCheckedChange = { viewModel.togglePregnancy(it) }
        )

        PillChip(
            label = "Problemas del corazón",
            checked = profile.hasHeartProblems,
            onCheckedChange = { viewModel.toggleHeartProblems(it) }
        )

        PillChip(
            label = "Osteoporosis",
            checked = profile.hasOsteoporosis,
            onCheckedChange = { viewModel.toggleOsteoporosis(it) }
        )

        PillChip(
            label = "Ninguna",
            checked = !profile.hasPregnancy && !profile.hasHeartProblems && !profile.hasOsteoporosis,
            onCheckedChange = { checked ->
                if (checked) {
                    viewModel.togglePregnancy(false)
                    viewModel.toggleHeartProblems(false)
                    viewModel.toggleOsteoporosis(false)
                }
            }
        )

        // Nota al pie
        Text(
            text = "Esto adapta los ejercicios y consejos que ves. Siempre sigue las indicaciones de tu médico.",
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
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
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
fun PillChip(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.accent,
                uncheckedColor = MaterialTheme.colorScheme.line
            ),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.ink,
            modifier = Modifier.weight(1f).padding(start = 12.dp)
        )
    }
}

@Composable
fun FilledTextField(
    textValue: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions? = null
) {
    Column(
        modifier = modifier
    ) {
        TextField(
            value = textValue,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.tint,
                unfocusedContainerColor = MaterialTheme.colorScheme.tint,
                disabledContainerColor = MaterialTheme.colorScheme.tint,
                errorContainerColor = MaterialTheme.colorScheme.tint,
                focusedIndicatorColor = MaterialTheme.colorScheme.accent,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.line,
                focusedTextColor = MaterialTheme.colorScheme.ink,
                unfocusedTextColor = MaterialTheme.colorScheme.ink,
                focusedLabelColor = MaterialTheme.colorScheme.inkSecondary,
                unfocusedLabelColor = MaterialTheme.colorScheme.inkSecondary
            ),
            keyboardOptions = keyboardOptions ?: KeyboardOptions.Default
        )
    }
}
