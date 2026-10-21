package com.jorgeramos.thyroidsupport.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.jorgeramos.thyroidsupport.domain.HealthGroups
import com.jorgeramos.thyroidsupport.presentation.HealthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    viewModel: HealthViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val openGroupId by remember { mutableStateOf(viewModel.getOpenGroupId()) }
    val flags by remember { mutableStateOf(viewModel.getFlags()) }
    val otherNote by remember { mutableStateOf(viewModel.getOtherNote()) }

    val groups = viewModel.groups

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, top = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Barra de progreso (3 segmentos: segundo en accent)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
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

        // Encabezado con flecha de volver y título "Tu salud" en Fraunces
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
                text = "Paso 2 de 3",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight(600)
                ),
                color = MaterialTheme.colorScheme.accent
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Text(
            text = "Tu salud",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.ink
        )

        // Pregunta "¿Algo de esto aplica?"
        Text(
            text = "¿Algo de esto aplica?",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight(600)
            ),
            color = MaterialTheme.colorScheme.ink
        )

        // Nota
        Text(
            text = "Esto adapta los consejos y recordatorios. Siempre sigue las indicaciones de tu médico.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.inkSecondary,
            modifier = Modifier.padding(top = 4.dp)
        )

        // 5 Grupos plegables
        groups.forEach { group ->
            HealthGroupCard(
                group = group,
                isOpen = openGroupId == group.id,
                onToggle = { viewModel.toggleGroup(group.id) },
                onOptionToggle = { optionId -> viewModel.toggleOption(optionId) },
                isOptionSelected = { viewModel.isOptionSelected(optionId) },
                selectedCount = { viewModel.getSelectedCount(group.id) }
            )
        }

        // Chips excluyentes: "Ninguna" y "No lo sé"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ExclusiveChip(
                label = "Ninguna",
                id = HealthGroups.NINGUNA,
                isSelected = viewModel.isOptionSelected(HealthGroups.NINGUNA),
                onToggle = { viewModel.toggleOption(HealthGroups.NINGUNA) }
            )
            ExclusiveChip(
                label = "No lo sé",
                id = HealthGroups.NO_LO_SE,
                isSelected = viewModel.isOptionSelected(HealthGroups.NO_LO_SE),
                onToggle = { viewModel.toggleOption(HealthGroups.NO_LO_SE) }
            )
        }

        // Campo de texto para "Otra condición o medicamento"
        OutlinedTextField(
            value = otherNote,
            onValueChange = { viewModel.updateOtherNote(it) },
            label = { Text("Otra condición o medicamento") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            maxLines = 1,
            maxLength = 120,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false
            ),
            colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.accent,
                unfocusedBorderColor = MaterialTheme.colorScheme.line
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón "Continuar" anclado al pie (siempre habilitado)
        Button(
            onClick = onNext,
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
fun HealthGroupCard(
    group: com.jorgeramos.thyroidsupport.domain.HealthGroup,
    isOpen: Boolean,
    onToggle: () -> Unit,
    onOptionToggle: (String) -> Unit,
    isOptionSelected: (String) -> Boolean,
    selectedCount: () -> Int
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onToggle() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.card
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp,
        border = if (isOpen)
            BorderStroke(1.5f.dp, MaterialTheme.colorScheme.accent)
        else
            BorderStroke(1.dp, MaterialTheme.colorScheme.line)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Encabezado del grupo
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = group.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight(600)
                    ),
                    color = MaterialTheme.colorScheme.ink
                )
                Spacer(modifier = Modifier.weight(1f))
                val count = selectedCount()
                if (count > 0) {
                    Text(
                        text = "$count elegida(s)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.inkSecondary
                    )
                }
                Icon(
                    imageVector = if (isOpen) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropDown,
                    contentDescription = if (isOpen) "Cerrar" else "Abrir",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.accent
                )
            }

            // Contenido expandido
            if (isOpen) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, bottom = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    group.options.forEach { option ->
                        HealthOptionChip(
                            label = option.label,
                            id = option.id,
                            isSelected = isOptionSelected(option.id),
                            onToggle = { onOptionToggle(option.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HealthOptionChip(
    label: String,
    id: String,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onToggle() },
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
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ExclusiveChip(
    label: String,
    id: String,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Chip(
        onClick = { onToggle() },
        selected = isSelected,
        colors = ChipDefaults.colors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.accent else MaterialTheme.colorScheme.tint,
            selectedContainerColor = MaterialTheme.colorScheme.accent,
            unselectedContainerColor = MaterialTheme.colorScheme.tint,
            selectedContentColor = Color(0xFFFFFFFF),
            unselectedContentColor = MaterialTheme.colorScheme.ink
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.accent,
                    uncheckedColor = MaterialTheme.colorScheme.line
                ),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.ink
            )
        }
    }
}
