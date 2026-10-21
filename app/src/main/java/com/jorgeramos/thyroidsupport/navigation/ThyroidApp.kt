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
import com.jorgeramos.thyroidsupport.ui.DiaryScreen
import com.jorgeramos.thyroidsupport.ui.ExerciseScreen
import com.jorgeramos.thyroidsupport.ui.ExportScreen
import com.jorgeramos.thyroidsupport.ui.LabScreen
import com.jorgeramos.thyroidsupport.ui.MealPlanScreen
import com.jorgeramos.thyroidsupport.ui.MedicationScreen
import com.jorgeramos.thyroidsupport.ui.OpenAIChatScreen
import com.jorgeramos.thyroidsupport.ui.RecommendationScreen
import com.jorgeramos.thyroidsupport.ui.SettingsScreen
import com.jorgeramos.thyroidsupport.ui.SymptomLogScreen
import com.jorgeramos.thyroidsupport.ui.home.HomeScreen
import com.jorgeramos.thyroidsupport.ui.onboarding.ConditionSubtypeScreen
import com.jorgeramos.thyroidsupport.ui.onboarding.OnboardingStep1Screen
import com.jorgeramos.thyroidsupport.ui.onboarding.OnboardingStep2Screen

@Composable
fun ThyroidApp(navController: NavHostController = rememberNavController()) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "onboardingStep1",
            modifier = Modifier.padding(padding)
        ) {
            // Onboarding
            composable("onboardingStep1") {
                OnboardingStep1Screen(
                    onNext = {
                        // Si es "Otra", ir directo a Paso 2
                        navController.navigate("onboardingStep2") {
                            popUpTo("onboardingStep1") { inclusive = false }
                        }
                    },
                    onBack = {},
                    onNavigateToSubtype = {
                        navController.navigate("conditionSubtype") {
                            popUpTo("onboardingStep1") { inclusive = false }
                        }
                    }
                )
            }
            composable("conditionSubtype") {
                ConditionSubtypeScreen(
                    onNext = {
                        navController.navigate("onboardingStep2") {
                            popUpTo("onboardingStep1") { inclusive = false }
                        }
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable("onboardingStep2") {
                OnboardingStep2Screen(
                    onFinish = {
                        navController.navigate("home") {
                            popUpTo("onboardingStep1") { inclusive = true }
                        }
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            // Home
            composable("home") {
                HomeScreen(
                    onNavigateToSymptoms = {
                        navController.navigate("symptomLog")
                    },
                    onNavigateToEvolution = {
                        navController.navigate("lab")
                    },
                    onNavigateToExercise = {
                        navController.navigate("exercise")
                    },
                    onNavigateToAdvice = {
                        navController.navigate("recommendation")
                    },
                    onNavigateToExport = {
                        navController.navigate("export")
                    },
                    onNavigateToAssistant = {
                        navController.navigate("openAI")
                    },
                    onNavigateToLab = {
                        navController.navigate("lab")
                    },
                    onNavigateToMedication = {
                        navController.navigate("medication")
                    }
                )
            }

            // Existing screens
            composable("dashboard") {
                DashboardScreen(
                    onNavigateToSymptomLog = {
                        navController.navigate("symptomLog")
                    },
                    onNavigateToLab = {
                        navController.navigate("lab")
                    },
                    onNavigateToMedication = {
                        navController.navigate("medication")
                    },
                    onNavigateToRecommendation = {
                        navController.navigate("recommendation")
                    },
                    onNavigateToExport = {
                        navController.navigate("export")
                    },
                    onNavigateToMealPlan = {
                        navController.navigate("mealPlan")
                    },
                    onNavigateToExercise = {
                        navController.navigate("exercise")
                    },
                    onNavigateToDiary = {
                        navController.navigate("diary")
                    },
                    onNavigateToOpenAI = {
                        navController.navigate("openAI")
                    },
                    onNavigateToSettings = {
                        navController.navigate("settings")
                    }
                )
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
            composable("mealPlan") {
                MealPlanScreen(onNavigateBack = {
                    navController.popBackStack()
                })
            }
            composable("exercise") {
                ExerciseScreen(onNavigateBack = {
                    navController.popBackStack()
                })
            }
            composable("diary") {
                DiaryScreen(onNavigateBack = {
                    navController.popBackStack()
                })
            }
            composable("openAI") {
                OpenAIChatScreen(onNavigateBack = {
                    navController.popBackStack()
                })
            }
            composable("settings") {
                SettingsScreen(onNavigateBack = {
                    navController.popBackStack()
                })
            }
        }
    }
}
