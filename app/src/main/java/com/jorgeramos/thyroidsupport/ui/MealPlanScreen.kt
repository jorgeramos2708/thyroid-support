package com.jorgeramos.thyroidsupport.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgeramos.thyroidsupport.domain.MealPlanEntry
import com.jorgeramos.thyroidsupport.domain.MealType
import com.jorgeramos.thyroidsupport.presentation.MealPlanViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MealPlanScreen(
    patientId: Long = 1,
    onNavigateBack: () -> Unit,
    viewModel: MealPlanViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val entries by viewModel.entries.observeAsState()
    val loading by viewModel.loading.observeAsState(false)
    val error by viewModel.error.observeAsState()

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Agrupar entradas por fecha
    val entriesByDate = entries?.groupBy { it.date } ?: emptyMap()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )

        Text(
            text = "Registra tus comidas para hacer seguimiento de nutrientes relevantes (yodo, selenio)",
            style = MaterialTheme.typography.bodyMedium
        )

        // Formulario de nueva entrada
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Nueva comida", style = MaterialTheme.typography.titleMedium)

                var mealType by remember { mutableStateOf(MealType.DESAYUNO) }
                var foods by remember { mutableStateOf("") }
                var calories by remember { mutableStateOf("") }
                var notes by remember { mutableStateOf("") }

                DropdownMenu(
                    expanded = true,
                    onDismissRequest = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MealType.values().forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = { mealType = type }
                        )
                    }
                }
                Text(text = "Tipo: ${mealType.name}", style = MaterialTheme.typography.bodyMedium)

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
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.foundation.text.KeyboardType.Number
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
                                    notes = notes.takeIf { it.isNotEmpty() }.orNull()
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

        // Lista de entradas
        if (entriesByDate.isNotEmpty()) {
            Text(text = "Historial", style = MaterialTheme.typography.titleMedium)
            entriesByDate.entries.sortedByDescending { it.key }.forEach { (date, dayEntries) ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = dateFormat.format(Date(date)),
                            style = MaterialTheme.typography.titleMedium
                        )
                        dayEntries.forEach { entry ->
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
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
