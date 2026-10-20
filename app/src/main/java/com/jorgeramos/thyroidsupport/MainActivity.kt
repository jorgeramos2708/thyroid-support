package com.jorgeramos.thyroidsupport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.jorgeramos.thyroidsupport.navigation.ThyroidApp
import com.jorgeramos.thyroidsupport.ui.theme.ThyroidSupportTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThyroidSupportTheme {
                Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ThyroidApp()
                }
            }
        }
    }
}
