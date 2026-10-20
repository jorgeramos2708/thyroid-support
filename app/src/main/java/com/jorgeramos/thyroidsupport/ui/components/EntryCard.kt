package com.jorgeramos.thyroidsupport.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EntryCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        androidx.compose.foundation.layout.Column(
            modifier = androidx.compose.foundation.layout.Column(Modifier).padding(16.dp)
        ) {
            Text(text = title, style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
            Text(text = subtitle, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
        }
    }
}
