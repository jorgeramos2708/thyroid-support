package com.jorgeramos.thyroidsupport.domain

data class Recommendation(
    val medication: MedicationRecommendation?,
    val supplement: SupplementRecommendation?,
    val diet: DietRecommendation?,
    val exercise: ExerciseRecommendation?,
    val warning: String?,
    val sources: List<String>
)

data class MedicationRecommendation(
    val name: String,
    val dose: String,
    val frequency: String,
    val rationale: String,
    val evidenceLevel: String
)

data class SupplementRecommendation(
    val name: String,
    val dose: String,
    val rationale: String,
    val evidenceLevel: String,
    val contraindications: List<String>
)

data class DietRecommendation(
    val focus: String,
    val foodsToInclude: List<String>,
    val foodsToLimit: List<String>,
    val rationale: String,
    val evidenceLevel: String
)

data class ExerciseRecommendation(
    val type: String,
    val frequency: String,
    val duration: String,
    val rationale: String,
    val evidenceLevel: String
)
