package com.jorgeramos.thyroidsupport.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DateTimeLabel(timestampMs: Long) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    Text(
        text = dateFormat.format(Date(timestampMs)),
        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
    )
}
