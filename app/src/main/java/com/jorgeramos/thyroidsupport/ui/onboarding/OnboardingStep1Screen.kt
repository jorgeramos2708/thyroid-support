package com.jorgeramos.thyroidsupport.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.presentation.onboarding.ConditionSubtype
import com.jorgeramos.thyroidsupport.presentation.onboarding.ConditionType
import com.jorgeramos.thyroidsupport.presentation.onboarding.OnboardingStep1ViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingStep1Screen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingStep1ViewModel = hiltViewModel()
) {
    val profile by viewModel.profile.observeAsState()
    val conditionType by viewModel.selectedConditionType.observeAsState()
    val conditionSubtype by viewModel.selectedConditionSubtype.observeAsState()
    val availableSubtypes by viewModel.availableSubtypes.observeAsState(emptyList())
    val showSubtypeSelection by viewModel.showSubtypeSelection.observeAsState(false)
    val isValid by viewModel.isValid.observeAsState(false)

    var name by remember { mutableStateOf(profile?.name ?: "") }
    var birthYear by remember { mutableStateOf(profile?.birthYear?.toString() ?: "") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopAppBar(
            title = { Text("Tu perfil") },
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
            text = "Paso 1 de 2",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )

        Text(
            text = "¿Cómo te llamas?",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                viewModel.updateName(it)
            },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
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
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "CONDICIÓN",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )

        ConditionTypeOption(
            conditionType = ConditionType.HYPOTHYROIDISM,
            isSelected = conditionType == ConditionType.HYPOTHYROIDISM,
            onClick = { viewModel.selectConditionType(ConditionType.HYPOTHYROIDISM) }
        )

        ConditionTypeOption(
            conditionType = ConditionType.HYPERTHYROIDISM,
            isSelected = conditionType == ConditionType.HYPERTHYROIDISM,
            onClick = { viewModel.selectConditionType(ConditionType.HYPERTHYROIDISM) }
        )

        ConditionTypeOption(
            conditionType = ConditionType.OTHER,
            isSelected = conditionType == ConditionType.OTHER,
            onClick = { viewModel.selectConditionType(ConditionType.OTHER) }
        )

        // Subtype selection shown only when Hypothyroidism or Hyperthyroidism is selected
        if (showSubtypeSelection && availableSubtypes.isNotEmpty()) {
            Text(
                text = "TIPO DE ${conditionType?.title?.uppercase()}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )

            ConditionSubtypeOption(
                subtype = availableSubtypes[0],
                isSelected = conditionSubtype == availableSubtypes[0],
                onClick = { viewModel.selectConditionSubtype(availableSubtypes[0]) },
                description = availableSubtypes[0].description
            )

            availableSubtypes.drop(1).forEach { subtype ->
                ConditionSubtypeOption(
                    subtype = subtype,
                    isSelected = conditionSubtype == subtype,
                    onClick = { viewModel.selectConditionSubtype(subtype) },
                    description = subtype.description
                )
            }
        }

        if (conditionType == ConditionType.OTHER) {
            Text(
                text = "Seleccionaste 'Otra o no lo sé'. Verás consejos generales adaptados a tu perfil.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "¿ALGO DE ESTO APLICA?",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
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
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (isValid) {
                    onNext()
                }
            },
            enabled = isValid,
            modifier = Modifier.fillMaxWidth()
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
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = { onClick() },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Column(
                modifier = Modifier.weight(1f).padding(start = 12.dp)
            ) {
                Text(
                    text = conditionType.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = conditionType.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
fun ConditionSubtypeOption(
    subtype: ConditionSubtype,
    isSelected: Boolean,
    onClick: () -> Unit,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = { onClick() },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = subtype.title,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
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
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f).padding(start = 12.dp)
        )
    }
}
