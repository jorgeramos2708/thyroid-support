package com.jorgeramos.thyroidsupport.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var apiKey by remember { mutableStateOf("") }
    var showingKey by remember { mutableStateOf(false) }

    val prefs: SharedPreferences = context.getSharedPreferences("thyroid_support_prefs", Context.MODE_PRIVATE)
    var savedKey by remember { mutableStateOf(prefs.getString("openai_api_key", "") ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Configura tu API Key de OpenAI para usar el asistente de IA.",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "La clave se almacena cifrada localmente en el dispositivo y nunca se envía a terceros excepto a OpenAI para procesar tus consultas.",
                style = MaterialTheme.typography.bodySmall
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "API Key de OpenAI", style = MaterialTheme.typography.titleMedium)

                    OutlinedTextField(
                        value = apiKey,
                        onValueChange = { apiKey = it },
                        label = { Text("sk-...") },
                        visualTransformation = if (showingKey) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            autoCorrect = false
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { showingKey = !showingKey }
                        ) {
                            Icon(
                                imageVector = if (showingKey) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Mostrar/ocultar clave"
                            )
                        }

                        Button(
                            onClick = {
                                if (apiKey.isNotBlank()) {
                                    prefs.edit().putString("openai_api_key", apiKey).apply()
                                    savedKey = apiKey
                                }
                            },
                            enabled = apiKey.isNotBlank()
                        ) {
                            Text("Guardar")
                        }

                        if (savedKey.isNotBlank()) {
                            Button(
                                onClick = {
                                    prefs.edit().remove("openai_api_key").apply()
                                    savedKey = ""
                                    apiKey = ""
                                }
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }

                    if (savedKey.isNotBlank()) {
                        Text(
                            text = "API Key configurada (${savedKey.take(4)}...)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Privacidad y datos", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Todos los datos de salud se almacenan localmente en tu dispositivo. No se sincronizan con la nube. Los datos enviados a OpenAI son únicamente el texto de tus preguntas; no se envían datos identificables del paciente.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
