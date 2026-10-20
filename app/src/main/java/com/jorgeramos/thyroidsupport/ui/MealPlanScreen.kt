package com.jorgeramos.thyroidsupport.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.domain.MealPlanEntry
import com.jorgeramos.thyroidsupport.domain.MealType
import com.jorgeramos.thyroidsupport.presentation.MealPlanViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlanScreen(
    patientId: Long = 1,
    onNavigateBack: () -> Unit,
    viewModel: MealPlanViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val entries by viewModel.entries.observeAsState(emptyList())
    val loading by viewModel.loading.observeAsState(false)
    val error by viewModel.error.observeAsState()

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val entriesByDate = entries.groupBy { it.date }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plan de Alimentación") },
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Registra tus comidas para hacer seguimiento de nutrientes relevantes (yodo, selenio)",
                style = MaterialTheme.typography.bodyMedium
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Nueva comida", style = MaterialTheme.typography.titleMedium)

                    var mealType by remember { mutableStateOf(MealType.DESAYUNO) }
                    var expandedDropdown by remember { mutableStateOf(false) }
                    var foods by remember { mutableStateOf("") }
                    var calories by remember { mutableStateOf("") }
                    var notes by remember { mutableStateOf("") }

                    ExposedDropdownMenuBox(
                        expanded = expandedDropdown,
                        onExpandedChange = { expandedDropdown = !expandedDropdown }
                    ) {
                        OutlinedTextField(
                            value = mealType.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo de comida") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedDropdown,
                            onDismissRequest = { expandedDropdown = false }
                        ) {
                            MealType.entries.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.name) },
                                    onClick = {
                                        mealType = type
                                        expandedDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = foods,
                        onValueChange = { foods = it },
                        label = { Text("Alimentos (separados por coma)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = calories,
                        onValueChange = { calories = it },
                        label = { Text("Calorías") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notas") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.addEntry(
                                    MealPlanEntry(
                                        date = System.currentTimeMillis(),
                                        mealType = mealType,
                                        foods = foods.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                                        calories = calories.toIntOrNull() ?: 0,
                                        iodineMcg = null,
                                        seleniumMcg = null,
                                        notes = notes.ifEmpty { null }
                                    )
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar comida")
                    }
                }
            }

            if (entriesByDate.isNotEmpty()) {
                Text(text = "Historial", style = MaterialTheme.typography.titleMedium)
                entriesByDate.entries.sortedByDescending { it.key }.forEach { (date, dayEntries) ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = dateFormat.format(Date(date)),
                                style = MaterialTheme.typography.titleMedium
                            )
                            dayEntries.forEach { entry ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${entry.mealType.name}: ${entry.foods.joinToString(", ")}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Calorías: ${entry.calories}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }

            error?.let { err ->
                Text(text = "Error: $err", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error))
            }

            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}
