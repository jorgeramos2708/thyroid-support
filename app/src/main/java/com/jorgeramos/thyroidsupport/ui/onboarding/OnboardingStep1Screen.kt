package com.jorgeramos.thyroidsupport.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
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
        TopAppBar(
            title = { Text("Tu perfil") },
            navigationIcon = {
                IconButton(
                    onClick = onBack,
                    enabled = false
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.ink
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        Text(
            text = "Paso 1 de 2",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.accent
        )

        Text(
            text = "¿Cómo te llamas?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.ink
        )

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                viewModel.updateName(it)
            },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.accent,
                unfocusedBorderColor = MaterialTheme.colorScheme.line
            )
        )

        OutlinedTextField(
            value = birthYear,
            onValueChange = {
                birthYear = it
                viewModel.updateBirthYear(it.toIntOrNull() ?: 0)
            },
            label = { Text("Año de nacimiento") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.accent,
                unfocusedBorderColor = MaterialTheme.colorScheme.line
            )
        )

        Text(
            text = "CONDICIÓN",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 0.14.em
            ),
            color = MaterialTheme.colorScheme.accent
        )

        ConditionTypeOption(
            conditionType = ConditionType.HYPOTHYROIDISM,
            isSelected = conditionType == ConditionType.HYPOTHYROIDISM,
            onClick = {
                viewModel.selectConditionType(ConditionType.HYPOTHYROIDISM)
                onNavigateToSubtype()
            }
        )

        ConditionTypeOption(
            conditionType = ConditionType.HYPERTHYROIDISM,
            isSelected = conditionType == ConditionType.HYPERTHYROIDISM,
            onClick = {
                viewModel.selectConditionType(ConditionType.HYPERTHYROIDISM)
                onNavigateToSubtype()
            }
        )

        ConditionTypeOption(
            conditionType = ConditionType.OTHER,
            isSelected = conditionType == ConditionType.OTHER,
            onClick = { viewModel.selectConditionType(ConditionType.OTHER) }
        )

        if (conditionType == ConditionType.OTHER) {
            Text(
                text = "Seleccionaste 'Otra o no lo sé'. Verás consejos generales adaptados a tu perfil.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.inkSecondary
            )
        }

        Text(
            text = "¿ALGO DE ESTO APLICA?",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 0.14.em
            ),
            color = MaterialTheme.colorScheme.accent
        )

        CheckboxRow(
            label = "Embarazo o planificación",
            checked = profile?.hasPregnancy == true,
            onCheckedChange = { viewModel.togglePregnancy(it) }
        )

        CheckboxRow(
            label = "Problemas del corazón",
            checked = profile?.hasHeartProblems == true,
            onCheckedChange = { viewModel.toggleHeartProblems(it) }
        )

        CheckboxRow(
            label = "Osteoporosis",
            checked = profile?.hasOsteoporosis == true,
            onCheckedChange = { viewModel.toggleOsteoporosis(it) }
        )

        Text(
            text = "Esto adapta los ejercicios y consejos que ves. Siempre sigue las indicaciones de tu médico.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.inkSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

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
fun ConditionTypeOption(
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
fun CheckboxRow(
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
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.ink,
            modifier = Modifier.weight(1f).padding(start = 12.dp)
        )
    }
}
