package com.jorgeramos.thyroidsupport.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jorgeramos.thyroidsupport.ui.DashboardScreen
import com.jorgeramos.thyroidsupport.ui.ExportScreen
import com.jorgeramos.thyroidsupport.ui.LabScreen
import com.jorgeramos.thyroidsupport.ui.MedicationScreen
import com.jorgeramos.thyroidsupport.ui.RecommendationScreen
import com.jorgeramos.thyroidsupport.ui.SymptomLogScreen

@Composable
fun ThyroidApp(navController: NavHostController = rememberNavController()) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(padding)
        ) {
            composable("dashboard") {
                DashboardScreen(onNavigateToSymptomLog = {
                    navController.navigate("symptomLog")
                })
            }
            composable("symptomLog") {
                SymptomLogScreen(onNavigateBack = {
                    navController.popBackStack()
                })
            }
            composable("lab") {
                LabScreen(onNavigateBack = {
                    navController.popBackStack()
                })
            }
            composable("medication") {
                MedicationScreen(onNavigateBack = {
                    navController.popBackStack()
                })
            }
            composable("recommendation") {
                RecommendationScreen(onNavigateBack = {
                    navController.popBackStack()
                })
            }
            composable("export") {
                ExportScreen(onNavigateBack = {
                    navController.popBackStack()
                })
            }
        }
    }
}
